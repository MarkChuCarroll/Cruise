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
import org.goodmath.cruise.interp.Engine;

public class CreateActorStatement {

  public CreateActorStatement(String type) {
    actorType = type;
  }

  public ValueExpression getTarget() {
    return target;
  }

  static int ACTIDNUM = 0;

  public void execute(Engine engine, Map<String, ValueExpression> state) throws CruiseRuntimeException {
    ActorIdentifier id = null;
    if (target instanceof ActorIdentifier) {
      id = (ActorIdentifier) target;
    } else {
      // Target is a variable. Create an actor name, and bind it to the variable.
      String varname = ((VariableExpression) target).getName();
      id = new ActorIdentifier("^@" + actorType + ACTIDNUM);
      ACTIDNUM++;
      state.put(varname, id);
    }
    List<ValueExpression> instantiatedArgs = new ArrayList<ValueExpression>(parameters.size());
    for (ValueExpression arg : parameters) {
      ValueExpression instantiated = arg.instantiateVariables(state);
      instantiatedArgs.add(instantiated);
    }
    engine.instantiateActor(actorType, id, instantiatedArgs);
  }

  public ActorIdentifier getConcreteTarget() throws CruiseTypeError {
    if (target instanceof ActorIdentifier) {
      return (ActorIdentifier) target;
    } else {
      throw new CruiseTypeError("Instantiating to an unbound target");
    }
  }

  public void setTarget(VariableExpression var) {
    target = var;
  }

  public void setTarget(ActorIdentifier ident) {
    target = ident;
  }

  public void run(Engine engine) throws CruiseRuntimeException {
    throw new CruiseRuntimeException("Not implemented yet");
  }

  public List<ValueExpression> getParameters() {
    return parameters;
  }

  public String getActorType() {
    return actorType;
  }

  protected ValueExpression target;
  protected List<ValueExpression> parameters = new ArrayList<ValueExpression>();
  protected String actorType;
}
