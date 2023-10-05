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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The AST for a full program. A program consists of three parts:
 * <ol>
 * <li>A set of actor specifications
 * <li>A set of actor instantiation expressions
 * <li>A set of messages to send to start the execution of a program.
 * </ol>
 * 
 * @author markcc
 *
 */
public class Program {
  public Program() {
  }

  public void addActor(ActorType a) {
    actors.put(a.getName(), a);
  }

  public Set<String> getActorNames() {
    return actors.keySet();
  }

  public ActorType getActor(String name) {
    return actors.get(name);
  }

  public SendMessageStatement getInitialMessage() {
    return message;
  }

  public void setInitialMessage(SendMessageStatement msg) {
    message = msg;
  }

  public Map<String, ActorType> actors = new HashMap<String, ActorType>();
  public SendMessageStatement message;
}
