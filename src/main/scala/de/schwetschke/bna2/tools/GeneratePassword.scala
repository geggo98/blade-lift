package de.schwetschke.bna2.tools

import scala.util.control.Breaks._
import scala.util.Random

/**
 * Source: http://www.multicians.org/thvv/Gpw.java
 *
 * User: stefan.schwetschke@googlemail.com
 * Date: 13.12.12
 * Time: 22:20
 *
 */
object GeneratePassword {
  val data=new GpwData
  val ran=new Random

  def generate(length : Int) : String = {
    val alphabet="abcdefghijklmnopqrstuvwxyz"
    def randomToUpper = {c : Char => if (ran.nextInt(4)==0) c.toLower else c.toUpper}

    val password = new StringBuilder(length)
    val pik = ran.nextDouble(); // random number [0,1]
    val random1 = (pik * data.getSigma) // weight by sum of frequencies
    var sum = 0
    breakable {
      for (c1 <- (0 until  26)) {
        for (c2 <- (0 until  26)) {
          for (c3 <- (0 until  26)) {
            sum += data.get(c1, c2, c3).toInt
            if (sum > random1) {
              password.append(alphabet.charAt(c1))
              password.append(alphabet.charAt(c2))
              password.append(alphabet.charAt(c3))
              break()
            } // if sum
          } // for c3
        } // for c2
      } // for c1
    }
    // Now do a random walk.
    var nchar = 3
    while (nchar < length) {
      val c1 = alphabet.indexOf(password.charAt(nchar-2).toLower)
      val c2 = alphabet.indexOf(password.charAt(nchar-1).toLower)
      sum = 0
      breakable {
        for (c3 <- (0 until 26))
        sum += data.get(c1, c2, c3).toInt
        if (sum == 0) {
          break()	// exit while loop
        }
      }
      val pik = ran.nextDouble()
      val random2 = (pik * sum)
      sum = 0
      breakable {
        for (c3 <- (0 until 26)) {
          sum += data.get(c1, c2, c3).toInt
          if (sum > random2) {
            password.append(randomToUpper(alphabet.charAt(c3)))
            break()
          } // if sum
        } // for c3
      }
       nchar = nchar+1
    } // while nchar
    password.toString()
  }
}
