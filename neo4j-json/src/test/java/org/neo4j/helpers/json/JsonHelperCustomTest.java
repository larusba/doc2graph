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

package org.neo4j.helpers.json;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Values;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.helpers.json.config.JsonHelperConfiguration;

/**
 * Test with default configuration
 * @author Omar Rampado
 *
 */
public class JsonHelperCustomTest {

	private static final String CALL_UPSERT = "CALL json.upsert({key},{json})";
	private static final String CALL_DELETE = "CALL json.delete({key})";

	// This rule starts a Neo4j instance for us
	@ClassRule
	public static Neo4jRule neo4j = new Neo4jRule().withProcedure(JsonHelper.class);

	private static Driver driver;

	private Session session;

	@BeforeClass
	public static void init() {
		JsonHelperConfiguration.reset();
		driver = GraphDatabase.driver(neo4j.boltURI(),
				Config.build()
				.withEncryptionLevel(Config.EncryptionLevel.NONE)
//				.withLogging(new ConsoleLogging(Level.ALL))
				.toConfig());
	}

	@AfterClass
	public static void close() {
		driver.close();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		session = driver.session();
		//clean database
		session.run("MATCH (n)-[r]->(o) DELETE n,r,o");
		session.run("MATCH (n) DELETE n");
		//set configuration
		session.run("CREATE (n:JSON_CONFIG {"
				+ "configuration: 'byNode'"
				+ ",root_node_key_property:'_root'"
				+ ",document_default_label:'DOCUMENT'"
				+ ",document_id_builder:'org.neo4j.helpers.json.document.impl.DocumentIdBuilderId'"
				+ ",document_relation_builder:'org.neo4j.helpers.json.document.impl.DocumentRelationBuilderByKey'"  
				+ ",document_label_builder:'org.neo4j.helpers.json.document.impl.DocumentLabelBuilderById'"
				+ ",log_discard_events: true"
				+ "})");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		session.close();
	}

