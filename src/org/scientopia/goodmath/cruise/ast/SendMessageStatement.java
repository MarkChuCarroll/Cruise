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

import org.scientopia.goodmath.cruise.interp.CruiseRuntimeException;


/**
 * A template for a message to be sent as part of an action. This may contain
 * variable references to variables defined in the corresponding message
 * pattern. If so, they must be substituted before this can be instantiated
 * and sent as a real message.
 *
 * @author markcc
 */
public class SendMessageStatement extends ValueExpression {
  /**
   * Create an outgoing message spec.
   * @param target a tuple element which must be either an ActorSpecifier
   *     or a Variable
   * @param message a message tuple which must be either a Tuple or a
   *     Variable which expands to a tuple.
   */
  public SendMessageStatement(ValueExpression target, ValueExpression message) {
    this.target = target;
    this.message = message;
  }

  @Override
  public ValueExpression instantiateVariables(Map<String, ValueExpression> map)
      throws UnmatchedVariableException {
    ValueExpression instTarget = target.instantiateVariables(map);
    TupleExpression instMessage = (TupleExpression) message.instantiateVariables(map);
    return new SendMessageStatement(instTarget, instMessage);
  }

  @Override
  // Outgoing messages are never matched against other tuples.
  public boolean match(ValueExpression te, Map<String, ValueExpression> map) {
    return false;
  }

  public ValueExpression getMessage() {
    return message;
  }

  /**
   * Check if the message is fully concrete, meaning that it has had all of its variable references replaced. If it is
   * complete, then return the message; if not, throw a runtime error.
   * @return the instantiated message.
   * @throws CruiseRuntimeException
   */
  public TupleExpression getConcreteMessage() throws CruiseRuntimeException {
    if (isConcrete()) {
      return (TupleExpression)message;
    }
    else {
      throw new CruiseRuntimeException("Message is not concrete");
    }
  }

  public ValueExpression getTarget() {
    return target;
  }

  /**
   * Get the target of this message if the message has been properly instantiated.
   * @return the instantiated message target
   * @throws CruiseRuntimeException
   */
  public ActorIdentifier getConcreteTarget() throws CruiseRuntimeException {
    if (target.isConcrete()) {
      return (ActorIdentifier)target;
    }
    else {
      throw new CruiseRuntimeException("Target is not concrete");
    }
  }

  protected ValueExpression target;
  protected ValueExpression message;

  @Override
  public boolean isConcrete() {
    if (!target.isConcrete()) {
      return false;
    }
    if (!message.isConcrete()) {
      return false;
    }
    return true;
  }

  public String toString() {
    return "<" + target.toString() + ":" + message.toString() + ">";
  }
}
