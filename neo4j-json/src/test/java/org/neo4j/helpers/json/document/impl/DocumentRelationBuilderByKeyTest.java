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

import java.io.File;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
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
 *
 */
public class DocumentRelationBuilderByKeyTest {

	private static GraphDatabaseService db;
	private static Log log;
	private Transaction tx;
	private DocumentRelationBuilderByKey docrel;
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
		
		this.docrel = new DocumentRelationBuilderByKey();
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
		Assert.assertEquals("key", rel.getType().name());

	}
	
	@Test
	public void shuldAddRelation() {
		context.setDocumentKey("key1");
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		Relationship rel1 = this.docrel.buildRelation(parent, child, context);

		context.setDocumentKey("key2");
		Relationship rel2 = this.docrel.buildRelation(parent, child, context);
		
		Assert.assertNotEquals(rel1.getId(), rel2.getId());
		
		Assert.assertEquals("key1", rel1.getType().name());
		Assert.assertEquals("key2", rel2.getType().name());
	}
	
	@Test
	public void shuldDoNothing() {
		context.setDocumentKey("key");
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		Relationship rel1 = this.docrel.buildRelation(parent, child, context);
		Relationship rel2 = this.docrel.buildRelation(parent, child, context);
		
		Assert.assertEquals(rel1.getId(), rel2.getId());
		
		Assert.assertEquals("key", rel2.getType().name());
	}
	
	@Test
	public void shouldDeleteNothing()
	{
		context.setDocumentKey("key");
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		this.docrel.buildRelation(parent, child, context);
		
		context.setDocumentKey("another_key");
		Set<Node> orphans = this.docrel.deleteRelations(context);
		
		Assert.assertEquals(0, orphans.size());
		Assert.assertEquals(1, StreamSupport.stream(parent.getRelationships().spliterator(), false).count());
	}
	
	@Test
	public void shouldDeleteRelation()
	{
		context.setDocumentKey("key");
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		this.docrel.buildRelation(parent, child, context);
		
		Set<Node> orphans = this.docrel.deleteRelations(context);
		
		Assert.assertEquals(2, orphans.size());
		Assert.assertEquals(0, StreamSupport.stream(parent.getRelationships().spliterator(), false).count());
	}
	
	@Test
	public void shouldDeleteRelations()
	{
		context.setDocumentKey("key");
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		Node track = db.createNode();
		track.setProperty("type", "track");
		
		this.docrel.buildRelation(parent, child, context);
		this.docrel.buildRelation(child, track, context);
		
		Set<Node> orphans = this.docrel.deleteRelations(context);
		
		Assert.assertEquals(3, orphans.size());
		Assert.assertEquals(0, StreamSupport.stream(parent.getRelationships().spliterator(), false).count());
		Assert.assertEquals(0, StreamSupport.stream(child.getRelationships().spliterator(), false).count());
	}
	
	@Test
	public void shouldDeleteOneRelations()
	{
		context.setDocumentKey("key");
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		Node track = db.createNode();
		track.setProperty("type", "track");
		
		this.docrel.buildRelation(parent, child, context);
		context.setDocumentKey("another_key");
		this.docrel.buildRelation(child, track, context);
		
		Set<Node> orphans = this.docrel.deleteRelations(context);
		
		Assert.assertEquals(1, orphans.size());
		Assert.assertEquals(1, StreamSupport.stream(parent.getRelationships().spliterator(), false).count());
		Assert.assertEquals(0, StreamSupport.stream(child.getRelationships(Direction.OUTGOING).spliterator(), false).count());
	}
	
	@Test
	public void shouldDeleteOnlyKey()
	{
		context.setDocumentKey("key");
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		Node track = db.createNode();
		track.setProperty("type", "track");
		
		this.docrel.buildRelation(parent, child, context);
		this.docrel.buildRelation(child, track, context);
		
		context.setDocumentKey("another_key");
		this.docrel.buildRelation(child, track, context);
		
		Set<Node> orphans = this.docrel.deleteRelations(context);
		
		Assert.assertEquals(0, orphans.size());
		Assert.assertEquals(1, StreamSupport.stream(parent.getRelationships().spliterator(), false).count());
		Assert.assertEquals(1, StreamSupport.stream(child.getRelationships(Direction.OUTGOING).spliterator(), false).count());
		
	}
	
	@Test
	public void shouldCreate3Relation()
	{
		context.setDocumentKey("key");
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		this.docrel.buildRelation(parent, child, context);

		context.setDocumentKey("another_key");

		Node track = db.createNode();
		track.setProperty("type", "track");
		this.docrel.buildRelation(parent, child, context);
		this.docrel.buildRelation(child, track, context);
		
		Assert.assertEquals(2, StreamSupport.stream(parent.getRelationships().spliterator(), false).count());
		Assert.assertEquals(1, StreamSupport.stream(child.getRelationships(Direction.OUTGOING).spliterator(), false).count());

	}
	
	@Test
	public void shouldSkipCreationWithoutKey()
	{
		context.setDocumentKey(null);
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		Relationship rel = this.docrel.buildRelation(parent, child, context);
		
		Assert.assertNull(rel);
	}
	
	@Test
	public void shouldSkipCreationWithEmptyKey()
	{
		context.setDocumentKey(" ");
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		Relationship rel = this.docrel.buildRelation(parent, child, context);
		
		Assert.assertNull(rel);
	}
	
	@Test
	public void shouldNotDeleteWithoutKey()
	{
		context.setDocumentKey("key");
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		this.docrel.buildRelation(parent, child, context);
		
		context.setDocumentKey(null);
		Set<Node> orphans = this.docrel.deleteRelations(context);
		Assert.assertTrue(orphans.isEmpty());
	}
	
	@Test
	public void shouldNotDeleteWithEmptyKey()
	{
		context.setDocumentKey("key");
		
		Node parent = db.createNode();
		parent.setProperty("type", "album");
		
		Node child = db.createNode();
		child.setProperty("type", "artist");
		
		this.docrel.buildRelation(parent, child, context);
		
		context.setDocumentKey(" ");
		Set<Node> orphans = this.docrel.deleteRelations(context);
		Assert.assertTrue(orphans.isEmpty());
	}
}
