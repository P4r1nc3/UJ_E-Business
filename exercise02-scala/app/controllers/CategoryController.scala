package controllers

import javax.inject._
import play.api.mvc._
import models.Category
import scala.collection.mutable.ListBuffer
import play.api.libs.json.{Json, OFormat}

object CategoryController {
  val categories = ListBuffer[Category]()
}

@Singleton
class CategoryController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  import CategoryController.categories

  implicit val categoryFormat: OFormat[Category] = Json.format[Category]

  def list(): Action[AnyContent] = Action {
    Ok(Json.toJson(categories))
  }

  def get(id: Long): Action[AnyContent] = Action {
    categories.find(_.id == id) match {
      case Some(category) => Ok(Json.toJson(category))
      case None => NotFound
    }
  }

  def create(): Action[AnyContent] = Action { request =>
    request.body.asJson match {
      case Some(json) =>
        val name = (json \ "name").as[String]
        val newId = if (categories.nonEmpty) categories.map(_.id).max + 1 else 1
        val newCategory = Category(id = newId, name = name)
        categories += newCategory
        Created(Json.toJson(newCategory))
      case None => BadRequest("Invalid category data")
    }
  }

  def update(id: Long): Action[AnyContent] = Action { request =>
    request.body.asJson match {
      case Some(json) =>
        val nameOpt = (json \ "name").asOpt[String]
        categories.indexWhere(_.id == id) match {
          case idx if idx != -1 =>
            val updatedCategory = categories(idx).copy(name = nameOpt.getOrElse(categories(idx).name))
            categories.update(idx, updatedCategory)
            Ok(Json.toJson(updatedCategory))
          case _ => NotFound
        }
      case None => BadRequest("Invalid category data")
    }
  }

  def delete(id: Long): Action[AnyContent] = Action {
    categories.indexWhere(_.id == id) match {
      case idx if idx != -1 =>
        categories.remove(idx)
        NoContent
      case _ => NotFound
    }
  }
}
