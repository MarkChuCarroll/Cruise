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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The AST specification of an actor type
 * @author markcc
 *
 */
public class ActorType {
  public ActorType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setInitialBehavior(String name) {
    initial = behaviors.get(name);
  }

  public Behavior getInitialBehavior() {
    return initial;
  }

  public Behavior getBehavior(String name) {
    return behaviors.get(name);
  }

  public void addBehavior(Behavior b) {
    behaviors.put(b.getName(), b);
  }

  public List<String> getInitParameters() {
    return initial.getParameters();
  }

  protected String name;
  protected Behavior initial;
  protected Map<String,Behavior> behaviors = new HashMap<String,Behavior>();
}
