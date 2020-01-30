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
package org.apache.ofbiz.jersey.util;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.ofbiz.jersey.response.Error;
import org.apache.ofbiz.jersey.response.Success;

public class ApiUtil {

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static ResponseBuilder successResponse(int statusCode, String reasonPhrase, String message) {
		Success success = new Success(statusCode, reasonPhrase, message);
		return Response.status(statusCode).type(MediaType.APPLICATION_JSON)
				.entity(success);
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static ResponseBuilder errorResponse(int statusCode, String reasonPhrase, String message) {
		Error error = new Error(statusCode, reasonPhrase, message);
		return Response.status(statusCode).type(MediaType.APPLICATION_JSON)
				.entity(error);
	}

}
