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
package org.neo4j.helpers.json.config.impl;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.helpers.json.config.JsonHelperConfiguration;
import org.neo4j.helpers.json.document.context.DocumentGrapherExecutionContext;
import org.neo4j.helpers.json.document.impl.DocumentIdBuilderTypeId;
import org.neo4j.helpers.json.document.impl.DocumentLabelBuilderConstant;
import org.neo4j.helpers.json.document.impl.DocumentRelationBuilderTypeArrayKey;
import org.neo4j.logging.Log;

/**
 * Default configuration
 * @author Omar Rampado
 * FIXME rename 
 */
public class JsonHelperConfigurationDefault extends JsonHelperConfiguration {

	/* (non-Javadoc)
	 * @see org.neo4j.helpers.json.config.JsonHelperConfiguration#newContext(org.neo4j.graphdb.GraphDatabaseService, org.neo4j.logging.Log)
	 */
	@Override
	public DocumentGrapherExecutionContext buildContext(GraphDatabaseService db, Log log) {
		DocumentGrapherExecutionContext context = new DocumentGrapherExecutionContext();
		
		context.setRootNodeKeyProperty("_document_key");
		context.setDocumentIdBuilder(new DocumentIdBuilderTypeId());
		
		DocumentRelationBuilderTypeArrayKey relBuilder = new DocumentRelationBuilderTypeArrayKey();
		relBuilder.setDb(db);
		relBuilder.setLog(log);
		
		context.setDocumentRelationBuilder(relBuilder);
		DocumentLabelBuilderConstant documentLabelBuilder = new DocumentLabelBuilderConstant();
		documentLabelBuilder.setDefaultLabel("DocNode");
		context.setDocumentLabelBuilder(documentLabelBuilder);
		context.setDb(db);
		context.setLog(log);
		
		return context;
	}

}
