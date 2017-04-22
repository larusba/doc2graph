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

package org.neo4j.helpers.json.document;

import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.helpers.json.document.context.DocumentRelationContext;
import org.neo4j.logging.Log;

/**
 * Build relationships between parent and child nodes
 * @author Omar Rampado
 *
 */
public interface DocumentRelationBuilder {

	/**
	 * Create or update relationship between parent and child node
	 * @param parent
	 * @param child
	 * @param context
	 * @return
	 */
	Relationship buildRelation(Node parent, Node child, DocumentRelationContext context);
	
	/**
	 * Remove all relationships regarding document context
	 * @param context
	 * @return the 'orphan' nodes
	 */
	Set<Node> deleteRelations(DocumentRelationContext context);

	void setDb(GraphDatabaseService db);

	void setLog(Log log);
}
