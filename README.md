# doc2graph
Convert JSON from document-oriented DB to [neo4j](https://neo4j.com/) graph.

This project is in BETA version, so it's **absolutely no warranty**.

The goal is to allow graph analizes on documental data using Neo4j; documents are stored into Neo4j reusing existing nodes so we can analyze document relationships.
The logic to convert JSON into a graph is stored into neo4j as procedures, so it can ben used from any kind of JSON source. 
Furthermore, the logic is configurable and programmable: you can obtain exactly the graph you need for yours analysis.

# neo4j-json
It's the core of `doc2graph` project. It's a neo4j's plugin and create some [procedure](http://neo4j.com/docs/developer-manual/current/extending-neo4j/procedures/) to call in cypher.

# couchbase-neo4j-connector
It's a java standalone process that synchronizes a [Couchbase](https://www.couchbase.com/) server to neo4j calling the `neo4j-json` procedures. It works as [DCP client](https://github.com/couchbaselabs/java-dcp-client).

# mongodb-neo4j-connector
It's a python doc\_manager that synchronizes a [MongoDB](https://www.mongodb.com/) replica to neo4j calling the `neo4-json` procedures. It work as module of [mondogb-connector](https://github.com/mongodb-labs/mongo-connector/wiki/Writing%20Your%20Own%20DocManager).

# Test
Nowadays it works on MacBook with OSX 10.11, Couchbase 4.5 enterprise, MongoDB 3.4, Neo4j 3.0.

## License

Copyright (c) 2017 [LARUS Business Automation](http://www.larus-ba.it)

This file is part of the "LARUS Integration Framework for Neo4j".

The "LARUS Integration Framework for Neo4j" is licensed
under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

