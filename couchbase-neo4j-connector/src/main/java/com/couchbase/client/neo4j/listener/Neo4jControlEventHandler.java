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

package com.couchbase.client.neo4j.listener;

import com.couchbase.client.core.logging.CouchbaseLogger;
import com.couchbase.client.core.logging.CouchbaseLoggerFactory;
import com.couchbase.client.dcp.Client;
import com.couchbase.client.dcp.ControlEventHandler;
import com.couchbase.client.dcp.message.DcpSnapshotMarkerRequest;
import com.couchbase.client.dcp.message.RollbackMessage;
import com.couchbase.client.deps.io.netty.buffer.ByteBuf;

import rx.Completable;
import rx.Subscription;

/**
 * ControlEventHandler for Neo4j synchronization 
 * TODO implement
 * @author Omar Rampado
 *
 */
public class Neo4jControlEventHandler implements ControlEventHandler {
	private static final CouchbaseLogger log = CouchbaseLoggerFactory.getInstance(Neo4jControlEventHandler.class);

	private Client client;
	
	/**
	 * Link client with this handler
	 * @param client
	 */
	public void init(Client client)
	{
		log.info("configure {} as controEventHandler",Neo4jControlEventHandler.class.getName());
		this.client = client;
		client.controlEventHandler(this);
	}
	
	/* (non-Javadoc)
	 * @see com.couchbase.client.dcp.ControlEventHandler#onEvent(com.couchbase.client.deps.io.netty.buffer.ByteBuf)
	 */
	@Override
    public void onEvent(final ByteBuf event) {
		
		//TODO implement
		
        if (DcpSnapshotMarkerRequest.is(event)) {
            client.acknowledgeBuffer(event);
        }
        if (RollbackMessage.is(event)) {
            final short partition = RollbackMessage.vbucket(event);
            client.rollbackAndRestartStream(partition, RollbackMessage.seqno(event))
                    .subscribe(new Completable.CompletableSubscriber() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onSubscribe(Subscription d) {
                        }
                    });
        }
        event.release();
    }
}
