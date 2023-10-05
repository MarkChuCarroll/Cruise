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

end Pong(I(I(Z()))) to ^FF
