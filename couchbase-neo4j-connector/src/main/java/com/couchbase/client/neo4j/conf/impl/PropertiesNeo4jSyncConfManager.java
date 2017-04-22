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

package com.couchbase.client.neo4j.conf.impl;

import java.io.FileInputStream;
import java.util.Properties;

import com.couchbase.client.core.logging.CouchbaseLogger;
import com.couchbase.client.core.logging.CouchbaseLoggerFactory;
import com.couchbase.client.neo4j.conf.Neo4jSyncConfManager;

/**
 * Load configuration properties from file
 * @author Omar Rampado
 *
 */
public class PropertiesNeo4jSyncConfManager implements Neo4jSyncConfManager {
	
	private static final CouchbaseLogger log = CouchbaseLoggerFactory.getInstance(PropertiesNeo4jSyncConfManager.class);
	
	private String couchbaseHostname="localhost";
	private String couchbaseBucket = "travel-sample";
	private String noe4jHostname = "localhost";
	private String noe4jUsername = "neo4j";
	private String noe4jPassword = "neo4j";
	
	/**
	 * Load properties from file and cache them
	 * @param configurationFile
	 */
	public PropertiesNeo4jSyncConfManager(String configurationFile) {
		Properties prop = new Properties();
		try(FileInputStream inStream = new FileInputStream(configurationFile))
		{
			prop.load(inStream);
			couchbaseHostname = prop.getProperty("couchbase.hostname");
			couchbaseBucket = prop.getProperty("couchbase.bucket");
			noe4jHostname = prop.getProperty("neo4j.hostname");
			noe4jUsername = prop.getProperty("neo4j.username");
			noe4jPassword = prop.getProperty("neo4j.password");
			
			log.info("{} load complete",configurationFile);
		}catch(Exception e)
		{
			log.error("Error loading {}: {}",configurationFile, e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see com.couchbase.client.neo4j.conf.Neo4jSyncConfManager#getCouchbaseHostname()
	 */
	@Override
	public String getCouchbaseHostname() {
		return this.couchbaseHostname;
	}

	/* (non-Javadoc)
	 * @see com.couchbase.client.neo4j.conf.Neo4jSyncConfManager#getCouchbaseBucket()
	 */
	@Override
	public String getCouchbaseBucket() {
		return this.couchbaseBucket;
	}

	/* (non-Javadoc)
	 * @see com.couchbase.client.neo4j.conf.Neo4jSyncConfManager#getNeo4jHostname()
	 */
	@Override
	public String getNeo4jHostname() {
		return this.noe4jHostname;
	}

	/* (non-Javadoc)
	 * @see com.couchbase.client.neo4j.conf.Neo4jSyncConfManager#getNeo4jUsername()
	 */
	@Override
	public String getNeo4jUsername() {
		return this.noe4jUsername;
	}

	/* (non-Javadoc)
	 * @see com.couchbase.client.neo4j.conf.Neo4jSyncConfManager#getNeo4jPassword()
	 */
	@Override
	public String getNeo4jPassword() {
		return this.noe4jPassword;
	}

}
