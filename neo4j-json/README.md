# Neo4j-json
This is the core of `doc2graph` project. 

Neo4j-json is a neo4j [plugin](http://neo4j.com/docs/developer-manual/current/extending-neo4j/procedures/), writed for version 3.0 or later, that exposes these procedures:
* `json.upsert({key},{json})`
* `json.delete({key})`

These procedures can be called from cypher query and are indipendent from the source of json.

# Neo4j-json install
To build the default configuration you have to download source and run

```
mvn package
cp target/neo4j-json\*.jar ${NEO_HOME}/plugins/
${NEO_HOME}/bin/neo4j restart
```

# Neo4j-json configuration
Neo4j-json is released with a default configuration, but it can be changed by creating a special node. The default configuration is like this:

```
CREATE (n:JSON_CONFIG {
        configuration: 'byNode'
        ,root_node_key_property:'_document_key'
        ,document_default_label:'DocNode'
        ,document_id_builder:'org.neo4j.helpers.json.document.impl.DocumentIdBuilderTypeId'
        ,document_relation_builder:'org.neo4j.helpers.json.document.impl.DocumentRelationBuilderHasTypeArrayKey'
        ,document_label_builder:'org.neo4j.helpers.json.document.impl.DocumentLabelBuilderByType'
		,log_discard_events: false
        })
```

If there isn't a JSON\_CONFIG node in database, default configuration is used but no configuration node is created. 

**IMPORTANT**: when you change the configuration you must restart the neo4j server.

## configuration
Node configuration is active only if contains `"configuration: 'byNode'"` else the node is ignored.

## root\_node\_key\_property
When insert a new document a new root node is created. Upsert procedure set a property with `root_node_key_property` as key and the document key as value.

## document\_default\_label
All the nodes are created with a label. This is the default label value used by `document_label_builder` if no other label can be applied.

## document\_id\_builder
It's the name of class that implements `org.neo4j.helpers.json.document.DocumentIdBuilder` interface. It builds a `org.neo4j.helpers.json.document.DocumentId` that stands for primary key value of the sub-document (node). This id is used to seek node in database if already exists. So, it's essential for node reusing.

You can choose from:
* `org.neo4j.helpers.json.document.impl.DocumentIdBuilderTypeId`
* `org.neo4j.helpers.json.document.impl.DocumentIdBuilderId`
* `org.neo4j.helpers.json.document.impl.DocumentIdBuilderPair` with properties:
	** `builder_id_pair_key1`
	** `builder_id_pair_key2`
* your own implementation
 

**IMPORTANT**: seeking node use also the label so you have to pay attention for label-id combo. 

## document\_relation\_builder
It's the name of class that implements `org.neo4j.helpers.json.document.DocumentRelationBuilder` interface. It manages the relationships, adding and removing them between nodes and deciding which nodes are orphans. Orphan nodes are deleted from database.

You can choose from:
* `org.neo4j.helpers.json.document.impl.DocumentRelationBuilderTypeArrayKey`
* `org.neo4j.helpers.json.document.impl.DocumentRelationBuilderHasTypeArrayKey`
* `org.neo4j.helpers.json.document.impl.DocumentRelationBuilderByKey`
* your own implementation

## document\_label\_builder
It's the name of class that implements `org.neo4j.helpers.json.document.DocumentLabelBuilder` interface. It builds the label that is applied on a node. When it cannot builds from subdocument data it uses the default value (`document_default_label`). Labels are used also to seek node that already exists in database, so you have to use it carefully.

You can choose from:
* `org.neo4j.helpers.json.document.impl.DocumentLabelBuilderConstant`
* `org.neo4j.helpers.json.document.impl.DocumentLabelBuilderByType`
* `org.neo4j.helpers.json.document.impl.DocumentLabelBuilderById`
* your own implementation

# Performance
TODO

## License

Copyright (c) 2016 [LARUS Business Automation](http://www.larus-ba.it)

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

