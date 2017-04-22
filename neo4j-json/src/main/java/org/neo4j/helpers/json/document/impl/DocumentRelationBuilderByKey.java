/**
 * Copyright (c) 2017 LARUS Business Automation [http://www.larus-ba.it]
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

import org.apache.commons.lang3.StringUtils;
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
		String documentKey = context.getDocumentKey();
		if(StringUtils.isBlank(documentKey))
		{
			return null;
		}
		
		RelationshipType relationType = RelationshipType.withName( documentKey );
		
		//check if already exists
		Iterable<Relationship> relationships = child.getRelationships(Direction.INCOMING,relationType);
		
		//find only relation between parent and child node
		List<Relationship> rels = StreamSupport.stream(relationships.spliterator(), false)
				.filter(rel -> rel.getStartNode().getId() == parent.getId())
				.collect(Collectors.toList());

		Relationship relationship;
		
		if(rels.isEmpty())
		{
			relationship = parent.createRelationshipTo(child, relationType);
			if(log.isDebugEnabled())
				log.debug("Create new Relation "+relationship);
		}else
		{
			relationship = rels.get(0);
			if(log.isDebugEnabled())
				log.debug("Update Relation "+relationship);
		}
		
		return relationship;
	}

	/* (non-Javadoc)
	 * @see org.neo4j.helpers.json.document.DocumentRelationBuilder#deleteRelations(org.neo4j.helpers.json.document.DocumentRelationContext)
	 */
	@Override
	public Set<Node> deleteRelations(DocumentRelationContext context) {
		Set<Node> orphans = new HashSet<>();

		String documentKey = context.getDocumentKey();
		if(StringUtils.isBlank(documentKey))
		{
			return orphans;
		}
		
		Result result = db.execute("MATCH (p)-[r:"+documentKey+"]->(c) RETURN r");
		result.forEachRemaining((res)->{
			Relationship rel = (Relationship) res.get("r");
			Node parent = rel.getStartNode();
			Node child = rel.getEndNode();
			rel.delete();
			if(log.isDebugEnabled())
			{
				log.debug("Delete relation "+rel);
			}
			updateOrphans(orphans, parent);
			updateOrphans(orphans, child);
		});
		
		return orphans;
	}

	/**
	 * Update orphans collection with node 
	 * @param orphans
	 * @param node
	 * @return true if node is orphan
	 */
	private boolean updateOrphans(Set<Node> orphans, Node node)
	{
		boolean orphan = ! node.getRelationships().iterator().hasNext();
		
		if(orphan)
		{
			orphans.add(node);
		}
		
		return orphan;
	}
}
