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

package com.couchbase.client.neo4j.listener;

import com.couchbase.client.dcp.DataEventHandler;
import com.couchbase.client.dcp.message.DcpDeletionMessage;
import com.couchbase.client.dcp.message.DcpMutationMessage;
import com.couchbase.client.deps.io.netty.buffer.ByteBuf;
import com.couchbase.client.deps.io.netty.util.CharsetUtil;
import com.couchbase.client.neo4j.connector.Neo4jConnector;

/**
 * DataEventHandler for Neo4j implementation
 * 
 * @author Omar Rampado
 *
 */
public class Neo4jDataEventHandler implements DataEventHandler {

//	private static final CouchbaseLogger log = CouchbaseLoggerFactory.getInstance(Neo4jDataEventHandler.class);
	
	private Neo4jConnector connector;

	/**
	 * @return the connector
	 */
	public Neo4jConnector getConnector() {
		return connector;
	}

	/**
	 * @param connector
	 *            the connector to set
	 */
	public void setConnector(Neo4jConnector connector) {
		this.connector = connector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.couchbase.client.dcp.DataEventHandler#onEvent(com.couchbase.client.
	 * deps.io.netty.buffer.ByteBuf)
	 */
	@Override
	public void onEvent(ByteBuf event) {
		if (DcpMutationMessage.is(event)) {
			String key = DcpMutationMessage.keyString(event);
			String json = DcpMutationMessage.content(event).toString(CharsetUtil.UTF_8);
			connector.onMutation(key, json);
		} else if (DcpDeletionMessage.is(event)) {
			String key = DcpMutationMessage.keyString(event);
			connector.onDelete(key);
		}
		event.release();
	}

}
