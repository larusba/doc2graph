"""
Neo4j implementation for the DocManager. Receives documents and 
communicates with Neo4j Server using doc2graph cypher api.
"""
import base64
import logging
import os
import os.path as path, sys

import bson.json_util
from bson.json_util import dumps

from mongo_connector.doc_managers.error_handler import ErrorHandler

from py2neo import Graph, authenticate


from mongo_connector import errors
from mongo_connector.compat import u
from mongo_connector.constants import (DEFAULT_COMMIT_INTERVAL,
                                       DEFAULT_MAX_BULK)
from mongo_connector.util import exception_wrapper, retry_until_ok
from mongo_connector.doc_managers.doc_manager_base import DocManagerBase

errors_handler = ErrorHandler()
wrap_exceptions = exception_wrapper(errors_handler.error_hash)

LOG = logging.getLogger(__name__)

class DocManager(DocManagerBase):
  """
  Neo4j implementation for the DocManager. Receives documents and 
  communicates with Neo4j Server using doc2graph cypher api.
  """

  def __init__(self, url, auto_commit_interval=DEFAULT_COMMIT_INTERVAL,
                 unique_key='_id', chunk_size=DEFAULT_MAX_BULK, **kwargs):
    
    self.graph = Graph(url)
    self.auto_commit_interval = auto_commit_interval
    self.unique_key = unique_key
    self.chunk_size = chunk_size
    self.kwargs = kwargs.get("clientOptions")

  def stop(self):
    """Stop the auto-commit thread."""
    self.auto_commit_interval = None
    #TODO bulk not implemented
  
  @wrap_exceptions
  def upsert(self, doc, namespace, timestamp):
    """Inserts a document into Neo4j."""
    doc_id = u(doc.pop("_id"))
    statement = "CALL json.upsert({doc_id},{doc})"
    params_dict = {"doc_id": doc_id, "doc": dumps(doc)}
    tx = self.graph.cypher.begin()
    tx.append(statement, params_dict)
    tx.commit()

  @wrap_exceptions
  def bulk_upsert(self, docs, namespace, timestamp):
    """Insert multiple documents into Neo4j."""
    """Maximum chunk size is 1000. Transaction blocks won't have more than 1000 statements."""
    tx = self.graph.cypher.begin()
    #TODO implement
    tx.commit()

  @wrap_exceptions
  def update(self, document_id, update_spec, namespace, timestamp):
    #FIXME implement
    tx = self.graph.cypher.begin()
    tx.commit()

  @wrap_exceptions
  def remove(self, document_id, namespace, timestamp):
    """Removes a document from Neo4j."""
    doc_id = u(document_id)
    statement = "CALL json.delete({doc_id})"
    params_dict = {"doc_id": doc_id}
    tx = self.graph.cypher.begin()
    tx.append(statement, params_dict)
    tx.commit()

  @wrap_exceptions
  def search(self, start_ts, end_ts):
    #TODO implement
    statement = "MATCH (d:Document) WHERE d._ts>={start_ts} AND d._ts<={end_ts} RETURN d".format(start_ts=start_ts, end_ts=end_ts)
    results = self.graph.cypher.execute(statement)
    return results


  def commit(self):
    LOG.error("Commit")
    

  @wrap_exceptions
  def get_last_doc(self):
    """Get the most recently modified node from Neo4j.
    This method is used to help define a time window within which documents
    may be in conflict after a MongoDB rollback.
    """
    LOG.error("Commit")    

  #FIXME remove??? 
  def handle_command(self, doc, namespace, timestamp):
    db = namespace.split('.', 1)[0]

