import re
import logging

from py2neo import GraphError as graph_error, BindError as bind_error
from py2neo.cypher.error import ClientError as client_error
from py2neo.cypher import CypherTransactionError as cypher_transaction_error
from py2neo.cypher.error.statement import InvalidType as invalid_type
from py2neo.cypher import CypherError as cypher_error
from py2neo.cypher.error.request import Invalid as invalid
from py2neo.cypher.error.request import InvalidFormat as invalid_format
from py2neo.cypher.error.schema import ConstraintAlreadyExists as constraint_already_exists
from py2neo.cypher.error.schema import ConstraintVerificationFailure as constraint_verification_failure
from py2neo.cypher.error.schema import ConstraintViolation as constraint_violation
from py2neo.cypher.error.schema import IllegalTokenName as illegal_token_name
from py2neo.cypher.error.schema import IndexAlreadyExists as index_already_exists
from py2neo.cypher.error.schema import IndexBelongsToConstraint as index_belongs_to_constraint
from py2neo.cypher.error.schema import LabelLimitReached as label_limit_reached
from py2neo.cypher.error.schema import NoSuchConstraint as no_such_constraint
from py2neo.cypher.error.schema import NoSuchIndex as no_such_index
from py2neo.cypher.error.statement import ArithmeticError as statement_arithmetic_error
from py2neo.cypher.error.statement import ConstraintViolation as statement_constraint_violation
from py2neo.cypher.error.statement import EntityNotFound as entity_not_found
from py2neo.cypher.error.statement import InvalidArguments as invalid_arguments
from py2neo.cypher.error.statement import InvalidSemantics as invalid_semantics
from py2neo.cypher.error.statement import InvalidSyntax as invalid_syntax
from py2neo.cypher.error.statement import NoSuchLabel as no_such_label
from py2neo.cypher.error.statement import NoSuchProperty as no_such_property
from py2neo.cypher.error.statement import ParameterMissing as parameter_missing
from py2neo.cypher.error.transaction import ConcurrentRequest as concurrent_request
from py2neo.cypher.error.transaction import EventHandlerThrewException as event_handler_threw_exception
from py2neo.cypher.error.transaction import InvalidType as transaction_invalid_type
from py2neo.cypher.error.transaction import UnknownId as unknown_id
from py2neo.cypher import DatabaseError as database_error
from py2neo.cypher.error.schema import ConstraintCreationFailure as constraint_creation_failure
from py2neo.cypher.error.schema import ConstraintDropFailure as constraint_drop_failure
from py2neo.cypher.error.schema import IndexCreationFailure as index_creation_failure
from py2neo.cypher.error.schema import IndexDropFailure as index_drop_failure
from py2neo.cypher.error.schema import NoSuchLabel as schema_no_such_label
from py2neo.cypher.error.schema import NoSuchPropertyKey as schema_no_such_property
from py2neo.cypher.error.schema import NoSuchRelationshipType as no_such_relationship_type
from py2neo.cypher.error.schema import NoSuchSchemaRule as no_such_schema_rule
from py2neo.cypher.error.statement import ExecutionFailure as execution_failure
from  py2neo.cypher.error.transaction import CouldNotBegin as could_not_begin
from py2neo.cypher.error.transaction import CouldNotCommit as could_not_commit
from py2neo.cypher.error.transaction import CouldNotRollback as could_not_rollback
from py2neo.cypher.error.transaction import ReleaseLocksFailed as release_locks_failed
from py2neo.cypher import TransientError as transient_error
from py2neo.cypher.error.network import UnknownFailure as unknown_failure
from py2neo.cypher.error.statement import ExternalResourceFailure as external_resource_failure
from py2neo.cypher.error.transaction import AcquireLockTimeout as acquire_lock_timeout

from mongo_connector import errors
from mongo_connector.errors import OperationFailed


LOG = logging.getLogger(__name__)

class Neo4jOperationFailed(OperationFailed):
    """Raised for failed commands on the destination database
    """
    # print("An error ocurred. Please check mongo-connector.log file")

class ErrorHandler(object):
  def __init__(self):
    self.error_hash = {
    graph_error: Neo4jOperationFailed,
    bind_error: errors.ConnectionFailed,
    invalid_type: Neo4jOperationFailed,
    cypher_transaction_error: Neo4jOperationFailed,
    cypher_error: Neo4jOperationFailed,
    client_error: Neo4jOperationFailed,
    invalid: Neo4jOperationFailed,
    invalid_format: Neo4jOperationFailed,
    constraint_already_exists: Neo4jOperationFailed,
    constraint_verification_failure: Neo4jOperationFailed,
    constraint_violation: Neo4jOperationFailed,
    illegal_token_name: Neo4jOperationFailed,
    index_already_exists: Neo4jOperationFailed,
    index_belongs_to_constraint: Neo4jOperationFailed,
    label_limit_reached: Neo4jOperationFailed,
    no_such_constraint: Neo4jOperationFailed,
    no_such_index: Neo4jOperationFailed,
    statement_arithmetic_error: Neo4jOperationFailed,
    statement_constraint_violation: Neo4jOperationFailed,
    entity_not_found: Neo4jOperationFailed,
    invalid_arguments: Neo4jOperationFailed,
    invalid_semantics: Neo4jOperationFailed,
    invalid_syntax: Neo4jOperationFailed,
    no_such_label: Neo4jOperationFailed,
    no_such_property: Neo4jOperationFailed,
    parameter_missing: Neo4jOperationFailed,
    concurrent_request: Neo4jOperationFailed,
    event_handler_threw_exception: Neo4jOperationFailed,
    transaction_invalid_type: Neo4jOperationFailed,
    unknown_id: Neo4jOperationFailed,
    database_error: Neo4jOperationFailed,
    constraint_creation_failure: Neo4jOperationFailed,
    constraint_drop_failure: Neo4jOperationFailed,
    index_creation_failure: Neo4jOperationFailed,
    index_drop_failure: Neo4jOperationFailed,
    schema_no_such_label: Neo4jOperationFailed,
    schema_no_such_property: Neo4jOperationFailed,
    no_such_relationship_type: Neo4jOperationFailed,
    no_such_schema_rule: Neo4jOperationFailed,
    execution_failure: Neo4jOperationFailed,
    could_not_begin: Neo4jOperationFailed,
    could_not_commit: Neo4jOperationFailed,
    could_not_rollback: Neo4jOperationFailed,
    release_locks_failed: Neo4jOperationFailed,
    transient_error: Neo4jOperationFailed,
    unknown_failure: Neo4jOperationFailed,
    external_resource_failure: Neo4jOperationFailed,
    acquire_lock_timeout: Neo4jOperationFailed,
    AttributeError: Neo4jOperationFailed,
    TypeError: Neo4jOperationFailed,
    NameError: Neo4jOperationFailed,
    RuntimeError: Neo4jOperationFailed
    
    }


