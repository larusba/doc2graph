# document-grapher
Convert JSON from document-oriented DB to neo4j graph.

DocumentGrapher is a project for synchronization from document-oriented database to Neo4J.
The goal is to allow graph analizes on documental data using Neo4j; documents are stored into Neo4j reusing existing nodes so we can analyze document relationships.
Nowadays only exists the Couchbase connector, but the infrastructure can be connected with every source.
There is neo4j-json project that expose some procedures for digest JSON document via cypher query.

# Neo4j-json install
Neo4j-json is a neo4j plugin, writed for version 3.0 or later, that create these procedures:
- json.upsert({key},{json)
- json.delete({key})

These procedures can be called from cypher query and are indipendent from the source of json.
To build the default configuration you have to download source and run

```
mvn package
cp target/neo4j-json\*.jar ${NEO\_HOME}/plugins/
${NEO\_HOME}/bin/neo4j restart
```

# Neo4h-json configuration
Not yet implemented. In version 1.0 will be possible to custumize these feautures:
- document ID
- relationships management

# DCP connector
couchbase-neo4j-connector is a standalone program that transfer mutation from Couchbase bucket to neo4j database.
There is an implementation that use BOLT protocol to call neo4j-json procedures.
To build couchbase-neo4j-connector you have to download source and run

```
mvn package
```

To run the connector 

```
java -jar couchbase-neo4j-connector -Dconf.file=dcp-configuration.properties
```

# DCP connector configuration
To run the couchbase-neo4j-connector you must have a configuration file. You can find a prototype into source 'neo4j-sync.propeties'.
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

Change with the right information and use it to run couchbase-neo4j-connector.


# Test
TODO

# Performance
TODO

