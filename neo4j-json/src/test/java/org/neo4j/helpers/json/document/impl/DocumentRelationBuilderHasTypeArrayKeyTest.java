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

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.json.document.context.DocumentRelationContext;
import org.neo4j.logging.FormattedLogProvider;
import org.neo4j.logging.Log;


/**
 * @author Omar Rampado
 */
public class DocumentRelationBuilderHasTypeArrayKeyTest {

	private static GraphDatabaseService db;
	private static Log log;
	private Transaction tx;
	private DocumentRelationBuilderHasTypeArrayKey docrel;
	private DocumentRelationContext context;
	
	@BeforeClass
	public static void init() {
		db = new GraphDatabaseFactory().newEmbeddedDatabase( new File(System.getProperty( "java.io.tmpdir" )) );
		FormattedLogProvider logProv = FormattedLogProvider.withDefaultLogLevel(org.neo4j.logging.Level.DEBUG).toOutputStream(System.out);
		log = logProv.getLog("");
	}
	
	@AfterClass
	public static void end()
	{
		db.shutdown();
	}

	@Before
	public void setUp()
	{
		tx = db.beginTx();
		db.getAllRelationships().forEach(rel -> rel.delete());
		db.getAllNodes().forEach(node -> node.delete());
		tx.success();
		
		tx = db.beginTx();
		
		this.docrel = new DocumentRelationBuilderHasTypeArrayKey();
		this.docrel.setDb(db);
		this.docrel.setLog(log);
		context = new DocumentRelationContext();
	}
	
	@After
	public void tearDown()
	{
		tx.failure();
	}
	
	
	@Test
	public void shuldCreateRelation() {
		context.setDocumentKey("key");
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		Relationship rel = this.docrel.buildRelation(parent, child, context);

		Assert.assertEquals(parent.getId(), rel.getStartNode().getId());
		Assert.assertEquals(child.getId(), rel.getEndNode().getId());
		Assert.assertEquals("HAS_ARTIST", rel.getType().name());
		
		String[] keys = (String[]) rel.getProperty("docKeys");
		
		Assert.assertEquals(1, keys.length);
		Assert.assertEquals("key", keys[0]);
	}
}