	/**
	 * Test method for
	 * {@link org.neo4j.helpers.json.JsonHelper#upsertJson(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldAddOnlyOneNode() {
		String key = "shouldAddOnlyOneNode";
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\""
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json ));
		StatementResult result = session.run("MATCH (n {id: '1', type: 'artist'}) RETURN n.name");
		Assert.assertEquals("Wrong node","Genesis", result.single().get("n.name").asString());
		
	}
	
	@Test
	public void shouldDiscard() {
		String key = "shouldDiscard";
		String json = "{"//missing ID
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\""
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json ));
		StatementResult result = session.run("MATCH (n {type: 'artist'}) RETURN n.name");
		Assert.assertFalse("Node must be discarded",result.hasNext());
		
	}
	
	@Test
	public void shouldCreatePrimitiveArray() {
		String key = "shouldAddOnlyOneNode";
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"members\": [\"Tony Banks\",\"Mike Rutherford\",\"Phil Collins\"],"
				+ "\"years\": [1967, 1998, 1999, 2000, 2006]"
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json ));
		StatementResult result = session.run("MATCH (n {id: '1', type: 'artist'}) RETURN n.members");
		Assert.assertEquals("Wrong node","Tony Banks", result.single().get("n.members").asList().get(0));
	}
	
	@Test
	public void shouldCreateComplexArray1(){
		String key = "shouldCreateComplexArray1";
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"From Genesis to Revelation\""
				+ "             }"
				+ "]"
				+ "}";
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json ));
		
		StatementResult result = session.run("MATCH (art {type: 'artist'}) - [r] -> (alb {type: 'album'}) RETURN art.name, alb.title,r");
		Record single = result.single();
		Assert.assertEquals("Wrong node","Genesis", single.get("art.name").asString());
		Assert.assertEquals("Wrong inner node","From Genesis to Revelation", single.get("alb.title").asString());
		Assert.assertEquals("Wrong inner node","shouldCreateComplexArray1", single.get("r").asRelationship().type());

	}
	
	@Test
	public void shouldCreateRelationKeys(){
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"FIRST From Genesis to Revelation\""
				+ "             }"
				+ "]"
				+ "}";
		
		String json2 = "{\"id\": \"2\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"King Crimson\","
				//same album of Genesis
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"From Genesis to Revelation\"" //change title
				+ "             }"
				+ "]"
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", "genesis", "json", json ));
		session.run(CALL_UPSERT, Values.parameters( "key", "kingcrimson", "json", json2 ));
		
		StatementResult result = session.run("MATCH (art {type: 'artist'}) - [r] -> (alb {type: 'album'}) RETURN art.name, alb.title,r");
		List<Record> list = result.list();
		Assert.assertEquals(2, list.size());
		
		Record genesis = list.get(0);
		Assert.assertEquals("Wrong node","Genesis", genesis.get("art.name").asString());
		Assert.assertEquals("Wrong inner node","From Genesis to Revelation", genesis.get("alb.title").asString());
		Assert.assertEquals("Wrong inner node","genesis", genesis.get("r").asRelationship().type());

		Record king = list.get(1);
		Assert.assertEquals("Wrong node","King Crimson", king.get("art.name").asString());
		Assert.assertEquals("Wrong inner node","From Genesis to Revelation", king.get("alb.title").asString());
		Assert.assertEquals("Wrong inner node","kingcrimson", king.get("r").asRelationship().type());
		
	}
	
	@Test
	public void shouldCreateComplexArray2(){
		String key = "shouldCreateComplexArray2";
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"From Genesis to Revelation\""
				+ "             },"
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"20\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"John Anthony\""
				+ "             }"
				+ "]"
				+ "}";
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json ));
		
		StatementResult result = session.run("MATCH (art {type: 'artist'}) - [r] -> (alb {type: 'album'}) RETURN art.name, alb.title,r");
		List<Record> list = result.list();
		Assert.assertEquals(2, list.size());
	}
	
	@Test
	public void shouldUpdateValue() {
		String key = "shouldUpdateValue";
		String json1 = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\""
				+ "}";
		
		String json2 = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"King Crimson\","
				+ "\"bornYear\": 1969"
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json1 ));
		StatementResult result = session.run("MATCH (n {id: '1', type: 'artist'}) RETURN n.name");
		Assert.assertEquals("Wrong node","Genesis", result.single().get("n.name").asString());
		
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json2 ));
		StatementResult result2 = session.run("MATCH (n {id: '1', type: 'artist'}) RETURN n.name, n.bornYear");
		Record single = result2.single();
		Assert.assertEquals("Wrong node","King Crimson", single.get("n.name").asString());
		Assert.assertEquals("Wrong node",1969, single.get("n.bornYear").asLong());
	}
	
	@Test
	public void shouldUpdateOnlyOneNode() {
		String key = "shouldUpdateOnlyOneNode";
		
		String json0 = "{\"id\": \"0\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\""
				+ "}";
		
		String json1 = "{\"id\": \"0\", "
				+ "\"type\": \"other\","
				+ "\"name\": \"Genesis\""
				+ "}";
		
		String json2 = "{\"id\": \"0\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"King Crimson\","
				+ "\"bornYear\": 1969"
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", key+"_json0", "json", json0 ));
		
		session.run(CALL_UPSERT, Values.parameters( "key", key+"_json1", "json", json1 ));
		StatementResult result = session.run("MATCH (n {id: '0'}) RETURN n.type");
		Assert.assertEquals("Wrong node","other", result.single().get("n.type").asString());
		
		session.run(CALL_UPSERT, Values.parameters( "key", key+"_json2", "json", json2 ));
		StatementResult result2 = session.run("MATCH (n {id: '0', type: 'artist'}) RETURN n.name, n.bornYear");
		Record single = result2.single();
		Assert.assertEquals("Wrong node","King Crimson", single.get("n.name").asString());
		Assert.assertEquals("Wrong node",1969, single.get("n.bornYear").asLong());
	}
	
	@Test
	public void shouldAddNestedNode() {
		String key = "shouldAddNestedNode";
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"origin\": {"
				+ "              \"type\": \"origin\","
				+ "              \"id\": \"1483\","
				+ "              \"sovereign-state\": \"UK\","
				+ "              \"country\": \"England\","				
				+ "              \"region\": \"South East\""
				+ "            }"				
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json ));
		
		StatementResult result = session.run("MATCH (n {id: '1', type: 'artist'}) RETURN n.name");
		Assert.assertEquals("Wrong node","Genesis", result.single().get("n.name").asString());
		
		StatementResult result1 = session.run("MATCH (n {id: '1483', type: 'origin'}) RETURN n.country");
		Assert.assertEquals("Wrong inner node","England", result1.single().get("n.country").asString());
	}
	
	@Test
	public void shouldCreateRelation() {
		String key = "shouldCreateRelation";
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"origin\": {"
				+ "              \"type\": \"origin\","
				+ "              \"id\": \"1483\","
				+ "              \"sovereign-state\": \"UK\","
				+ "              \"country\": \"England\","				
				+ "              \"region\": \"South East\""
				+ "            }"				
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json ));
		
		StatementResult result = session.run("MATCH (a {id: '1', type: 'artist'}) - [r] -> (o) RETURN a.name, o.country,r");
		Record single = result.single();
		Assert.assertEquals("Wrong node","Genesis", single.get("a.name").asString());
		Assert.assertEquals("Wrong inner node","England", single.get("o.country").asString());
		Assert.assertEquals("Wrong inner node","shouldCreateRelation", single.get("r").asRelationship().type());
	}

	@Test
	public void shouldCreate2LevelRelation(){
		String key = "shouldCreate2LevelRelation";
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"From Genesis to Revelation\","
				+ "               \"tracks\": [{"
				+ "                             \"type\": \"track\","
				+ "                             \"id\": \"100\","
				+ "                             \"title\": \"Where the Sour Turns to Sweet\""
				+ "                           }]"
				+ "             }"
				+ "]"
				+ "}";
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json ));
		
		StatementResult result = session.run("MATCH (art {type: 'artist'}) - [r] -> (alb {type: 'album'}) RETURN art.name, alb.title,r");
		Record single = result.single();
		Assert.assertEquals("Wrong node","Genesis", single.get("art.name").asString());
		Assert.assertEquals("Wrong inner node","From Genesis to Revelation", single.get("alb.title").asString());
		Assert.assertEquals("Wrong inner node","shouldCreate2LevelRelation", single.get("r").asRelationship().type());
		
		StatementResult result1 = session.run("MATCH (alb {type: 'album'}) - [r] -> (tra {type: 'track'}) RETURN alb.title, tra.title,r");
		Record single1 = result1.single();
		Assert.assertEquals("Wrong album node","From Genesis to Revelation", single1.get("alb.title").asString());
		Assert.assertEquals("Wrong track node","Where the Sour Turns to Sweet", single1.get("tra.title").asString());
		Assert.assertEquals("Wrong inner node","shouldCreate2LevelRelation", single1.get("r").asRelationship().type());
	}
	
	@Test
	public void shouldDeleteNodeAndRelation(){
		String key = "shouldDeleteNodeAndRelation";
		String json1 = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"From Genesis to Revelation\","
				+ "               \"tracks\": [{"
				+ "                             \"type\": \"track\","
				+ "                             \"id\": \"100\","
				+ "                             \"title\": \"Where the Sour Turns to Sweet\""
				+ "                           }]"
				+ "             }"
				+ "]"
				+ "}";
		
		String json2 = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"albums\": ["
				+ "]"
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json1 ));
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json2 ));
		
		StatementResult result = session.run("MATCH (art {type: 'artist'}) - [r] -> (alb {type: 'album'}) RETURN art.name, alb.title,r");
		Assert.assertTrue(result.list().isEmpty());
		
		StatementResult result1 = session.run("MATCH (art {type: 'artist'}) RETURN art.name");
		Assert.assertEquals("Wrong node","Genesis", result1.single().get("art.name").asString());
	}
	
	/**
	 * Test method for
	 * {@link org.neo4j.helpers.json.JsonHelper#deleteJson(java.lang.String)}.
	 */
	@Test
	public void shouldDeleteJson() {
		String key = "shouldDeleteJson";
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"From Genesis to Revelation\","
				+ "               \"tracks\": [{"
				+ "                             \"type\": \"track\","
				+ "                             \"id\": \"100\","
				+ "                             \"title\": \"Where the Sour Turns to Sweet\""
				+ "                           }]"
				+ "             }"
				+ "]"
				+ "}";
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json ));
		StatementResult result1 = session.run("MATCH (art {type: 'artist'}) RETURN art.name");
		Assert.assertEquals("Wrong node","Genesis", result1.single().get("art.name").asString());
		
		session.run(CALL_DELETE, Values.parameters( "key", key));
		StatementResult result = session.run("MATCH (n) RETURN n");
		Assert.assertEquals(1,result.list().size());//JSON_CONFIG
	}
	
	@Test
	public void shouldDeleteDocumentWihoutRelations() {
		String key = "shouldDeleteDocumentWihoutRelations";
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\""
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json ));
		StatementResult result1 = session.run("MATCH (art {type: 'artist'}) RETURN art.name");
		Assert.assertEquals("Wrong node","Genesis", result1.single().get("art.name").asString());
		
		session.run(CALL_DELETE, Values.parameters( "key", key));
		StatementResult result = session.run("MATCH (n) RETURN n");
		Assert.assertEquals(1,result.list().size());//JSON_CONFIG
	}
	
	@Test
	public void shouldSetDocumentKey() {
		String key = "shouldSetDocumentKey";
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"From Genesis to Revelation\","
				+ "               \"tracks\": [{"
				+ "                             \"type\": \"track\","
				+ "                             \"id\": \"100\","
				+ "                             \"title\": \"Where the Sour Turns to Sweet\""
				+ "                           }]"
				+ "             }"
				+ "]"
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", key, "json", json ));
		StatementResult result1 = session.run("MATCH (n {_root: 'shouldSetDocumentKey'}) RETURN n.type");
		Assert.assertEquals("Wrong _root","artist", result1.single().get("n.type").asString());

	}
	
	@Test
	public void shouldDeleteOneJson() {
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"From Genesis to Revelation\","
				+ "               \"tracks\": [{"
				+ "                             \"type\": \"track\","
				+ "                             \"id\": \"100\","
				+ "                             \"title\": \"Where the Sour Turns to Sweet\""
				+ "                           }]"
				+ "             }"
				+ "]"
				+ "}";
		
		String json2 = "{\"id\": \"2\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"King Crimson\""
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", "genesis", "json", json ));
		session.run(CALL_UPSERT, Values.parameters( "key", "kingcrimson", "json", json2 ));
		
		StatementResult result1 = session.run("MATCH (art {type: 'artist'}) RETURN art.name");
		Assert.assertEquals(2,result1.list().size());
		
		session.run(CALL_DELETE, Values.parameters( "key", "genesis"));
		
		StatementResult result = session.run("MATCH (n:ID_2) RETURN n.name");
		Assert.assertEquals("King Crimson", result.single().get("n.name").asString());
	}

	@Test
	public void shouldAddRelationKeys(){
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"From Genesis to Revelation\","
				+ "               \"tracks\": [{"
				+ "                             \"type\": \"track\","
				+ "                             \"id\": \"100\","
				+ "                             \"title\": \"Where the Sour Turns to Sweet\""
				+ "                           }]"
				+ "             }"
				+ "]"
				+ "}";
		
		String json2 = "{\"id\": \"2\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"King Crimson\","
				//same album of Genesis
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"From Genesis to Revelation\","
				+ "               \"tracks\": [{"
				+ "                             \"type\": \"track\","
				+ "                             \"id\": \"100\","
				+ "                             \"title\": \"Where the Sour Turns to Sweet\""
				+ "                           }]"
				+ "             }"
				+ "]"
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", "genesis", "json", json ));
		session.run(CALL_UPSERT, Values.parameters( "key", "kingcrimson", "json", json2 ));
		
		StatementResult result = session.run("MATCH (alb {type: 'album'}) - [r] -> (tra {type: 'track'}) RETURN alb.title, tra.title,r,type(r)");
		List<Record> list = result.list();
		Assert.assertEquals(2, list.size());
		
		List<String> results = list.stream().map(n -> n.get("type(r)").asString()).collect(Collectors.toList());
		Assert.assertTrue(results.contains("genesis"));
		Assert.assertTrue(results.contains("kingcrimson"));
	}
	
	@Test
	public void shouldDeleteRelationKey(){
		String json = "{\"id\": \"1\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"Genesis\","
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"From Genesis to Revelation\","
				+ "               \"tracks\": [{"
				+ "                             \"type\": \"track\","
				+ "                             \"id\": \"100\","
				+ "                             \"title\": \"Where the Sour Turns to Sweet\""
				+ "                           }]"
				+ "             }"
				+ "]"
				+ "}";
		
		String json2 = "{\"id\": \"2\", "
				+ "\"type\": \"artist\","
				+ "\"name\": \"King Crimson\","
				//same album of Genesis
				+ "\"albums\": ["
				+ "             {"
				+ "               \"type\": \"album\","
				+ "               \"id\": \"10\","
				+ "               \"producer\": \"Jonathan King\","
				+ "               \"title\": \"From Genesis to Revelation\","
				+ "               \"tracks\": [{"
				+ "                             \"type\": \"track\","
				+ "                             \"id\": \"100\","
				+ "                             \"title\": \"Where the Sour Turns to Sweet\""
				+ "                           }]"
				+ "             }"
				+ "]"
				+ "}";
		
		session.run(CALL_UPSERT, Values.parameters( "key", "genesis", "json", json ));
		session.run(CALL_UPSERT, Values.parameters( "key", "kingcrimson", "json", json2 ));
		session.run(CALL_DELETE, Values.parameters( "key", "genesis"));
		
		StatementResult result = session.run("MATCH (alb {type: 'album'}) - [r] -> (tra {type: 'track'}) RETURN alb.title, tra.title,type(r)");
		List<Record> list = result.list();
		Assert.assertEquals(1, list.size());
		
		Record fromGenesis = list.get(0);
		
		Assert.assertEquals("kingcrimson",fromGenesis.get("type(r)").asString());
	}
}
