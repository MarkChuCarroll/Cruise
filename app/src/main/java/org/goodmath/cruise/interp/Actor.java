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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.goodmath.cruise.ast.ActorIdentifier;
import org.goodmath.cruise.ast.ActorType;
import org.goodmath.cruise.ast.Behavior;
import org.goodmath.cruise.ast.CreateActorStatement;
import org.goodmath.cruise.ast.MessageHandler;
import org.goodmath.cruise.ast.SendMessageStatement;
import org.goodmath.cruise.ast.TupleExpression;
import org.goodmath.cruise.ast.ValueExpression;

public class Actor implements IActor {
  public Actor(Engine engin, String name, ActorType spec, List<ValueExpression> params)
      throws CruiseRuntimeException {
    engine = engin;
    actorName = name;
    actorSpec = spec;
    adoptBehavior(actorSpec.getInitialBehavior(), params, state);

  }

  public void adoptBehavior(Behavior beh, List<ValueExpression> params, Map<String, ValueExpression> bindings)
      throws CruiseRuntimeException {
    state = new HashMap<String, ValueExpression>();
    if (beh.getParameters().size() != params.size()) {
      throw new CruiseRuntimeException("Behavior parameters do not match arguments");
    }
    int i = 0;
    for (String name : beh.getParameters()) {
      state.put(name, params.get(i).instantiateVariables(bindings));
      i++;
    }
    behavior = beh;
  }

  /*
   * (non-Javadoc)
   * 
   * @see cruise.interp.IActor#getName()
   */
  public String getName() {
    return actorName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see cruise.interp.IActor#enqueueMessage(cruise.ast.Tuple)
   */
  public void enqueueMessage(TupleExpression msg) throws CruiseRuntimeException {
    if (!msg.isConcrete()) {
      throw new CruiseRuntimeException("Cannot enqueue a non-concrete message");
    }
    messages.add(msg);
  }

  /*
   * (non-Javadoc)
   * 
   * @see cruise.interp.IActor#processMessage()
   */
  public void processMessage() throws CruiseRuntimeException {
    TupleExpression tuple = messages.remove();
    for (MessageHandler mh : behavior.getHandlers()) {
      Map<String, ValueExpression> match = new HashMap<String, ValueExpression>(state);
      if (mh.matches(tuple, match)) {
        match.put("$self", new ActorIdentifier(this.getName()));
        // First, instantiate actors.
        for (CreateActorStatement create : mh.getCreations()) {
          create.execute(engine, match);
        }
        for (SendMessageStatement outmsg : mh.getSends()) {
          SendMessageStatement msg = (SendMessageStatement) outmsg.instantiateVariables(match);
          engine.sendMessage(this, msg.getConcreteTarget().getName(), msg.getConcreteMessage());
        }
        String replacementBehavior = mh.getReplacementBehavior();
        if (replacementBehavior != null) {
          behavior = actorSpec.getBehavior(replacementBehavior);
          if (behavior == null) {
            throw new CruiseRuntimeException("No behavior " + behavior + " in actor type " + actorSpec.getName());
          }
          adoptBehavior(behavior, mh.getReplacementParameters(), match);
        }
        return;
      }
    }
    throw new CruiseRuntimeException("Actor type " + actorSpec.getName() + " has no action to match message " + tuple);
  }

  /*
   * (non-Javadoc)
   * 
   * @see cruise.interp.IActor#poll()
   */
  public boolean poll() {
    return !messages.isEmpty();
  }

  protected Queue<TupleExpression> messages = new LinkedList<TupleExpression>();
  protected Map<String, ValueExpression> state = null;
  protected Behavior behavior = null;
  protected String actorName;
  protected ActorType actorSpec;
  protected Engine engine;
}
