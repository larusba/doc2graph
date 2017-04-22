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

import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.neo4j.helpers.json.document.DocumentId;
import org.neo4j.helpers.json.document.DocumentIdBuilder;

/**
 * Build {@link DocumentIdPair}
 * @author Omar Rampado
 *
 */
public class DocumentIdBuilderPair implements DocumentIdBuilder {

	private String key1;
	private String key2;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neo4j.helpers.json.document.DocumentIdBuilder#buildId(java.util.Map)
	 */
	@Override
	public DocumentId buildId(Map<String,Object> obj) {
		Object obj1 = obj.get(this.key1);
		Object obj2 = obj.get(this.key2);

		Validate.notNull(obj1, "every object must have "+this.key1+": "+obj);
		Validate.notNull(obj2, "every object must have "+this.key2+": "+obj);
		
		return new DocumentIdPair(key1, key2, obj1, obj2);
	}

	@Override
	public void init(Map<String, String> configuration) {
		this.key1 = configuration.get("builder_id_pair_key1");
		this.key2 = configuration.get("builder_id_pair_key2");
	}

}
