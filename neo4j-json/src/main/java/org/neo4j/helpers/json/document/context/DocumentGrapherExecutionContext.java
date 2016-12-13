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

package org.neo4j.helpers.json.document.context;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.helpers.json.document.DocumentIdBuilder;
import org.neo4j.helpers.json.document.DocumentLabelBuilder;
import org.neo4j.helpers.json.document.DocumentRelationBuilder;
import org.neo4j.logging.Log;

/**
 * Configuration of DocumentGrapher dependency
 * 
 * @author Omar Rampado
 *
 */
public class DocumentGrapherExecutionContext {

	private GraphDatabaseService db;
	private Log log;
	private DocumentIdBuilder documentIdBuilder;
	private DocumentRelationBuilder documentRelationBuilder;
	private DocumentLabelBuilder documentLabelBuilder;
	private String rootNodeKeyProperty;

	/**
	 * @return the rootNodeKeyProperty
	 */
	public String getRootNodeKeyProperty() {
		return rootNodeKeyProperty;
	}

	/**
	 * @param rootNodeKeyProperty
	 *            the rootNodeKeyProperty to set
	 */
	public void setRootNodeKeyProperty(String rootNodeKeyProperty) {
		this.rootNodeKeyProperty = rootNodeKeyProperty;
	}

	/**
	 * @return the documentLabelBuilder
	 */
	public DocumentLabelBuilder getDocumentLabelBuilder() {
		return documentLabelBuilder;
	}

	/**
	 * @param documentLabelBuilder
	 *            the documentLabelBuilder to set
	 */
	public void setDocumentLabelBuilder(DocumentLabelBuilder documentLabelBuilder) {
		this.documentLabelBuilder = documentLabelBuilder;
	}

	/**
	 * @return the db
	 */
	public GraphDatabaseService getDb() {
		return db;
	}

	/**
	 * @param db
	 *            the db to set
	 */
	public void setDb(GraphDatabaseService db) {
		this.db = db;
	}

	/**
	 * @return the log
	 */
	public Log getLog() {
		return log;
	}

	/**
	 * @param log
	 *            the log to set
	 */
	public void setLog(Log log) {
		this.log = log;
	}

	/**
	 * @return the documentIdBuilder
	 */
	public DocumentIdBuilder getDocumentIdBuilder() {
		return documentIdBuilder;
	}

	/**
	 * @param documentIdBuilder
	 *            the documentIdBuilder to set
	 */
	public void setDocumentIdBuilder(DocumentIdBuilder documentIdBuilder) {
		this.documentIdBuilder = documentIdBuilder;
	}

	/**
	 * @return the documentRelationBuilder
	 */
	public DocumentRelationBuilder getDocumentRelationBuilder() {
		return documentRelationBuilder;
	}

	/**
	 * @param documentRelationBuilder
	 *            the documentRelationBuilder to set
	 */
	public void setDocumentRelationBuilder(DocumentRelationBuilder documentRelationBuilder) {
		this.documentRelationBuilder = documentRelationBuilder;
	}

}
