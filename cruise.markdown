Quick overview of the actor model
-------------------------------------

Actors are a theoretical model of computation, which is designed to describe completely
asynchronous parallel computation. Doing things totally asynchronously is very strange, and very counter-intuitive. But the fact of the matter is, in real distributed systems, everything *is* fundamentally asynchronous, so being able to describe distributed systems in terms of a simple, analyzable model is a good thing.

According to the actor model, a computation is described by a collection of things called, what else, actors. An actor has a *mailbox*, and a *behavior*. The mailbox is a uniquely named place where messages sent to an actor can be queued; the behavior is a definition of how the actor is going to process a message from its mailbox. The behavior gets to look at the message, and based on its
contents, it can do three kinds of things:

1. Create other actors.
2. Send messages to other actors whose mailbox it knows.
3. Specify a new behavior for the actor to use to process its next message.


You can do pretty much anything you need to do in computations with that basic mechanism. The catch
is, as I said, it's all asynchronous. So, for example, if you want to write an actor that adds two
numbers, you *can't* do it by what you'd normally think of as a subroutine call. In a lot of ways, it *looks* like a method call in something like Smalltalk: one actor (object) sends a message to another actor, and in response, the receiver takes some action specified by them message.

But subroutines and methods are synchronous, and *nothing* in actors is synchronous. In an object-oriented language, when you send a message, you stop and wait until the receiver of the message is done with it. In Actors, it doesn't work that way: you send a message, and it's sent, over and done with. You don't wait for anything; you're done.  If you want a reply, you need to send the the other actor a reference to your mailbox, and make sure that your behavior knows what to do when the reply comes in.

It ends up looking something like the continuation passing form of a functional programming language: to do a subroutine-like operation, you need to pass an extra parameter to the subroutine invocation; that extra parameter is the *intended receiver* of the result. 

You'll see some examples of this when we get to some code.

Tuples - A Really Ugly Way of Handling Data
----------------------------------------------

Cruise has a strange data model. The idea behind it is to make it easy to build actor behaviors around the idea of pattern matching. The easiest/stupidest way of doing this is to make all data consist of tagged tuples. A tagged tuple consists of a tag name (an identifier starting with an uppercase letter), and a list of values enclosed in the tuple. The values inside of a tuple can be either other tuples, or actor names (identifiers starting with lower-case letters).

So, for example, `Foo(Bar(), adder)` is a tuple. The tag is "`Foo`". It's contents are another tuple, "`Bar()`", and an actor name, "`adder`". 

