/**
 * *****************************************************************************
 * Copyright 2012 University of Southern California
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * This code was developed by the Information Integration Group as part of the
 * Karma project at the Information Sciences Institute of the University of
 * Southern California. For more information, publications, and related
 * projects, please see: http://www.isi.edu/integration
 *****************************************************************************
 */
package edu.isi.karma.controller.command.importdata;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.apache.hadoop.yarn.util.SystemClock;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HttpHeaders;
import edu.isi.karma.controller.command.Command;

import edu.isi.karma.controller.command.CommandException;
import edu.isi.karma.controller.command.CommandType;
import edu.isi.karma.controller.command.selection.SuperSelection;
import edu.isi.karma.controller.command.selection.SuperSelectionManager;
import edu.isi.karma.controller.update.ErrorUpdate;
import edu.isi.karma.controller.update.HistoryUpdate;
import edu.isi.karma.controller.update.ImportServiceCommandPreferencesUpdate;
import edu.isi.karma.controller.update.UpdateContainer;
import edu.isi.karma.controller.update.WorksheetListUpdate;
import edu.isi.karma.controller.update.WorksheetUpdateFactory;
import edu.isi.karma.imp.Import;
import edu.isi.karma.imp.csv.CSVFileImport;
import edu.isi.karma.imp.csv.CSVImport;
import edu.isi.karma.imp.json.JsonImport;
import edu.isi.karma.rep.Worksheet;
import edu.isi.karma.rep.Workspace;
import edu.isi.karma.rep.sources.InvocationManager;
import edu.isi.karma.util.HTTPUtil;
import edu.isi.karma.view.VWorkspace;
import edu.isi.karma.view.VWorkspaceRegistry;
import edu.isi.karma.webserver.ExecutionController;
import edu.isi.karma.webserver.KarmaException;
import edu.isi.karma.webserver.WorkspaceRegistry;
import edu.isi.karma.controller.command.importdata.ServiceQueue;
import edu.isi.karma.controller.command.importdata.ServiceQueues;


public class ImportServiceCommand extends ImportCommand {

    private static Logger logger = LoggerFactory.getLogger(ImportServiceCommand.class);
    private String serviceUrl;
    private String worksheetName;
    private boolean includeInputAttributes;
    private String encoding;

	protected ImportServiceCommand(String id, String model, String ServiceUrl, String worksheetName,
            boolean includeInputAttributes, String encoding) {
        super(id, model);
        this.serviceUrl = ServiceUrl;
        this.worksheetName = worksheetName;
        this.includeInputAttributes = includeInputAttributes;
        this.encoding = encoding;
    }

    @Override
    public String getCommandName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getTitle() {
        return "Import Service";
    }

    @Override
    public String getDescription() {
        if (serviceUrl.length() > 50) {
            return serviceUrl.substring(0, 50);
        } else {
            return serviceUrl;
        }
    }

    @Override
    public UpdateContainer doIt(Workspace workspace) throws CommandException {
      
        UpdateContainer c = new UpdateContainer();
        try {
        	
            String filePath = downloadFile(serviceUrl, "/home/tarek/GUC/Bash/realtime-karma/karma-web/src/main/webapp/publish/");
            System.out.println("File Path: " + filePath);
         
            String fileExtension = getFileExtension(filePath); // Use the utility method to get file extension
    
        
            Import imp = null;
            switch (fileExtension) {
                case "json":
                    JSONObject json = readJsonFromFile(filePath);
                    imp = new JsonImport(json, worksheetName, workspace, encoding, -1);
                    break;
                case "csv":
                    // Assuming CSVFileImport constructor and other details are correct
                    imp = new CSVFileImport(1, 2, ',', '"', encoding, 100000000, new File(filePath), workspace, null);
                    break;
                case "xml":
                    // XML file handling - assuming you have a similar constructor for XMLImport
                    // Document xml = readXMLFile(filePath);
                    // imp = new XMLImport(xml, worksheetName, workspace, encoding);
                    break;
             
            }
            Worksheet wsht = imp.generateWorksheet();
            c.add(new ImportServiceCommandPreferencesUpdate(serviceUrl, worksheetName));

            c.add(new WorksheetListUpdate());
            c.append(WorksheetUpdateFactory.createWorksheetHierarchicalAndCleaningResultsUpdates(wsht.getId(), SuperSelectionManager.DEFAULT_SELECTION, workspace.getContextId()));
            ServiceQueues.addServiceQueue(wsht.getId(), serviceUrl, encoding, worksheetName);
           
            return c;
        } catch (Exception e) {
            logger.error("Error occured while creating worksheet from web-service: " + serviceUrl);
            return new UpdateContainer(new ErrorUpdate("Error creating worksheet from web-service"));
        }
    
    }

    public void serviceHelper(Workspace workspace, String workSheetId, String serviceUrl, String worksheetName, boolean includeInputAttributes, String encoding) throws ClientProtocolException, IOException, CommandException, JSONException, ClassNotFoundException, KarmaException {
        Worksheet worksheet = workspace.getWorksheet(workSheetId);
        UpdateContainer c = new UpdateContainer();
        try {
            String filePath = downloadFile(serviceUrl, "/home/tarek/GUC/Bash/realtime-karma/karma-web/src/main/webapp/publish/");
            String fileExtension = getFileExtension(filePath); // Use the utility method to get file extension
    
            Import imp = null;
            switch (fileExtension) {
                case "json":
                    JSONObject json = readJsonFromFile(filePath);
                    imp = new JsonImport(json, worksheetName, workspace, encoding, -1);
                    break;
                case "csv":
                imp = worksheet.getImportMethod();
                if (imp instanceof CSVImport) {
                    System.out.println("The import is CSVImport");
                    ((CSVImport) imp).setFile(new File(filePath));
                    worksheet.getImportMethod().generateWorksheetFormKnowenWorkSheet(worksheet);
               
                }
                      break;
                case "xml":
                    break;
                default:
                    logger.error("Unsupported file extension: " + fileExtension);
                    return;
            }
    
            if (!(imp instanceof CSVImport))
                imp.generateWorksheetFormKnowenWorkSheet(worksheet);
 
        } catch (Exception e) {
            logger.error("Error occurred while creating worksheet from web-service: " + serviceUrl, e);
        }
    }
    
    private String getFileExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filePath.substring(dotIndex + 1).toLowerCase();
    }
    


    public static JSONObject readJsonFromFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONObject(content);
    }



   
    public String downloadFile(String serviceUrl, String outputDirectory) throws IOException, InterruptedException {
       try {
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl))
                .GET()
                .build();

        // Create a temporary file
        Path tempFile = Files.createTempFile(null, null);

        // Download the content to the temporary file
        HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(tempFile));

        // Check Content-Type header from response to determine the file extension
        String contentType = response.headers().firstValue("Content-Type").orElse("");
        String extension = "";
        if (contentType.contains("application/json")) {
            extension = ".json";
        } else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
            extension = ".xml";
        } else if (contentType.contains("text/csv")) {
            extension = ".csv";
        }

        // Move and rename the temporary file based on the determined file extension
        Path outputPath = Paths.get(outputDirectory, serviceUrl.substring(serviceUrl.lastIndexOf("/") + 1) + extension);
        Files.move(tempFile, outputPath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("File downloaded and saved as: " + outputPath);
        return outputPath.toString();
        
       } catch (Exception e) {
           System.out.println("Error in downloadFile: " + e.getMessage());
           return null;
       }
    }

   
    @Override
    protected Import createImport(Workspace workspace) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
