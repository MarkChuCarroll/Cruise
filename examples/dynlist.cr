//************************************************************************
// * Cruise - a programming language of bad actors
// * Copyright 2012 Mark C. Chu-Carroll
// * 
// * Licensed under the Apache License, Version 2.0 (the "License"); 
// * you may not use this file except in compliance with the License. 
// * You may obtain a copy of the License at 
// *      http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, 
// * software distributed under the License is distributed on an 
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
// * either express or implied. See the License for the specific 
// * language governing permissions and limitations under the License.
// * 

actor !Nil {
   behavior :Nil() {
      on IsNil($target) { send True() to $target }   
      on Print() { send Nil() to out 
                   send Newline() to out }
   }
   initial :Nil
}

actor !Cons {
  behavior :Cons($head,$tail) {
    on GetHead($target) { send $head to $target }
    on GetTail($target) { send $tail to $target }
    on SetHead($val) { adopt :Cons($val, $tail) }
    on SetTail($val) { adopt :Cons($head, $val) }
    on IsNil($target) { send False() to $target }
    on Insert($val, $target) { instantiate !Cons($val, $self) to $new
                               send NewActor($new) to $target }
    on Print() { send $head to out
                 send Print() to $tail
               }                              
  }   
  initial :Cons
}

actor !Unwrapper {
   behavior :Main() {
      on NewActor($name) { send Print() to $name }
   }
   initial :Main
}


instantiate !Nil() as nil
instantiate !Cons(One(),nil) as one
instantiate !Cons(Two(),one) as two
instantiate !Cons(Three(), two) as three
instantiate !Unwrapper() as unwrapper

send Print() to three


send Insert(Four(), unwrapper) to three
