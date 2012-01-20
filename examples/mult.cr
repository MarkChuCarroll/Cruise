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
    on Plus(Z(),$x, $misc, $target) { send Sum($x, $misc) to $target }
    on Plus(I($x), $y, $misc, $target) { send Plus($x,I($y), $misc, $target) to $self }
  }
  initial :Add
}

// mult comment

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
