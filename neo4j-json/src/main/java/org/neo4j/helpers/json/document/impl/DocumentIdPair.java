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

import java.util.HashMap;
import java.util.Map;

import org.neo4j.helpers.json.document.DocumentId;
import org.neo4j.helpers.json.document.utils.IdValueTypeSafe;

/**
 * DocumentId composed by a pair of values, customizable
 * @author Omar Rampado
 *
 */
public class DocumentIdPair implements DocumentId {

	private String key1;
	private String key2;
	private IdValueTypeSafe v1;
	private IdValueTypeSafe v2;
	private Map<String, String> fields;
	
	/**
	 * @param v2
	 * @param v1
	 */
	public DocumentIdPair(String k1, String k2, Object v1, Object v2) {
		super();
		this.key1 = k1;
		this.key2 = k2;
		this.v1 = new IdValueTypeSafe(v1);
		this.v2 = new IdValueTypeSafe(v2);
		this.fields = new HashMap<>(2);
		this.fields.put(key1, this.v1.toString());
		this.fields.put(key2, this.v2.toString());
	}



	/* (non-Javadoc)
	 * @see org.neo4j.helpers.json.document.DocumentId#getFields()
	 */
	@Override
	public Map<String, String> getFields() {
		return this.fields;
	}



	/* (non-Javadoc)
	 * @see org.neo4j.helpers.json.document.DocumentId#toCypherFilter()
	 */
	@Override
	public String toCypherFilter() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.key1);
		sb.append(": ");
		sb.append(this.v1.toCypher());
		
		sb.append(", ");
		
		sb.append(this.key2);
		sb.append(": ");
		sb.append(this.v2.toCypher());
		
		
		return sb.toString();
	}

}
