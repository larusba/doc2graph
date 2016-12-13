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

package com.couchbase.client.neo4j.connector.impl;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;

import com.couchbase.client.core.logging.CouchbaseLogger;
import com.couchbase.client.core.logging.CouchbaseLoggerFactory;
import com.couchbase.client.neo4j.conf.Neo4jSyncConfManager;
import com.couchbase.client.neo4j.connector.Neo4jConnector;

/**
 * Call Neo4j procedures using Bolt protocol
 * @author Omar Rampado
 *
 */
public class BoltNeo4jConnector implements Neo4jConnector {
	
	private static final CouchbaseLogger log = CouchbaseLoggerFactory.getInstance(BoltNeo4jConnector.class);
	
	//TODO convert in threadlocal???
	private Session session;
	
	/**
	 * 
	 */
	public BoltNeo4jConnector() {
	}
	
	public void init(Neo4jSyncConfManager configuration)
	{
		log.info("configure neo4j connection to {}, user {}",configuration.getNeo4jHostname(),configuration.getNeo4jUsername());
		Driver driver = GraphDatabase.driver("bolt://"+configuration.getNeo4jHostname(), 
				AuthTokens.basic(configuration.getNeo4jUsername(), configuration.getNeo4jPassword()));
		session = driver.session();
	}

	/* (non-Javadoc)
	 * @see com.couchbase.client.neo4j.connector.Neo4jConnector#onMutation(java.lang.String, java.lang.String)
	 */
	@Override
	public String onMutation(String key, String json) {
		session.run("CALL json.upsert({key},{json})", Values.parameters("key",key,"json",json));
		return key;
	}

	/* (non-Javadoc)
	 * @see com.couchbase.client.neo4j.connector.Neo4jConnector#onDelete(java.lang.String)
	 */
	@Override
	public String onDelete(String key) {
		session.run("CALL json.delete({key})", Values.parameters("key",key));
		return key;
	}

}
