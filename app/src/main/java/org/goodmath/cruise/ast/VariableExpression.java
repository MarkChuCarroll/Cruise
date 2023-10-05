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

import java.util.Map;

/**
 * A tuple element representing a variable filling a position in an abstract
 * tuple.
 * 
 * @author markcc
 *
 */
public class VariableExpression extends ValueExpression {
  public VariableExpression(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  protected String name;

  @Override
  public boolean match(ValueExpression other, Map<String, ValueExpression> map) {
    if (map.containsKey(name)) {
      ValueExpression te = map.get(name);
      return te.match(other, map);
    } else {
      map.put(name, other);
      return true;
    }
  }

  @Override
  public ValueExpression instantiateVariables(Map<String, ValueExpression> replacementMap)
      throws UnmatchedVariableException {
    if (replacementMap.containsKey(name)) {
      return replacementMap.get(name);
    } else {
      throw new UnmatchedVariableException("No variable " + name);
    }
  }

  @Override
  public boolean isConcrete() {
    return false;
  }

  @Override
  public String toString() {
    return getName();
  }
}
