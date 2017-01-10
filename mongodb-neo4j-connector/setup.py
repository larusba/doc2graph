
import os
import sys

try:
    from setuptools import setup, find_packages
    from setuptools.extension import Extension
except ImportError:
    from ez_setup import use_setuptools
    use_setuptools()
    from setuptools import setup
    from distutils.core import setup, find_packages
    from distutils.extension import Extension

extra_opts = {}

python_2 = sys.version_info < (3,)

try:
    with open("README.md", "r") as fd:
        extra_opts['long_description'] = fd.read()
except IOError:
    pass        # Install without README.md

packages = ["mongo_connector", "mongo_connector.doc_managers"]
package_metadata = {
    "name": "neo4j-json-doc-manager",
    "version": "0.0.1",
    "description": "Neo4j Json Doc manager for Mongo Connector",
    "long_description": "Neo4j Json Doc Manager is a tool that will import data from Mongodb to a " 
                        "Neo4j graph structure, via Mongo-Connector, using doc2graph way.",
    "author": "LARUS",
    "author_email": "omar.rampado@larus-ba.it",
    "url": "https://github.com/neo4j-contrib/neo4j_doc_manager.git",  #FIXME change
    "entry_points": {
        "console_scripts": [
            'mongo-connector = mongo_connector.connector:main',
        ],
    },
    "packages": packages,
    "install_requires": ['mongo-connector>=2.1','py2neo==2.0.8','requests>=2.5.1'],
    "license": "Apache Software License",
    "classifiers": [
        "Development Status :: 5 - Production/Stable", #FIXME change
        "Intended Audience :: Developers",
        "License :: OSI Approved :: Apache Software License",
        "Operating System :: OS Independent",
        "Programming Language :: Python :: 2.7",
        "Programming Language :: Python :: 3.3",
        "Programming Language :: Python :: 3.4",
        "Topic :: Database",
        "Topic :: Software Development",
    ],
}


try:
    setup(ext_modules=extensions, **package_metadata)
except:
    setup(**package_metadata)

