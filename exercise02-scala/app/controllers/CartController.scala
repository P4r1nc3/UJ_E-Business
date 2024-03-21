package controllers

import javax.inject._
import play.api.mvc._
import models.{Cart, CartItem}
import scala.collection.mutable.ListBuffer
import play.api.libs.json.{Json, OFormat, Reads, Writes}

object CartController {
  val carts = ListBuffer[Cart]()
  implicit val cartItemFormat: OFormat[CartItem] = Json.format[CartItem]
  implicit val cartFormat: OFormat[Cart] = Json.format[Cart]
}

@Singleton
class CartController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  import CartController._
  var nextId: Long = 1

  def list(): Action[AnyContent] = Action {
    Ok(Json.toJson(carts))
  }

  def get(id: Long): Action[AnyContent] = Action {
    carts.find(_.id == id) match {
      case Some(cart) => Ok(Json.toJson(cart))
      case None => NotFound
    }
  }

  def create(): Action[AnyContent] = Action { request =>
    request.body.asJson match {
      case Some(json) =>
        val items = (json \ "items").as[List[CartItem]]
        if (items.forall(item => ProductController.products.exists(_.id == item.productId))) {
          val newCart = Cart(nextId, items)
          carts += newCart
          nextId += 1
          Created(Json.toJson(newCart))
        } else {
          BadRequest("One or more products do not exist")
        }
      case None => BadRequest("Invalid cart data")
    }
  }

  def update(id: Long): Action[AnyContent] = Action { request =>
    request.body.asJson match {
      case Some(json) =>
        val itemsOpt = (json \ "items").asOpt[List[CartItem]]
        carts.indexWhere(_.id == id) match {
          case idx if idx != -1 && itemsOpt.isDefined &&
            itemsOpt.get.forall(item => ProductController.products.exists(_.id == item.productId)) =>
            val updatedCart = carts(idx).copy(items = itemsOpt.get)
            carts.update(idx, updatedCart)
            Ok(Json.toJson(updatedCart))
          case _ => NotFound
        }
      case None => BadRequest("Invalid cart data")
    }
  }

  def delete(id: Long): Action[AnyContent] = Action {
    carts.indexWhere(_.id == id) match {
      case idx if idx != -1 =>
        carts.remove(idx)
        NoContent
      case _ => NotFound
    }
  }
}
