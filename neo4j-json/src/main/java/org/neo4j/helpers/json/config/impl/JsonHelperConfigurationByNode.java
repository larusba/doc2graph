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

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.json.config.JsonHelperConfiguration;
import org.neo4j.helpers.json.document.DocumentIdBuilder;
import org.neo4j.helpers.json.document.DocumentLabelBuilder;
import org.neo4j.helpers.json.document.DocumentRelationBuilder;
import org.neo4j.helpers.json.document.context.DocumentGrapherExecutionContext;
import org.neo4j.logging.Log;

/**
 * Read configuration from Node
 * 
 * @author Omar Rampado FIXME rename
 */
public class JsonHelperConfigurationByNode extends JsonHelperConfiguration {

	private Map<String,String> configuration;
	
	/**
	 * Take properties from node
	 * 
	 * @param confNode
	 */
	public JsonHelperConfigurationByNode(Node confNode) {
		super();
		this.configuration = new HashMap<>();
		confNode.getAllProperties().entrySet().forEach(es -> {
			this.configuration.put(es.getKey(), String.valueOf(es.getValue()));
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neo4j.helpers.json.config.JsonHelperConfiguration#buildContext(org.
	 * neo4j.graphdb.GraphDatabaseService, org.neo4j.logging.Log)
	 */
	@Override
	protected DocumentGrapherExecutionContext buildContext(GraphDatabaseService db, Log log) {
		DocumentGrapherExecutionContext context = new DocumentGrapherExecutionContext();

		String keyProperty = this.configuration.get("root_node_key_property");
		String docIdBuilderString = this.configuration.get("document_id_builder");
		String docRelBuilderString = this.configuration.get("document_relation_builder");
		String docLabelBuilderString = this.configuration.get("document_label_builder");
		String defaultLabel = this.configuration.get("document_default_label");
		String logDiscardEvent = this.configuration.get("log_discard_events");
		
		context.setRootNodeKeyProperty(keyProperty);
		
		DocumentIdBuilder documentIdBuilder = (DocumentIdBuilder) newInstance(docIdBuilderString);
		documentIdBuilder.init(configuration);
		context.setDocumentIdBuilder(documentIdBuilder);
		
		DocumentRelationBuilder documentRelationBuilder = (DocumentRelationBuilder) newInstance(docRelBuilderString);
		documentRelationBuilder.setDb(db);
		documentRelationBuilder.setLog(log);
		context.setDocumentRelationBuilder(documentRelationBuilder);
		
		DocumentLabelBuilder documentLabelBuilder = (DocumentLabelBuilder) newInstance(docLabelBuilderString);
		documentLabelBuilder.setDefaultLabel(defaultLabel);
		context.setDocumentLabelBuilder(documentLabelBuilder);
		context.setDb(db);
		context.setLog(log);
		context.setLogDiscard(Boolean.getBoolean(logDiscardEvent));
		
		return context;
	}

	private Object newInstance(String clazz)
	{
		try {
			Class<?> documentIdBuilderClass = Class.forName(clazz);
			return documentIdBuilderClass.newInstance();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Configuration node error, class not found: "+clazz,e);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Configuration node error, cannot create instance of: "+clazz,e);
		} 
	}
}
