package bootstrap.liftweb

import net.liftweb.db.{ConnectionIdentifier, ConnectionManager}
import org.h2.jdbcx.JdbcConnectionPool
import net.liftweb.common.{Full, Box}
import java.sql.Connection

/**
 *  
 *
 * User: stefan.schwetschke@googlemail.com
 * Date: 03.12.12
 * Time: 18:14
 *
 */
object Database extends ConnectionManager {
  //val uri="jdbc:h2:target/database/H2" // Persistent database in directory target/database/H2
  val uri="jdbc:h2:mem:test" // Transient, in memory database
  val pool=JdbcConnectionPool.create(uri, "sa", "sa")

  def newConnection(name: ConnectionIdentifier): Box[Connection] = {
    val connection=pool.getConnection
    connection.setAutoCommit(false)
    Full(connection)
  }

  def releaseConnection(conn: Connection) {
    conn.close()
  }

  def closeAll() {
    pool.dispose()
  }
}
