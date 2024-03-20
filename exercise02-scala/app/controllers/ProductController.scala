package controllers

import javax.inject._
import play.api.mvc._
import models.Product
import scala.collection.mutable.ListBuffer
import play.api.libs.json.{Json, OFormat}

@Singleton
class ProductController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  private val products = ListBuffer[Product]()
  implicit val productFormat: OFormat[Product] = Json.format[Product]

  def list(): Action[AnyContent] = Action {
    if (products.isEmpty) NoContent else Ok(Json.toJson(products))
  }

  def get(id: Long): Action[AnyContent] = Action {
    products.find(_.id == id) match {
      case Some(product) => Ok(Json.toJson(product))
      case None => NotFound
    }
  }

  def create(): Action[AnyContent] = Action { request =>
    val json = request.body.asJson.get
    val product = json.as[Product]
    products += product
    Created(s"Product created with id: ${product.id}")
  }

  def update(id: Long): Action[AnyContent] = Action { request =>
    val json = request.body.asJson.get
    val newProduct = json.as[Product]
    products.indexWhere(_.id == id) match {
      case idx if idx != -1 =>
        products.update(idx, newProduct)
        Ok(s"Product with id $id updated.")
      case _ => NotFound
    }
  }

  def delete(id: Long): Action[AnyContent] = Action {
    products.indexWhere(_.id == id) match {
      case idx if idx != -1 =>
        products.remove(idx)
        Ok(s"Product with id $id deleted.")
      case _ => NotFound
    }
  }
}
