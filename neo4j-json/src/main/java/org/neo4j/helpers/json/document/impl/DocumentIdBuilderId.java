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

import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.neo4j.helpers.json.document.DocumentId;
import org.neo4j.helpers.json.document.DocumentIdBuilder;

/**
 * Build {@link DocumentIdId}
 * @author Omar Rampado
 *
 */
public class DocumentIdBuilderId implements DocumentIdBuilder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.neo4j.helpers.json.document.DocumentIdBuilder#buildId(java.util.Map)
	 */
	@Override
	public DocumentId buildId(Map<String,Object> obj) {
		Object idObj = obj.get(DocumentIdId.ID);

		Validate.notNull(idObj, "every object must have "+DocumentIdTypeId.ID+": "+obj);
		
		return new DocumentIdId(idObj);
	}

	@Override
	public void init(Map<String, String> configuration) {
		//nothing to configure
	}

}
