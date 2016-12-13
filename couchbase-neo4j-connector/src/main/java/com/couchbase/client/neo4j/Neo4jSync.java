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

package com.couchbase.client.neo4j;

import org.apache.commons.lang3.Validate;

import com.couchbase.client.core.logging.CouchbaseLogger;
import com.couchbase.client.core.logging.CouchbaseLoggerFactory;
import com.couchbase.client.dcp.Client;
import com.couchbase.client.dcp.StreamFrom;
import com.couchbase.client.dcp.StreamTo;
import com.couchbase.client.neo4j.conf.Neo4jSyncConfManager;
import com.couchbase.client.neo4j.conf.impl.PropertiesNeo4jSyncConfManager;
import com.couchbase.client.neo4j.connector.impl.BoltNeo4jConnector;
import com.couchbase.client.neo4j.listener.Neo4jControlEventHandler;
import com.couchbase.client.neo4j.listener.Neo4jDataEventHandler;

/**
 * Main class to start synchronization with Neo4j instance
 * Use: -Dconf.file=<configuration_file>
 * @author Omar Rampado
 *
 */
public class Neo4jSync {

	private static final CouchbaseLogger log = CouchbaseLoggerFactory.getInstance(Neo4jSync.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String configurationFile = System.getProperty("conf.file");
		Validate.notBlank(configurationFile,"Use: -Dconf.file=<configuration_file>");
		
		Neo4jSyncConfManager confManager = new PropertiesNeo4jSyncConfManager(configurationFile);
		
		log.info("configure DCP client on {}[{}]",confManager.getCouchbaseHostname(),confManager.getCouchbaseBucket());
		
		//TODO password?
		// Connect to Couchbase DCP
        final Client client = Client.configure()
                .hostnames(confManager.getCouchbaseHostname())
                .bucket(confManager.getCouchbaseBucket())
                .build();
        
        BoltNeo4jConnector connector = new BoltNeo4jConnector();
        connector.init(confManager);
        
        //FIXME manage rollback
        Neo4jControlEventHandler controlEventHandler = new Neo4jControlEventHandler();
        controlEventHandler.init(client);
                
        log.info("configure BoltNeo4jConnector as neo4jConnector");
        log.info("configure Neo4jDataEventHandler as dataEventHandler");
        Neo4jDataEventHandler dataEventHandler = new Neo4jDataEventHandler();
		dataEventHandler.setConnector(connector);
        client.dataEventHandler(dataEventHandler);
        
        // Connect the sockets
        client.connect().await();

        // Initialize the state (start now, never stop)
        log.info("initialize stream from NOW to INFINITY");
        client.initializeState(StreamFrom.NOW, StreamTo.INFINITY).await();

        // Start streaming on all partitions
        client.startStreaming().await();
        
        //NEVER STOP
	}

}
