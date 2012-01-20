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

import org.scientopia.goodmath.cruise.ast.TupleExpression;

/**
 * The interface for live actors in a running program.
 */
public interface IActor {

  /**
   * Get the dynamic name of the actor.
   * @return the actor name
   */
  public abstract String getName();

  /**
   * Add a message to the actor's queue.
   * @param msg the message to add. Must be a fully concrete tuple - no variable references.
   * @throws CruiseRuntimeException if the tuple is not concrete
   */
  public abstract void enqueueMessage(TupleExpression msg)
      throws CruiseRuntimeException;

  /**
   * Take the first message from the actors queue, and process it.
   * @throws CruiseRuntimeException if the actor does not have a matching
   *     message handler in its current behavior, or if the message handling action
   *     throws any exception.
   */
  public abstract void processMessage() throws CruiseRuntimeException;

  /**
   * Check to see if the actor has any messages queued to be processed.
   * @return true if there are messages
   */
  public abstract boolean poll();

}