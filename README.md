# Cruise: a language of bad actors

This language is a joke. It's based on the actor model, which is very serious.
But as an implementation of the actor model, this is a cute piece of trash.
It's single-threaded, it's sloppy, it doesn't typecheck, it doesn't compile in
any way. It doesn't even give reasonable errors for bad command line arguments.
But it does run programs (badly).

To build it, just run:

```
gradle install
```

There's a set of example programs in the "examples" directory. To run
one of them, use the `gradle run` command. For example, to run "examples/adder.cr",
you'd run:

```
./app/build/install/cruise/bin/cruise adder.cr
```

To see exactly what's going on during execution, you can add the
"--trace" flag:

```
./app/build/install/cruise/bin/cruise --trace examples/adder.cr
```


