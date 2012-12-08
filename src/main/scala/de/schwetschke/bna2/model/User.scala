package de.schwetschke.bna2.model

import net.liftweb.mapper._

/**
 * Entity class for users
 */
class User extends MegaProtoUser[User] {
  override def getSingleton = User
  def isAdmin : Boolean = superUser.is
}

/**
 * Provides the meta model for User together with the self administration features for each user
 */
object User extends User with MetaMegaProtoUser[User] {
  override def dbTableName = "users"

  def currentUserIsAdmin : Boolean = currentUser.dmap(false)(_ isAdmin)
}

/**
 * Again the meta model for User, but this time without self administration but with the CRUD view for the administrator
 */
object UserAdministration extends User with KeyedMetaMapper[Long, User] with ProtoUser[User] with CRUDify[Long, User] {
  override def dbTableName = User.dbTableName
}
