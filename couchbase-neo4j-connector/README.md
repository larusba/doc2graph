# DCP connector
couchbase-neo4j-connector is a standalone program that transfer mutation from Couchbase bucket to neo4j database.
There is only one implementation that use [BOLT](https://neo4j.com/blog/neo4j-3-0-milestone-1-release/) protocol to call `neo4j-json` procedures.

**This project depends to dpc-client, in latest version (0.8.0) that can be download and install from [here](https://github.com/couchbaselabs/java-dcp-client).**

To build `couchbase-neo4j-connector` you have to download source and run

```
mvn package
```

To run the connector 

```
java -jar couchbase-neo4j-connector -Dconf.file=dcp-configuration.properties
```

# DCP connector configuration
To run the `couchbase-neo4j-connector` you must have a configuration file. You can find a prototype into source `neo4j-sync.propeties`.
Here's the content:

```
# Sync
couchbase.bucket=cbnc\_test
couchbase.hostname=localhost

# Neo4j
neo4j.hostname=localhost
neo4j.username=neo4j
neo4j.password=admin

```

Change with the right information and use it to run `couchbase-neo4j-connector`.


# Test
TODO

# Performance
TODO

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

