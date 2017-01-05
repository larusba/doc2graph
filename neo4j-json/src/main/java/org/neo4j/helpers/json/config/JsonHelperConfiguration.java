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

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.json.JsonHelper;
import org.neo4j.helpers.json.config.impl.JsonHelperConfigurationByNode;
import org.neo4j.helpers.json.config.impl.JsonHelperConfigurationDefault;
import org.neo4j.helpers.json.document.context.DocumentGrapherExecutionContext;
import org.neo4j.logging.Log;

/**
 * Configuration manager for {@link JsonHelper}
 * @author Omar Rampado
 * FIXME rename according with the JsonHelper refactor
 * TODO use threadlocal or other instead of (static) singleton
 */
public abstract class JsonHelperConfiguration {

	/**
	 * Create a new execution context setting db and log.
	 * @param db
	 * @param log
	 * @return
	 */
	protected abstract DocumentGrapherExecutionContext buildContext(GraphDatabaseService db, Log log);
	
	private static JsonHelperConfiguration instance;
	
	/**
	 * Get current configuration manager
	 * @return
	 */
	public static DocumentGrapherExecutionContext newContext(GraphDatabaseService db, Log log)
	{
		
		if(instance == null)
		{
			Node configurationNode = db.findNode(Label.label("JSON_CONFIG"), "configuration", "byNode");
			if(configurationNode == null)
			{
				log.info("Load default configuration: "+JsonHelperConfigurationDefault.class);
				instance = new JsonHelperConfigurationDefault();				
			}else
			{
				log.info("Load configuration from node: "+JsonHelperConfigurationByNode.class);
				instance = new JsonHelperConfigurationByNode(configurationNode);	
			}
		}
		
		return instance.buildContext(db, log);
	}
	
	public static void reset()
	{
		instance = null;
	}
}
