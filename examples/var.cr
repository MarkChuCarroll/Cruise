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
