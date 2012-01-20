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
package org.scientopia.goodmath.cruise.ast;

import java.util.Map;

import org.scientopia.goodmath.cruise.parser.ParseException;


/**
 * TupleElements are things that can be part of a tuple in the AST of a program.
 * @author markcc
 *
 */
public abstract class ValueExpression {
  public abstract boolean match(ValueExpression te, Map<String,ValueExpression> map);
  public abstract ValueExpression instantiateVariables(Map<String,ValueExpression> map) throws UnmatchedVariableException;
  public abstract boolean isConcrete();
  public abstract String toString();

  public static TupleExpression intToTuple(int i) {
    if (i == 0) {
      return new TupleExpression("Z");
    }
    else { //(i > 0)
      TupleExpression result = new TupleExpression("I");
      TupleExpression next = intToTuple(i-1);
      result.getElements().add(next);
      return result;
    }
  }
}
