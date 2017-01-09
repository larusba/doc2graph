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

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.helpers.json.document.DocumentId;

public class DocumentIdBuilderIdTest {

	DocumentIdBuilderId builder;
	
	@Before
	public void setUp() throws Exception {
		this.builder = new DocumentIdBuilderId();
	}

	@Test
	public void testBuildId() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", 12);
		map.put("type", "artist");
		DocumentId buildId = builder.buildId(map);
		Assert.assertEquals("id: 12", buildId.toCypherFilter());
	}

	
	@Test(expected=NullPointerException.class)
	public void testBuildIdNoId() {
		Map<String, Object> map = new HashMap<>();
		map.put("type", "artist");
		builder.buildId(map);
	}
}
