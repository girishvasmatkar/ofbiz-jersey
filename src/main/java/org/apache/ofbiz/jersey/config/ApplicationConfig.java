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
package org.apache.ofbiz.jersey.config;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ofbiz.base.util.Debug;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;


public class ApplicationConfig extends ResourceConfig {
	public ApplicationConfig() {
		packages("org.apache.ofbiz.jersey.resource");
		packages("org.apache.ofbiz.jersey.providers");
		packages("com.fasterxml.jackson.jaxrs");
		packages("io.swagger.v3.jaxrs2.integration.resources");
		register(MultiPartFeature.class);
		if (Debug.verboseOn()) {
			register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), Level.INFO,
					LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));
		}

	}

}
