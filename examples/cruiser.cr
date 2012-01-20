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


