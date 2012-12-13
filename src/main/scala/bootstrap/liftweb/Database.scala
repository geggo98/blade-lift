package bootstrap.liftweb

import net.liftweb.db.{ConnectionIdentifier, ConnectionManager}
import org.h2.jdbcx.JdbcConnectionPool
import net.liftweb.common.{Logger, Full, Box}
import java.sql.Connection

abstract trait JDBCDatabaseWithPooling {
  this : ServerManager =>

  val jdbcConnectionUri : String
  val jdbcUserName : String
  val jdbcPassword : String

  private lazy val _connectionManager = new JdbcConnectionManagerWithPooling(jdbcConnectionUri, jdbcUserName, jdbcPassword)
  def databaseConnectionManager() = _connectionManager
}

trait H2TransientDatabase {
  this : JDBCDatabaseWithPooling =>
  val jdbcConnectionUri = "jdbc:h2:mem:test"
  val jdbcUserName="sa"
  val jdbcPassword="sa"
}

trait H2PersistentDatabase {
  this : JDBCDatabaseWithPooling =>
  val jdbcConnectionUri = "jdbc:h2:target/database/H2"
  val jdbcUserName="sa"
  val jdbcPassword="sa"
}

/**
 *  
 *
 * User: stefan.schwetschke@googlemail.com
 * Date: 03.12.12
 * Time: 18:14
 *
 */
class JdbcConnectionManagerWithPooling(uri : String, userName : String, password : String) extends DatabaseConnectionManager with Logger {
  info("Create connection pool for JDBC URI %s".format(uri))
  private val _pool=JdbcConnectionPool.create(uri, userName, password)

  def newConnection(name: ConnectionIdentifier): Box[Connection] = {
    debug("Get database connection from pool")
    val connection=_pool.getConnection
    connection.setAutoCommit(false)
    Full(connection)
  }

  def releaseConnection(conn: Connection) {
    debug("Closing database connection")
    conn.close()
  }

  def closeAll() {
    info("Closing all database connections.")
    _pool.dispose()
  }
}
