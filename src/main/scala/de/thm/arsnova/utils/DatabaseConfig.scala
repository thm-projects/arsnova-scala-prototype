package de.thm.arsnova.utils

trait DatabaseConfig extends Config{
  val driver = slick.driver.MySQLDriver

  import driver.api._

  val db: Database = Database.forConfig("database")
}
