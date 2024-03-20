package controllers

import javax.inject._
import play.api.mvc._
import models.Product
import scala.collection.mutable.ListBuffer
import play.api.libs.json.{Json, OFormat}

@Singleton
class ProductController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  private val products = ListBuffer[Product]()
  private var nextId: Long = 1
  implicit val productFormat: OFormat[Product] = Json.format[Product]

  def list(): Action[AnyContent] = Action {
    Ok(Json.toJson(products))
  }

  def get(id: Long): Action[AnyContent] = Action {
    products.find(_.id == id) match {
      case Some(product) => Ok(Json.toJson(product))
      case None => NotFound
    }
  }

  def create(): Action[AnyContent] = Action { request =>
    request.body.asJson match {
      case Some(json) =>
        val name = (json \ "name").as[String]
        val categoryId = (json \ "categoryId").as[Long]
        val newProduct = Product(id = nextId, name = name, categoryId = categoryId)
        products += newProduct
        nextId += 1
        Created(Json.toJson(newProduct))
      case None => BadRequest("Invalid product data")
    }
  }

  def update(id: Long): Action[AnyContent] = Action { request =>
    request.body.asJson match {
      case Some(json) =>
        val name = (json \ "name").as[String]
        val categoryId = (json \ "categoryId").as[Long]
        products.indexWhere(_.id == id) match {
          case idx if idx != -1 =>
            val updatedProduct = products(idx).copy(name = name, categoryId = categoryId)
            products.update(idx, updatedProduct)
            Ok(Json.toJson(updatedProduct))
          case _ => NotFound
        }
      case None => BadRequest("Invalid product data")
    }
  }

  def delete(id: Long): Action[AnyContent] = Action {
    products.indexWhere(_.id == id) match {
      case idx if idx != -1 =>
        products.remove(idx)
        NoContent
      case _ => NotFound
    }
  }
}
