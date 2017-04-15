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
 * Build relationship with name parent.type_child.type with docKeys array
 * ES: artist_origin
 * @author Omar Rampado
 *
 */
public class DocumentRelationBuilderTypeArrayKey implements DocumentRelationBuilder {
	
	private static final String DOC_KEYS = "docKeys";
	
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

	/**
	 * Compose the type of the relationship
	 * @param parent
	 * @param child
	 * @param context
	 * @return
	 */
	protected String buildRelationName(Node parent, Node child, DocumentRelationContext context){
		String parentType = (String) parent.getProperty("type");
		String childType = (String) child.getProperty("type");
		return parentType+"_"+childType;
	}
	
	/* (non-Javadoc)
	 * @see org.neo4j.helpers.json.document.DocumentRelationBuilder#buildRelation(org.neo4j.graphdb.Node, org.neo4j.graphdb.Node, org.neo4j.helpers.json.document.DocumentRelationContext)
	 */
	@Override
	public Relationship buildRelation(Node parent, Node child, DocumentRelationContext context) {
		String relationName = buildRelationName(parent, child, context);
		
		RelationshipType type = RelationshipType.withName( relationName );
		
		//check if already exists
		Iterable<Relationship> relationships = child.getRelationships(Direction.INCOMING,type);
		
		Relationship relationship;

		//find only relation between parent and child node
		List<Relationship> rels = StreamSupport.stream(relationships.spliterator(), false)
				.filter(rel -> rel.getStartNode().getId() == parent.getId())
				.collect(Collectors.toList());

		if(rels.isEmpty())
		{
			relationship = parent.createRelationshipTo(child, type);
			if(log.isDebugEnabled())
				log.debug("Create new Relation "+relationship);
		}else
		{
			relationship = rels.get(0);
			if(log.isDebugEnabled())
				log.debug("Update Relation "+relationship);
		}

		//manage array of keys
		
		String[] keys = new String[0];
		
		//create property if doesn't exists (new relation)
		if(relationship.getAllProperties().containsKey(DOC_KEYS))
		{
			keys = (String[]) relationship.getProperty(DOC_KEYS);
		}
		
		//set document key into property
		String documentKey = context.getDocumentKey();
		if(! ArrayUtils.contains(keys, documentKey))
		{
			keys = ArrayUtils.add(keys, documentKey);			
			relationship.setProperty(DOC_KEYS, keys);
		}
		
		
		return relationship;
	}

	/* (non-Javadoc)
	 * @see org.neo4j.helpers.json.document.DocumentRelationBuilder#deleteRelations(org.neo4j.helpers.json.document.DocumentRelationContext)
	 */
	@Override
	public Set<Node> deleteRelations(DocumentRelationContext context) {
		Set<Node> orphans = new HashSet<>();
		
		Result result = db.execute("MATCH (p)-[r]->(c) WHERE \""+context.getDocumentKey()+"\" IN r."+DOC_KEYS+" RETURN r");
		result.forEachRemaining((res)->{
			Relationship rel = (Relationship) res.get("r");
			String[] keys = (String[]) rel.getProperty(DOC_KEYS);
			if(keys.length == 1)
			{
				Node parent = rel.getStartNode();
				Node child = rel.getEndNode();
				rel.delete();
				if(log.isDebugEnabled())
				{
					log.debug("Delete relation "+rel);
				}
				
				updateOrphans(orphans, parent);
				updateOrphans(orphans, child);
				
			}else
			{
				//if other document pass through this relationship
				keys = ArrayUtils.removeElement(keys, context.getDocumentKey());
				rel.setProperty(DOC_KEYS, keys);
			}
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
