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
package org.apache.ofbiz.jersey.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.Provider;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.model.ModelField;
import org.apache.ofbiz.entity.model.ModelReader;
import org.apache.ofbiz.jersey.annotation.Secured;
import org.apache.ofbiz.jersey.core.HttpResponseStatus;
import org.apache.ofbiz.jersey.response.Error;
import org.apache.ofbiz.jersey.response.Success;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.service.ServiceUtil;

@Path("/entities")
@Provider
@Secured
public class EntityResource {

	public static final String MODULE = EntityResource.class.getName();

	@Context
	private HttpServletRequest httpRequest;

	@Context
	private ServletContext servletContext;
	
	@Path("/import")
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response entityImport(String importXml) throws IOException {
		ResponseBuilder builder = null;
		LocalDispatcher dispatcher = (LocalDispatcher) servletContext.getAttribute("dispatcher");
		String fullText = null;
		if (importXml != null && importXml.startsWith("<entity-engine-xml>") && importXml.endsWith("</entity-engine-xml>")) {
			fullText = importXml;
		} else {
			fullText = "<entity-engine-xml>" + importXml + "</entity-engine-xml>";
		}
		GenericValue userLogin = (GenericValue)httpRequest.getAttribute("userLogin");
		Map<String, Object> result = null;
		try {
			result = dispatcher.runSync("entityImport", UtilMisc.toMap("fulltext", fullText, "userLogin", userLogin));
		} catch (GenericServiceException e) {
			Debug.logError(e, "Exception thrown while running entityImport service: ", MODULE);
			String errMsg = UtilProperties.getMessage("JerseyUiLabels", "api.error.import_entity", httpRequest.getLocale());
			throw new RuntimeException(errMsg);
		}
		
		if (ServiceUtil.isError(result)) {
			Error error = new Error(HttpResponseStatus.UNPROCESSABLE_ENTITY.getStatusCode(), HttpResponseStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), (String)result.get(ModelService.ERROR_MESSAGE));
			builder = Response.status(HttpResponseStatus.UNPROCESSABLE_ENTITY).type(MediaType.APPLICATION_JSON).entity(error);
		} else {
			String msg = UtilProperties.getMessage("JerseyUiLabels", "api.success.import_entity", httpRequest.getLocale());
			Success success = new Success(Response.Status.OK.getStatusCode(), Response.Status.OK.getReasonPhrase(), msg);
			builder = Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(success);
		}
		return builder.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listEntities() throws IOException, GenericEntityException {
		ResponseBuilder builder = null;
		Delegator delegator = (Delegator) servletContext.getAttribute("delegator");
		ModelReader reader = delegator.getModelReader();
		TreeSet<String> entities = new TreeSet<String>(reader.getEntityNames());
		builder = Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(entities);
		return builder.build();
	}
	
	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEntity(@PathParam(value = "name") String entityName) throws IOException, GenericEntityException {
		ResponseBuilder builder = null;
		List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		Delegator delegator = (Delegator) servletContext.getAttribute("delegator");
		ModelEntity entity = delegator.getModelEntity(entityName);
		List<String> fieldNames = entity.getAllFieldNames();
		fieldNames.forEach((fieldName) -> {
			ModelField field = entity.getField(fieldName);
			String fType = field.getType();
			boolean isPk = field.getIsPk();
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("name", fieldName);
			map.put("type", fType);
			map.put("is_pk", isPk);
			response.add(map);
		});
		builder = Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(response);
		return builder.build();
	}

}
