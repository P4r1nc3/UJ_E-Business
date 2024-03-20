package controllers

import javax.inject._
import play.api.mvc._
import models.Product

import scala.collection.mutable.ListBuffer
import play.api.libs.json.{Json, OFormat}

object ProductController {
  val products = ListBuffer[Product]()
}

@Singleton
class ProductController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  import ProductController.products
  private var nextId: Long = 1
  implicit val productFormat: OFormat[Product] = Json.format[Product]

  def categoryExists(categoryId: Long): Boolean = {
    CategoryController.categories.exists(_.id == categoryId)
  }

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
        val categoryId = (json \ "categoryId").as[Long]
        if (!categoryExists(categoryId)) {
          BadRequest(s"Category with ID $categoryId does not exist.")
        } else {
          val name = (json \ "name").as[String]
          val description = (json \ "description").as[String]
          val price = (json \ "price").as[BigDecimal]
          val newProduct = Product(id = nextId, categoryId = categoryId, name = name, description = description, price = price)
          products += newProduct
          nextId += 1
          Created(Json.toJson(newProduct))
        }
      case None => BadRequest("Invalid product data")
    }
  }

  def update(id: Long): Action[AnyContent] = Action { request =>
    request.body.asJson match {
      case Some(json) =>
        val categoryIdOpt = (json \ "categoryId").asOpt[Long]
        val categoryIdValid = categoryIdOpt.forall(categoryExists)
        if (!categoryIdValid) {
          BadRequest("Invalid categoryId: category does not exist")
        } else {
          val nameOpt = (json \ "name").asOpt[String]
          val descriptionOpt = (json \ "description").asOpt[String]
          val priceOpt = (json \ "price").asOpt[BigDecimal]

          products.indexWhere(_.id == id) match {
            case idx if idx != -1 =>
              val productToUpdate = products(idx)
              val updatedProduct = productToUpdate.copy(
                categoryId = categoryIdOpt.getOrElse(productToUpdate.categoryId),
                name = nameOpt.getOrElse(productToUpdate.name),
                description = descriptionOpt.getOrElse(productToUpdate.description),
                price = priceOpt.getOrElse(productToUpdate.price)
              )
              products.update(idx, updatedProduct)
              Ok(Json.toJson(updatedProduct))
            case _ => NotFound
          }
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
