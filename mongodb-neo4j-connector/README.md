# mongodb-neo4j-connector
It's a `doc_manager` module for [mongodb-connector](https://github.com/mongodb-labs/mongo-connector) that call `neo4j-json`'s procedures. 

# Installation
You must have Python installed in order to use this project. Python 3 is recommended.
Neo4j, MongoDB and `neo4j-json` already installed.

First, install mongodb-connector with pip:

```
pip install mongodb-connector
```

(You might need sudo privileges).

Get the code and install the module and install the module.

```
git clone https://github.com/larusba/doc2graph.git
cd ./doc2graph
pip install ./mongodb-neo4j-connector
```

# For Developers
You don't need to install the `mongodb-neo4j-connector` during the development: you just set the environment variable

```
cd ./mongodb-neo4j-connector 
export PYTHONPATH=.
```

# Run the connector
First set the user credentials for neo4j

```
export NEO4J_AUTH=<user>:<password>
```
Then run the connector

```
mongo-connector -m localhost:27017 -t http://localhost:7474/db/data -d neo4j_json_doc_manager
```


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

