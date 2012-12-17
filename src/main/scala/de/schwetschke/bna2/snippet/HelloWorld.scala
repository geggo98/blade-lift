package de.schwetschke.bna2
package snippet

import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import de.schwetschke.bna2.lib._
import Helpers._

class HelloWorld {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  // replace the contents of the element with id "time" with the date
  def howdy = "#time *" #> date.map(_.toString)

  /*
   lazy val date: Date = TestDependencyFactory.time.vend // create the date via factory

   def howdy = "#time *" #> date.toString
   */
}
