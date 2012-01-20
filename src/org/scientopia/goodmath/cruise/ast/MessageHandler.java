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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageHandler {
  public MessageHandler(ValueExpression te) {
    tuplePattern = te;
  }

  public boolean matches(TupleExpression liveTuple, Map<String,ValueExpression> env) {
    if (tuplePattern.match(liveTuple, env)) {
      return true;
    }
    else {
      return false;
    }
  }

  public List<SendMessageStatement> getSends() {
    return sends;
  }

  public List<CreateActorStatement> getCreations() {
    return createStmts;
  }

  public ValueExpression getPattern() {
    return tuplePattern;
  }

  public String getReplacementBehavior() {
    return replacementBehavior;
  }

  public void setReplacementBehavior(String b) {
    replacementBehavior = b;
  }

  public List<ValueExpression> getReplacementParameters() {
    return replacementParameters;
  }

  private ValueExpression tuplePattern;
  private List<CreateActorStatement> createStmts = new ArrayList<CreateActorStatement>();
  private List<SendMessageStatement> sends = new ArrayList<SendMessageStatement>();
  private String replacementBehavior;
  private List<ValueExpression> replacementParameters = new ArrayList<ValueExpression>();
}
