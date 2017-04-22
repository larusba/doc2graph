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
 * DocumentId composed of "type" and "id"
 * @author Omar Rampado
 *
 */
public class DocumentIdTypeId implements DocumentId {

	public static final String ID = "id";
	public static final String TYPE = "type";
	
	
	private IdValueTypeSafe id;
	private IdValueTypeSafe type;
	private Map<String, String> fields;
	
	/**
	 * @param type
	 * @param id
	 */
	public DocumentIdTypeId(Object type, Object id) {
		super();
		this.type = new IdValueTypeSafe(type);
		this.id = new IdValueTypeSafe(id);
		this.fields = new HashMap<>(2);
		this.fields.put(TYPE, this.type.toString());
		this.fields.put(ID, this.id.toString());
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
		sb.append(ID);
		sb.append(": ");
		sb.append(this.id.toCypher());
		
		sb.append(", ");
		
		sb.append(TYPE);
		sb.append(": ");
		sb.append(this.type.toCypher());
		
		
		return sb.toString();
	}

}
