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

package org.neo4j.helpers.json.document;

import java.util.Map;

/**
 * Set of attributes that defines unique identifier of document into the graph.
 * With DocumentID the nodes can be linked
 * @author Omar Rampado
 *
 */
public interface DocumentId {

	/**
	 * Key is the attribute's name
	 * Value is the attribute's value
	 * that compose the identify
	 * @return
	 */
	Map<String, String> getFields();
	
	/**
	 * Buil a cypher filter condition
	 * ES: filed1: 'aaa', field2: 'bbb'
	 * @return
	 */
	String toCypherFilter();
	
	/**
	 * Equals and hashCode must be implemented
	 */
}
