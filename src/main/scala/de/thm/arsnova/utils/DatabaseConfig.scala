package de.thm.arsnova.utils

trait DatabaseConfig extends Config{
  val driver = slick.driver.MySQLDriver

  import driver.api._

  def db: Database = Database.forConfig("database")

  implicit val session: Session = db.createSession()
}
