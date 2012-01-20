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
package org.scientopia.goodmath.cruise.interp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scientopia.goodmath.cruise.ast.ActorIdentifier;
import org.scientopia.goodmath.cruise.ast.ActorType;
import org.scientopia.goodmath.cruise.ast.CreateActorStatement;
import org.scientopia.goodmath.cruise.ast.CruiseTypeError;
import org.scientopia.goodmath.cruise.ast.SendMessageStatement;
import org.scientopia.goodmath.cruise.ast.TupleExpression;
import org.scientopia.goodmath.cruise.ast.ValueExpression;
import org.scientopia.goodmath.cruise.parser.CruiseParser;


public class Engine {
  public static void main(String args[]) {
    boolean debug = false;
    String programFile = null;
    if (args[0].charAt(0) == '-') {
      if (args[0].equals("-trace")) {
        debug = true;
      }
      programFile = args[1];
    }
    else {
      programFile = args[0];
    }
    FileInputStream in = null;
    try {
      File f = new File(programFile);
      if (f.exists()) {
        in = new FileInputStream(new File(programFile));
      }
      else {
        System.err.println("File does not exist!");
      }
    }
    catch (IOException e) {
      System.err.println("IO Exception reading source file");
      System.exit(1);
    }
    Engine engine = null;
    try {
      CruiseParser parser = new CruiseParser(in);
      engine = parser.program();
      engine.trace = debug;
    }
    catch (Exception e) {
      System.err.println("Parse error reading program: "+ e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }

    try {
      engine.run();
    } catch (CruiseRuntimeException e) {
      System.err.println("Runtime error: "+ e);
    }

  }

  public Engine() {
    for (PrimitiveActor prim : primitives) {
      actors.put(prim.getName(), prim);
    }
  }

  public void addActorType(ActorType spec) {
    actorSpecs.put(spec.getName(), spec);
  }

  public void sendMessage(IActor from, String to, TupleExpression msg) throws CruiseRuntimeException {
    IActor target = null;
    if (to.equals("^Self")) {
      target = from;
    }
    else {
      if (actors.containsKey(to)) {
        target = actors.get(to);
      }
      else {
        throw new CruiseRuntimeException("Target actor: " + to + " is unknown");
      }
    }
    if (trace) {
      String fromName = (from != null ? from.getName() : "<startup>");
      System.out.println(fromName + "->" + to + ": " + msg);
    }
    target.enqueueMessage(msg);
  }

  public void addMessage(SendMessageStatement msg) {
    messages.add(msg);
  }

  public void instantiateActor(String atypeName, ActorIdentifier act, List<ValueExpression> args)
      throws CruiseRuntimeException {
    ActorType aType = actorSpecs.get(atypeName);
    if (aType == null) {
      throw new CruiseTypeError("Actor type " + atypeName + " not found");
    }
    Actor actor = new Actor(this, act.getName(), aType, args);
    actors.put(act.getName(), actor);
  }

  public void run() throws CruiseRuntimeException {
    for (CreateActorStatement create : creates) {
      ActorIdentifier act = create.getConcreteTarget();
      String atypeName = create.getActorType();
      instantiateActor(atypeName, act, create.getParameters());
    }
    for (SendMessageStatement msg : messages) {
      sendMessage(null, msg.getConcreteTarget().getName(), msg.getConcreteMessage());
    }
    while (true) {
      boolean processedMessage = false;
      for (IActor a : actors.values()) {
        if (a.poll()) {
          a.processMessage();
          processedMessage = true;
        }
      }
      if (!processedMessage) {
        break;
      }
    }
  }

  public List<CreateActorStatement> getCreates() {
    return creates;
  }

  public boolean trace = true;
  protected Map<String,IActor> actors = new HashMap<String,IActor>();
  protected Map<String,ActorType> actorSpecs = new HashMap<String,ActorType>();
  protected List<SendMessageStatement> messages = new ArrayList<SendMessageStatement>();
  protected List<CreateActorStatement> creates = new ArrayList<CreateActorStatement>();
  protected static List<PrimitiveActor> primitives;

  /**
   * Primitive actors call this method using a static initializer to register themselves.
   * @param a an instance of a primitive actor type to be included in the runtime.
   */
  public static void registerPrimitiveActor(PrimitiveActor a) {
    if (primitives == null) {
      primitives = new ArrayList<PrimitiveActor>();
    }
    primitives.add(a);
  }

  static {
    PrimitivePrintActor actor = new PrimitivePrintActor();
    Engine.registerPrimitiveActor(actor);
  }
}
