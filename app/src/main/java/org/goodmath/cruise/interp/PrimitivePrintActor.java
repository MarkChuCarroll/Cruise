/************************************************************************
 * Cruise - a programming language of bad actors
 * Copyright 2012 Mark C. Chu-Carroll
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 */

package org.goodmath.cruise.interp;

import org.goodmath.cruise.ast.TupleExpression;
import org.goodmath.cruise.ast.ValueExpression;

public class PrimitivePrintActor extends PrimitiveActor {
  public PrimitivePrintActor() {
  }

  @Override
  public void processMessage() throws CruiseRuntimeException {
    TupleExpression msg = messages.remove();
    printTuple(msg);
  }

  public void printTuple(TupleExpression msg) throws CruiseRuntimeException {
    if (msg.getTupleType().equals("String")) {
      for (ValueExpression te : msg.getElements()) {
        if (te instanceof TupleExpression) {
          TupleExpression t = (TupleExpression) te;
          System.out.print(t.getTupleType());
        } else {
          throw new CruiseRuntimeException("Illegal value in a string tuple");
        }
      }
    } else if (msg.getTupleType().equals("Newline")) {
      System.out.println("");
    } else {
      System.out.println(msg.toString());
    }
  }

  public String getName() {
    return "out";
  }

}
