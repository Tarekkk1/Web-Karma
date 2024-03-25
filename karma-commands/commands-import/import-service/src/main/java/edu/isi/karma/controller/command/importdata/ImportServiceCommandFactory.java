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
package edu.isi.karma.controller.command.importdata;

import javax.servlet.http.HttpServletRequest;

import edu.isi.karma.controller.command.Command;
import edu.isi.karma.controller.command.CommandFactory;
import edu.isi.karma.controller.command.selection.SuperSelection;
import edu.isi.karma.controller.update.UpdateContainer;
import edu.isi.karma.controller.update.WorksheetUpdateFactory;
import edu.isi.karma.imp.Import;
import edu.isi.karma.imp.json.JsonImport;
import edu.isi.karma.rep.HTable;
import edu.isi.karma.rep.Worksheet;
import edu.isi.karma.rep.Workspace;
import edu.isi.karma.util.HTTPUtil;
import edu.isi.karma.controller.update.WorksheetDataUpdate;


class Helper {
	static Workspace workspace;
}
public class ImportServiceCommandFactory extends CommandFactory {
	private enum Arguments {
		serviceUrl, worksheetName, includeInputAttributes, encoding
	}

	@Override
	public Command createCommand(HttpServletRequest request,
			Workspace workspace) {
				if (Helper.workspace == null) {
			Helper.workspace = workspace;
		}
		return new ImportServiceCommand(getNewId(workspace),
				Command.NEW_MODEL,
				request.getParameter(Arguments.serviceUrl.name()),
				request.getParameter(Arguments.worksheetName.name()),
				Boolean.parseBoolean(request.getParameter(Arguments.includeInputAttributes.name())),
				request.getParameter(Arguments.encoding.name())
				);
	}

	public void updateWorkSheet(String workSheetId, String serviceUrl, String worksheetName, String encoding) throws Exception{
		
		ImportServiceCommand command = new ImportServiceCommand(getNewId(Helper.workspace),
				Command.NEW_MODEL,
				serviceUrl,
				worksheetName,
				false,
				encoding
				);	
				command.serviceHelper(Helper.workspace, workSheetId, serviceUrl, worksheetName, false, encoding);

	}


	@Override
	public Class<? extends Command> getCorrespondingCommand()
	{
		return ImportServiceCommand.class;
	}
}
