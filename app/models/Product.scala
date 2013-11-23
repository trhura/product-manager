package models

import play.api.Play
import play.api.Play.current

import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import mongoContext._

/*
Adding a custom Salat context to work with Play's Classloader
Using example from:
https://github.com/leon/play-salat/blob/master/sample/app/models/mongoContext.scala
*/
package object mongoContext {
  implicit val context = {
    val context = new Context {
      val name = "global"
      override val typeHintStrategy = StringTypeHintStrategy(when = TypeHintFrequency.WhenNecessary, typeHint = "_t")
    }
    //context.registerGlobalKeyOverride(remapThis = "id", toThisInstead = "_id")
    context.registerClassLoader(Play.classloader)
    context
  }
}

case class Pricing
(
  cost: Double,
  price: Double,
  promo_price: Double,
  savings: Int,
  on_sale: Int
)

case class Product
(
  @Key("_id") _id: org.bson.types.ObjectId,
  id: Int,
  title: String,
  pricing: Pricing
)

object Product extends ModelCompanion[Product, ObjectId] {
  val dao = new SalatDAO[Product, ObjectId](collection = mongoCollection("products")) {}

  def findOneById(id: Long): Option[Product] = dao.findOne(MongoDBObject("id" -> id))
  //def findByTitle(title: String) = dao.findOne(MongoDBObject("title" -> title))
}
