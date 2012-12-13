package bootstrap.liftweb

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.webapp.WebAppContext
import java.net.ServerSocket
import java.io.IOException

/**
 *  
 *
 * User: stefan.schwetschke@googlemail.com
 * Date: 13.12.12
 * Time: 18:52
 *
 */
abstract trait JettyServer {
  this : ServerManager =>

  private var _port : Option[Int]= None
  private var _server : Option[Server] = None

  def serverUrl = _port match {
    case Some(port) => "http://localhost:%s/".format(port)
  }

  def startServer(port : Int) {
    _port=Some(port)
    val server  = new Server(port)
    _server=Some(server)
    server.setHandler(new ContextHandlerCollection)
    val context = new WebAppContext()
    context.setServer(server)
    context.setContextPath("/")
    context.setWar(warPath)
    server.getHandler match {
      case handler : ContextHandlerCollection => handler.addHandler(context)
    }
    server.start()
  }

  def stopServer() {
    info("Stopping the server")
    _server match {
      case Some(server) => server.stop()
      case None => warn("You must start the server before stopping it")
    }
    databaseConnectionManager().closeAll()
  }
}

trait StartServerWithProductionPort  {
  this : ServerManager =>
  def startServer() : Unit = startServer(80)
}

trait StartServerWithDeveloperPort  {
  this : ServerManager =>
  def startServer() : Unit = startServer(8081)
}

trait StartServerWithRandomPort  {
  this : ServerManager =>
  def startServer() {
    var run=1
    var started=false
    while(!started && run<=3) {
      try{
        info("Try to start server, trial %s".format(run))
        debug("Creating socket with port given by the system")
        val socket=new ServerSocket(0)
        val port=socket.getLocalPort
        debug("Closing port")
        socket.close()
        debug("Trying port %s".format(port))
        startServer(port)
        started=true
      }catch{
        case e : IOException =>
          info("Could not start server in try %s".format(run),e)
          run=run+1
      }
    }
  }
}
