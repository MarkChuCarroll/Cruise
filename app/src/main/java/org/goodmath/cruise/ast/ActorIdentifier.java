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
 * The AST representation of a by-name reference to an actor.
 *
 * Actors perform computations by sending messages. Each actor
 * has a unique identifier called either its ID, or its
 * _mailbox address_. If you have the identifier of an actor,
 * then you can sennd messages to that actor.
 *
 */
public class ActorIdentifier extends ValueExpression {
  public ActorIdentifier(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  protected String name;

  @Override
  public ValueExpression instantiateVariables(Map<String, ValueExpression> map)
      throws UnmatchedVariableException {
    return new ActorIdentifier(name);
  }

  @Override
  public boolean match(ValueExpression te, Map<String, ValueExpression> map) {
    if (!(te instanceof ActorIdentifier)) {
      return false;
    } else {
      ActorIdentifier tea = (ActorIdentifier) te;
      if (tea.getName().equals(getName())) {
        return true;
      } else {
        return false;
      }
    }
  }

  @Override
  public boolean isConcrete() {
    return true;
  }

}
