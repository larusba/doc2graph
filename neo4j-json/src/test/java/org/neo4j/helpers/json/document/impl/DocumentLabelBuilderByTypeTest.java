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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Label;


/**
 * @author Omar Rampado
 *
 */
public class DocumentLabelBuilderByTypeTest {

	DocumentLabelBuilderByType builder;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		builder = new DocumentLabelBuilderByType();
	}

	@Test
	public void testBuildLabelUnderscore() {
		builder.setDefaultLabel("NODE");
		Map<String, Object> map = new HashMap<>();
		map.put("type", "big_artist");
		Label label = builder.buildLabel(map);
		Assert.assertEquals("BigArtist", label.name());
	}

	@Test
	public void testBuildLabelSpace() {
		builder.setDefaultLabel("NODE");
		Map<String, Object> map = new HashMap<>();
		map.put("type", "big artist");
		Label label = builder.buildLabel(map);
		Assert.assertEquals("BigArtist", label.name());
	}
	
	@Test
	public void testBuildLabelDefault() {
		builder.setDefaultLabel("NODE");
		Map<String, Object> map = new HashMap<>();
		map.put("id", "123");
		Label label = builder.buildLabel(map);
		Assert.assertEquals("NODE", label.name());
	}
}
