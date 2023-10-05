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
package org.goodmath.cruise.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.goodmath.cruise.interp.CruiseRuntimeException;

/**
 * Tuples are the basic structure of messages and data in Cruise. A tuple
 * consists of a tuple type identified by a name starting with an
 * upper-case letter, followed by parens containing a list of values.
 * Values can be other tuples, variables, or actor names.
 *
 * Tuples in a programs AST can contain variable references. Tuples
 * containing variables are called <em>abstract</em> tuples; tuples
 * where none of the values are variables are called <em>concrete</em> tuples.
 * At runtime, all tuples passed by messages must be concrete.
 *
 * During message matching, a variable in a particular position in a tuple
 * matches any value placed in that position.
 * 
 * @author markcc
 *
 */
public class TupleExpression extends ValueExpression {
  public TupleExpression(String type) {
    tupleType = type;
  }

  public String getTupleType() {
    return tupleType;
  }

  public List<ValueExpression> getElements() {
    return elements;
  }

  protected String tupleType;
  protected List<ValueExpression> elements = new ArrayList<ValueExpression>();

  @Override
  public boolean match(ValueExpression te, Map<String, ValueExpression> map) {
    if (!(te instanceof TupleExpression)) {
      return false;
    }
    TupleExpression tuple = (TupleExpression) te;
    if (!(tuple.getTupleType().equals(getTupleType()))) {
      return false;
    } else {
      if (elements.size() == tuple.getElements().size()) {
        for (int i = 0; i < elements.size(); i++) {
          ValueExpression myElement = elements.get(i);
          ValueExpression otherElement = tuple.getElements().get(i);
          if (!myElement.match(otherElement, map)) {
            return false;
          }
        }
        return true;
      } else {
        return false;
      }
    }
  }

  @Override
  public ValueExpression instantiateVariables(Map<String, ValueExpression> map) throws UnmatchedVariableException {
    TupleExpression result = new TupleExpression(tupleType);
    for (ValueExpression el : elements) {
      result.getElements().add(el.instantiateVariables(map));
    }
    return result;
  }

  @Override
  public boolean isConcrete() {
    for (ValueExpression tel : getElements()) {
      if (!tel.isConcrete()) {
        return false;
      }
    }
    return true;
  }

  public String toString() {
    StringBuffer result = new StringBuffer();
    if (getTupleType().equals("I") || getTupleType().equals("Z")) {
      int i = intTupleToInt(this);
      result.append(i);
    } else {
      result.append(getTupleType());
      result.append("(");
      boolean first = true;
      for (ValueExpression el : elements) {
        if (!first) {
          result.append(", ");
        }
        result.append(el.toString());
        first = false;
      }
      result.append(")");
    }
    return result.toString();
  }

  private int intTupleToInt(TupleExpression msg) {
    if (msg.getTupleType().equals("Z")) {
      return 0;
    } else if (msg.getTupleType().equals("I") && msg.getElements().size() == 1
        && msg.getElements().get(0) instanceof TupleExpression) {
      return 1 + intTupleToInt((TupleExpression) msg.getElements().get(0));
    } else {
      return 0;
    }
  }

}
