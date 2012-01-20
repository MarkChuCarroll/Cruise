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

actor !Adder {
  behavior :Add() {
    on Plus(Z(),$x, $target) { send $x to $target }
    on Plus(I($x), $y, $target) { send Plus($x,I($y), $target) to $self }
  }
  initial :Add
}

actor !Done {
  behavior :Done() {
    on Result($x) { }
  }
  initial :Done
}

instantiate !Adder() as adder
instantiate !Done() as done

send Plus(I(I(I(Z()))),I(I(Z())), out) to adder