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
package org.neo4j.helpers.json.config;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.MockType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.json.document.DocumentRelationBuilder;
import org.neo4j.helpers.json.document.context.DocumentGrapherExecutionContext;
import org.neo4j.helpers.json.document.impl.DocumentIdBuilderId;
import org.neo4j.helpers.json.document.impl.DocumentLabelBuilderByType;
import org.neo4j.helpers.json.document.impl.DocumentRelationBuilderByKey;
import org.neo4j.logging.Log;

/**
 * @author Omar Rampado
 *
 */
@RunWith(EasyMockRunner.class)
public class JsonHelperConfigurationTest {

	@Mock(type=MockType.NICE)
	GraphDatabaseService db;

	@Mock(type=MockType.NICE)
	Log log;
	
	@Mock(type=MockType.NICE)
	Node confNode;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		JsonHelperConfiguration.reset();
		expect(confNode.getProperty("configuration")).andStubReturn("byNode");
		expect(confNode.getProperty("root_node_key_property")).andStubReturn("_root_");
		expect(confNode.getProperty("document_id_builder")).andStubReturn("org.neo4j.helpers.json.document.impl.DocumentIdBuilderId");
		expect(confNode.getProperty("document_relation_builder")).andStubReturn("org.neo4j.helpers.json.document.impl.DocumentRelationBuilderByKey");
		expect(confNode.getProperty("document_label_builder")).andStubReturn("org.neo4j.helpers.json.document.impl.DocumentLabelBuilderByType");		
		
		expect(db.findNode(Label.label("JSON_CONFIG"), "configuration", "byNode")).andReturn(confNode);
		replay(db,log, confNode);
	}
	

	/**
	 * Test method for {@link org.neo4j.helpers.json.config.JsonHelperConfiguration#buildContext(org.neo4j.graphdb.GraphDatabaseService, org.neo4j.logging.Log)}.
	 */
	@Test
	public void testNewContext() {

		DocumentGrapherExecutionContext ctx1 = JsonHelperConfiguration.newContext(db, log);
		Assert.assertNotNull(ctx1);
		DocumentGrapherExecutionContext ctx2 = JsonHelperConfiguration.newContext(db, log);
		Assert.assertNotNull(ctx2);
		
		Assert.assertFalse("Configurazion must be renew",ctx1 == ctx2);
	}

	@Test
	public void testNewDocumentRelationBuilder() {
		DocumentRelationBuilder ctx1 = JsonHelperConfiguration.newContext(db, log).getDocumentRelationBuilder();
		Assert.assertNotNull(ctx1);
		DocumentRelationBuilder ctx2 = JsonHelperConfiguration.newContext(db, log).getDocumentRelationBuilder();
		Assert.assertNotNull(ctx2);
		
		Assert.assertFalse("DocumentRelationBuilder must be renew",ctx1 == ctx2);
	}
	
	@Test
	public void testConfiguration()
	{
		DocumentGrapherExecutionContext ctx = JsonHelperConfiguration.newContext(db, log);
		Assert.assertTrue(ctx.getDb() == db);
		Assert.assertTrue(ctx.getLog() == log);
		Assert.assertEquals("_root_",ctx.getRootNodeKeyProperty());
		Assert.assertTrue(ctx.getDocumentIdBuilder() instanceof DocumentIdBuilderId);
		Assert.assertTrue(ctx.getDocumentRelationBuilder() instanceof DocumentRelationBuilderByKey);
		Assert.assertTrue(ctx.getDocumentLabelBuilder() instanceof DocumentLabelBuilderByType);
	}
	
	
}
