package de.schwetschke.bna2.tools

import org.specs2.mutable.Specification
import org.specs2.matcher.{Expectable, Matcher}

/**
 *  
 *
 * User: stefan.schwetschke@googlemail.com
 * Date: 14.12.12
 * Time: 13:07
 *
 */
class PasswordGeneratorTest extends Specification {
  "The generated password" should {
    "contain the given number of letters" in {
      GeneratePassword.generate(11).length must beEqualTo(11)
    }
    "contain lower case and upper case letters" in {
      GeneratePassword.generate(100) must containLetter(_.isLower, "lowercase") and containLetter(_.isUpper, "uppercase")
    }

   "be unique" in {
     (1 to 100).map(_ => GeneratePassword.generate(5)).groupBy(x => x) must haveLength(100)
   }
  }
}

case class containLetter(f : Char => Boolean, description : String) extends Matcher[String]() {
  override def apply[S <: String](t: Expectable[S]) =
    result(t.value.toSet.exists(f), "Contains %s letter".format(description), "Does not contain %s letter".format(description), t)
}
