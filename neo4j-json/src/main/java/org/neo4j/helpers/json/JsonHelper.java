/**
 * Copyright (c) 2016 LARUS Business Automation [http://www.larus-ba.it]
 * <p>
 * This file is part of the "LARUS Integration Framework for Neo4j".
 * <p>
 * The "LARUS Integration Framework for Neo4j" is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neo4j.helpers.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.helpers.json.document.DocumentGrapher;
import org.neo4j.helpers.json.document.context.DocumentGrapherExecutionContext;
import org.neo4j.helpers.json.document.impl.DocumentGrapherRecursive;
import org.neo4j.helpers.json.document.impl.DocumentIdBuilderTypeId;
import org.neo4j.helpers.json.document.impl.DocumentLabelBuilderConstant;
import org.neo4j.helpers.json.document.impl.DocumentRelationBuilderTypeArrayKey;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.PerformsWrites;
import org.neo4j.procedure.Procedure;

/**
 * Procedures to convert Json document to nodes and relationships
 * @author Omar Rampado
 *
 */
public class JsonHelper {

	@Context
	public GraphDatabaseService db;

	@Context
	public Log log;

	protected DocumentGrapherExecutionContext getConfiguration()
	{
		DocumentGrapherExecutionContext context = new DocumentGrapherExecutionContext();
		
		context.setRootNodeKeyProperty("_document_key");
		context.setDocumentIdBuilder(new DocumentIdBuilderTypeId());
		
		DocumentRelationBuilderTypeArrayKey relBuilder = new DocumentRelationBuilderTypeArrayKey();
		relBuilder.setDb(db);
		relBuilder.setLog(log);
		
		context.setDocumentRelationBuilder(relBuilder);
		context.setDocumentLabelBuilder(new DocumentLabelBuilderConstant("DocNode"));
		context.setDb(db);
		context.setLog(log);
		
		return context;
	}
	
	/**
	 * Get implementation of grapher 
	 * @return
	 */
	private DocumentGrapher getDocumentGrapher()
	{
		//must be renew to avoid conflict in concurrent
		return new DocumentGrapherRecursive(getConfiguration()); 
	}

	/**
	 * Insert or update document with key
	 * @param key unique identifier of document
	 * @param json string of json representation of document  
	 * @return ?
	 */
	@Procedure("json.upsert")
	@PerformsWrites
	public Stream<Output> upsertJson(@Name("key") String key, @Name("json") String json) {
		try
		{
			deleteJson(key);
			
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map = new HashMap<String, Object>();
			
			try {
				map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
			} catch (IOException e) {
				//FIXME throw exception
				log.error("Error during parsing JSON: "+json,e);
			}
			
			getDocumentGrapher().upsertDocument(key, map);

		}catch(Exception e)
		{
			log.error("Error with key: "+key,e);
			//FIXME throw exception
		}
		
		return Stream.empty();
	}
	
	/**
	 * Remove document with key from graph
	 * @param key
	 * @return
	 */
	@Procedure("json.delete")
	@PerformsWrites
	public Stream<Output> deleteJson(@Name("key") String key) {
		
		getDocumentGrapher().deleteDocument(key);
		log.info("Delete document with key: "+key);

		return Stream.empty();
	}
}
