/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.apache.ofbiz.jersey.core;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;


public enum HttpResponseStatus implements Response.StatusType {

	UNPROCESSABLE_ENTITY(422, "Unprocessable Entity");

	private HttpResponseStatus(int statusCode, String reasonPhrase) {
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	private int statusCode;
	private String reasonPhrase;

	@Override
	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public Family getFamily() {
		return Response.Status.Family.CLIENT_ERROR;
	}

	@Override
	public String getReasonPhrase() {
		return reasonPhrase;
	}

}
