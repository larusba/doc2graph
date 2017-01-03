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

import static org.easymock.EasyMock.replay;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.helpers.json.document.DocumentRelationBuilder;
import org.neo4j.helpers.json.document.context.DocumentGrapherExecutionContext;
import org.neo4j.logging.Log;

/**
 * @author Omar Rampado
 *
 */
@RunWith(EasyMockRunner.class)
public class JsonHelperConfigurationTest {

	@Mock
	public GraphDatabaseService db;

	@Mock
	public Log log;

	/**
	 * Test method for {@link org.neo4j.helpers.json.config.JsonHelperConfiguration#newContext(org.neo4j.graphdb.GraphDatabaseService, org.neo4j.logging.Log)}.
	 */
	@Test
	public void testNewContext() {
		replay(db,log);
		JsonHelperConfiguration instance = JsonHelperConfiguration.getInstance();
		DocumentGrapherExecutionContext ctx1 = instance.newContext(db, log);
		Assert.assertNotNull(ctx1);
		DocumentGrapherExecutionContext ctx2 = instance.newContext(db, log);
		Assert.assertNotNull(ctx2);
		
		Assert.assertFalse("Configurazion must be renew",ctx1 == ctx2);
	}

	@Test
	public void testNewDocumentRelationBuilder() {
		replay(db,log);
		JsonHelperConfiguration instance = JsonHelperConfiguration.getInstance();
		DocumentRelationBuilder ctx1 = instance.newContext(db, log).getDocumentRelationBuilder();
		Assert.assertNotNull(ctx1);
		DocumentRelationBuilder ctx2 = instance.newContext(db, log).getDocumentRelationBuilder();
		Assert.assertNotNull(ctx2);
		
		Assert.assertFalse("DocumentRelationBuilder must be renew",ctx1 == ctx2);
	}
	
	@Test
	public void testConfiguration()
	{
		replay(db,log);
		JsonHelperConfiguration instance = JsonHelperConfiguration.getInstance();
		Assert.assertNotNull(instance);
		
		DocumentGrapherExecutionContext ctx1 = instance.newContext(db, log);
		Assert.assertNotNull(ctx1.getRootNodeKeyProperty());
		Assert.assertNotNull(ctx1.getDb());
		Assert.assertNotNull(ctx1.getLog());
		Assert.assertNotNull(ctx1.getDocumentIdBuilder());
		Assert.assertNotNull(ctx1.getDocumentLabelBuilder());
		Assert.assertNotNull(ctx1.getDocumentRelationBuilder());
	}
}