Since tuples and actors are the only things that exist, we need to construct all other types
of values from some combination of tuples and actors. To do math, we can use tuples to build up Peano numbers. The tuple "`Z()`" is zero; "`I(n)`" is the number `n+1'. So, for example, 3 is "`I(I(I(Z())))`".

The only way to decompose tuples is through pattern matching in messages. In an actor behavior. message handlers specify a *tuple pattern*, which is a tuple where some positions may be filled by{\em unbound} variables. When a tuple is matched against a pattern, the variables in the pattern are bound to the values of the corresponding elements of the tuple.

A few examples:

* matching `I(I(I(Z())))` with `I($x)` will succeed with `$x` bound to `I(I(Z))`.
* matching `Cons(X(),Cons(Y(),Cons(Z,Nil())))` with `Cons($x,$y)` will succeed with
   $x bound to `X()`, and $y bound to `Cons(Y(),Cons(Z(),Nil()))`.
* matching `Cons(X(),Cons(Y(),Cons(Z(),Nil())))` with `Cons($x, Cons(Y(), Cons($y, Nil())))` will succeed with `$x` bound to `X()`, and `$y` bound to `Z()`.


Cruise - the Language of Bad Actors
--------------------------------------

Instead of my rambling on even more, let's take a look at some Cruise programs. We'll
start off with Hello World, sort of.

    actor !Hello {
        behavior :Main() {
	        on Go() { send Hello(World()) to out }
        }
        initial :Main
    }
    
    instantiate !Hello() as hello
    send Go() to hello

This declares an actor type "!Hello"; it's got one behavior with no parameters. It only knows
how to handle one message, "Go()". When it receives go, it sends a hello world tuple to the actor named "out", which is a built-in that just prints whatever is sent to it.

Let's be a bit more interesting, and try something using integers. Here's some code to do
a greater than comparison:

    actor !GreaterThan {
       behavior :Compare() {
          on GT(Z(),Z(), $action, $iftrue, $iffalse) { send $action to $iffalse }
          on GT(Z(), I($x), $action, $iftrue, $iffalse) { send $action to $iffalse }
          on GT(I($x), Z(), $action, $iftrue, $iffalse) { send $action to $iftrue }
          on GT(I($x), I($y), $action, $iftrue, $iffalse) { send GT($x,$y,$action,$iftrue,$iffalse) to $self }
       }
       initial :Compare
    }
    
    actor !True {
        behavior :True() {
           on Result() { send True() to out}
        }
       initial :True
    }
    
    actor !False {
       behavior :False() {
          on Result() { send False() to out}
        }
        initial :False
    }
    
    instantiate !True() as true
    instantiate !False() as false
    instantiate !GreaterThan() as greater
    
    send GT(I(I(Z())), I(Z()), Result(), true, false) to greater
    send GT(I(I(Z())), I(I(I(Z()))), Result(), true, false) to greater
    send GT(I(I(Z())), I(I(Z())), Result(), true, false) to greater


This is typical of how you do "control flow" in Cruise: you set up different actors
for each branch, and pass those actors names to the test; one of them will receive
a message to continue the execution.

What about multiple behaviors? Here's a trivial example of a flip-flop:

    actor !FlipFlop {
       behavior :Flip() {
          on Ping($x) { send Flip($x) to out
                        adopt :Flop() }
          on Pong($x) { send Flip($x) to out}
       }
       behavior :Flop() {
          on Ping($x) { send Flop($x) to out }
          on Pong($x) { send Flop($x) to out
                        adopt :Flip() }
       }
       initial :Flip
    }
    
    instantiate !FlipFlop() as ff
    
    send Ping(I(I(Z()))) to ff
    send Ping(I(I(Z()))) to ff
    send Ping(I(I(Z()))) to ff
    send Ping(I(I(Z()))) to ff
    send Pong(I(I(Z()))) to ff
    send Pong(I(I(Z()))) to ff
    send Pong(I(I(Z()))) to ff
    send Pong(I(I(Z()))) to ff

If the actor is in the ":Flip" behavior, then when it gets a "Ping", it sends "Flip" to out, and switches behavior to flop. If it gets point, it just sents "Flip" to out, and stays in ":Flip".
The ":Flop" behavior is pretty much the same idea, accept that it switches behaviors on "Pong".

An example of how behavior changing can actually be useful is implementing settable variables:

    actor !Var {
        behavior :Undefined() {
           on Set($v) { adopt :Val($v) }
           on Get($target) { send Undefined() to $target }
           on Unset() { }
        }
        behavior :Val($val) {
            on Set($v) { adopt :Val($v) }
            on Get($target) { send $val to $target }
            on Unset() { adopt :Undefined() }
        }
        initial :Undefined
    }
    
    instantiate !Var() as v
    
    send Get(out) to v
    send Set(I(I(I(Z())))) to v
    send Get(out) to v


Two more programs, and I'll stop torturing you. First, a simple adder:


    actor !Adder {
        behavior :Add() {
            on Plus(Z(),$x, $target) { send $x to $target }
            on Plus(I($x), $y, $target) { send Plus($x,I($y), $target) to $self }
        }
        initial :Add
    }
    
    actor !Done {
       behavior :Done() {
          on Result($x) { send Result($x) to out }
       }
       initial :Done
    }
    
    instantiate !Adder() as adder
    instantiate !Done() as done
    
    send Plus(I(I(I(Z()))),I(I(Z())), out) to adder

Pretty straightforward - the only interesting thing about it is the way that it sends the result of invoking add to a continuation actor.

Now, let's use an addition actor to implement a multiplier actor. This shows off some interesting techniques, like carrying auxiliary values that will be needed by the continuation. It also shows
you that I cheated, and added integers to the parser; they're translated into the peano-tuples 
by the parser.

    actor !Adder {
       behavior :Add() {
          on Plus(Z(),$x, $misc, $target) { send Sum($x, $misc) to $target }
          on Plus(I($x), $y, $misc, $target) { 
	          send Plus($x,I($y), $misc, $target) to $self 
	      }
       }
       initial :Add
    }
    
    actor !Multiplier {
    	behavior :Mult() {
            on Mult(I($x), $y, $sum, $misc, $target) { 
	           send Plus($y, $sum, MultMisc($x, $y, $misc, $target), $self) to adder
	        }
            on Sum($sum, MultMisc(Z(), $y, $misc, $target)) { 
	            send Product($sum, $misc) to $target 
	        }
            on Sum($sum, MultMisc($x, $y, $misc, $target)) {
	           send Mult($x, $y, $sum, $misc, $target) to $self 
	        }
        }
        initial :Mult
    }
    
    instantiate !Adder() as adder
    instantiate !Multiplier() as multiplier
    send Mult(32, 191, 0, Nil(), out) to multiplier

