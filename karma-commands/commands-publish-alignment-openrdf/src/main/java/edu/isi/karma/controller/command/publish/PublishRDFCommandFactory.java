/*******************************************************************************
 * Copyright 2012 University of Southern California
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This code was developed by the Information Integration Group as part 
 * of the Karma project at the Information Sciences Institute of the 
 * University of Southern California.  For more information, publications, 
 * and related projects, please see: http://www.isi.edu/integration
 ******************************************************************************/
package edu.isi.karma.controller.command.publish;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import edu.isi.karma.controller.command.Command;
import edu.isi.karma.controller.command.CommandFactory;
import edu.isi.karma.rep.Worksheet;
import edu.isi.karma.rep.Workspace;
import edu.isi.karma.rep.metadata.WorksheetProperties.Property;
import edu.isi.karma.webserver.ContextParametersRegistry;
import edu.isi.karma.webserver.ServletContextParameterMap;
import edu.isi.karma.webserver.ServletContextParameterMap.ContextParameter;


class Helper {
	static Workspace workspace;
	
}
public class PublishRDFCommandFactory extends CommandFactory {
	private enum Arguments {
		worksheetId, addInverseProperties, rdfPrefix, rdfNamespace, saveToStore, 
		hostName,dbName,userName,password,modelName, 
		tripleStoreUrl, graphUri, replaceContext, generateBloomFilters, 
		selectionName
	}

	@Override
	public Command createCommand(HttpServletRequest request,
			Workspace workspace) {
		System.out.println("create command to the rdf");
		if(request.getParameter("graphUri") != null) {
			System.out.println("graphUri is not nullllllllllll at createCommand");
		}
		try {

			String[] requestParams = {
				request.getParameter(Arguments.worksheetId.name()), // [0] worksheetId
				request.getParameter(Arguments.addInverseProperties.name()), // [1] addInverseProperties
				request.getParameter(Arguments.selectionName.name()), // [2] selectionName
				request.getParameter(Arguments.saveToStore.name()), // [3] saveToStore
				request.getParameter(Arguments.hostName.name()), // [4] hostName
				request.getParameter(Arguments.dbName.name()), // [5] dbName
				request.getParameter(Arguments.userName.name()), // [6] userName
				request.getParameter(Arguments.password.name()), // [7] password
				request.getParameter(Arguments.modelName.name()), // [8] modelName
				request.getParameter(Arguments.tripleStoreUrl.name()), // [9] tripleStoreUrl
				request.getParameter(Arguments.graphUri.name()), // [10] graphUri
				request.getParameter(Arguments.replaceContext.name()), // [11] replaceContext
				request.getParameter(Arguments.generateBloomFilters.name()) // [12] generateBloomFilters
			};
			
			// Extract workspace parameters into an array
			String[] workspaceParams = {
				workspace.getContextId(), // Assuming getContextId() gives the necessary context identifier
				workspace.getWorksheet(request.getParameter(Arguments.worksheetId.name())).getMetadataContainer().getWorksheetProperties().getPropertyValue(Property.prefix), // rdfPrefix
				workspace.getWorksheet(request.getParameter(Arguments.worksheetId.name())).getMetadataContainer().getWorksheetProperties().getPropertyValue(Property.baseURI), // rdfNamespace
				getNewId(workspace) // Assuming this method can be called here to generate an ID
				// Add other necessary workspace details if needed
			};

			if (Helper.workspace == null) {
				Helper.workspace = workspace;
			}

			RdfQueues.addRdfQueue(requestParams, workspaceParams);

		
		final ServletContextParameterMap contextParameters = ContextParametersRegistry.getInstance().getContextParameters(workspace.getContextId());
		String worksheetId = request.getParameter(Arguments.worksheetId
				.name());
		String addInverseProperties = request.getParameter(Arguments.addInverseProperties
				.name());
		Worksheet worksheet = workspace.getWorksheet(worksheetId);
		String rdfPrefix =  worksheet.getMetadataContainer().getWorksheetProperties().getPropertyValue(Property.prefix);
		String rdfNamespace = worksheet.getMetadataContainer().getWorksheetProperties().getPropertyValue(Property.baseURI);
		String selectionName = request.getParameter(Arguments.selectionName.name());

		PublishRDFCommand comm = new PublishRDFCommand(getNewId(workspace), 
				Command.NEW_MODEL,
				worksheetId,
				contextParameters
				.getParameterValue(ContextParameter.PUBLIC_RDF_ADDRESS),
				rdfPrefix, rdfNamespace, addInverseProperties,
				request.getParameter(Arguments.saveToStore.name()),
				request.getParameter(Arguments.hostName.name()),
				request.getParameter(Arguments.dbName.name()),
				request.getParameter(Arguments.userName.name()),
				request.getParameter(Arguments.password.name()),
				request.getParameter(Arguments.modelName.name()),
				request.getParameter(Arguments.tripleStoreUrl.name()),
				request.getParameter(Arguments.graphUri.name()),
				Boolean.parseBoolean(request.getParameter(Arguments.replaceContext.name())), 
				Boolean.parseBoolean(request.getParameter(Arguments.generateBloomFilters.name())), 
				selectionName
				);
		System.out.println("doneeeee");

		return comm;
			} catch (Exception e) {
				System.out.println("Error in creating command");
				e.printStackTrace();
				return null;
		}
	

	}
	public void createCommand(String[] request, String[] workSheet) {
		System.out.println("create command to the rdf");
		
		try {
		final ServletContextParameterMap contextParameters = ContextParametersRegistry.getInstance().getContextParameters(workSheet[0]);
		String worksheetId = request[0];
		String addInverseProperties = request[1];
		String rdfPrefix =  workSheet[1];
		String rdfNamespace = workSheet[2];
		String selectionName = request[2];
		if (Helper.workspace == null) {
			System.out.println("workspace is null");
		}
		PublishRDFCommand comm =
		new PublishRDFCommand(getNewId(Helper.workspace),
				Command.NEW_MODEL,
				worksheetId,
				contextParameters
				.getParameterValue(ContextParameter.PUBLIC_RDF_ADDRESS),
				rdfPrefix, rdfNamespace, addInverseProperties,
				request[3],
				request[4],
				request[5],
				request[6],
				request[7],
				request[8],
				request[9],
				request[10],
				Boolean.parseBoolean(request[11]), 
				Boolean.parseBoolean(request[12]), 
				selectionName
				);

			comm.doIt(Helper.workspace);
		System.out.println("doneeeee");
		}
		catch (Exception e) {
			System.out.println("Error in creating command");
			e.printStackTrace();
		}
	}
		
	@Override
	public Class<? extends Command> getCorrespondingCommand()
	{
		return PublishRDFCommand.class;
	}
}
