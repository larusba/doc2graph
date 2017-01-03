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

package org.neo4j.helpers.json.document.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.ArrayUtils;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.helpers.json.document.DocumentRelationBuilder;
import org.neo4j.helpers.json.document.context.DocumentRelationContext;
import org.neo4j.logging.Log;

/**
 * Build relationship using document key as label
 * FIXME implement
 * @author Omar Rampado
 *
 */
public class DocumentRelationBuilderByKey implements DocumentRelationBuilder {
	
	private GraphDatabaseService db;
	
	private Log log;
	
	/**
	 * @param db the db to set
	 */
	public void setDb(GraphDatabaseService db) {
		this.db = db;
	}
	
	/**
	 * @param log the log to set
	 */
	public void setLog(Log log) {
		this.log = log;
	}

	/* (non-Javadoc)
	 * @see org.neo4j.helpers.json.document.DocumentRelationBuilder#buildRelation(org.neo4j.graphdb.Node, org.neo4j.graphdb.Node, org.neo4j.helpers.json.document.DocumentRelationContext)
	 */
	@Override
	public Relationship buildRelation(Node parent, Node child, DocumentRelationContext context) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.neo4j.helpers.json.document.DocumentRelationBuilder#deleteRelations(org.neo4j.helpers.json.document.DocumentRelationContext)
	 */
	@Override
	public Set<Node> deleteRelations(DocumentRelationContext context) {
		Set<Node> orphans = new HashSet<>();

		
		return orphans;
	}

}
