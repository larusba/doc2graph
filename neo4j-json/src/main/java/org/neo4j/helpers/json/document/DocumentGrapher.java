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

import java.util.Map;

import org.neo4j.graphdb.Node;

/**
 * Logic to transform a Document into a Graph (tree)
 * @author Omar Rampado
 *
 */
public interface DocumentGrapher {

	/**
	 * Create and re-use node and relations
	 * @param key
	 * @param document
	 * @return id of root node
	 */
	@SuppressWarnings("rawtypes")
	Node upsertDocument(String key, Map document);
	
	/**
	 * Remove non shared node and relationships regarding input document key
	 * @param key
	 * @return
	 */
	long deleteDocument(String key);
}
