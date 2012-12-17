import bootstrap.liftweb.ServerManager

/**
 *  
 *
 * User: stefan.schwetschke@googlemail.com
 * Date: 03.12.12
 * Time: 16:47
 *
 */
object StartServer extends App {
  val manager=ServerManager.configuration.vend

  manager.startServer()

  Seq("Started Jetty on %s" format(manager.serverUrl), "Press ENTER to exit").foreach (Console.println _)
  Console.readLine()

  manager.stopServer()
}
