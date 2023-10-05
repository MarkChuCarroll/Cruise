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

import org.goodmath.cruise.interp.CruiseRuntimeException;

@SuppressWarnings("serial")
public class CruiseTypeError extends CruiseRuntimeException {

  public CruiseTypeError() {
    super();
    // TODO Auto-generated constructor stub
  }

  public CruiseTypeError(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  public CruiseTypeError(String message) {
    super(message);
    // TODO Auto-generated constructor stub
  }

  public CruiseTypeError(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }

}
