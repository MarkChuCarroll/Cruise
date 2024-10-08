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

actor !Concatenator {
  behavior :Concat() {
    on Concat(Nil(), $right, $intermediate, $target) {
      send ConcatResult($right, $intermediate) to $target
    }
    on Concat(Cons($head, Nil()), $right, $intermediate, $target) {
      send ConcatResult(Cons($head, $right), $intermediate) to $target
    }
    on Concat(Cons($head, $t), $right, $intermediate, $target) {
      send Concat($t, $right, Temp($head, $target, $intermediate), $self) to $self
    }
    on ConcatResult($list, Temp($head, $target, $intermediate)) {
      send ConcatResult(Cons($head, $list), $intermediate) to $target
    }
  }
  initial :Concat
}

instantiate !Concatenator() as concat

send Concat(Cons(A(),Cons(B(), Cons(C(), Nil()))), 
            Cons(D(), Cons(E(), Cons(F(), Nil()))), Nil(), out) to concat


send Concat(Cons(A(),
                 Cons(Cons(B(), 
                           Cons(C(), Nil())
                          ), 
                      Nil()
                     )
                 ), 
                 Cons(D(), Cons(E(), Cons(F(), Nil()))), Nil(), out) to concat


