package controllers

import javax.inject._
import play.api.mvc._
import models.Category
import scala.collection.mutable.ListBuffer
import play.api.libs.json.{Json, OFormat}

@Singleton
class CategoryController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  private val categories = ListBuffer[Category]()
  private var nextId: Long = 1
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
        val newCategory = Category(id = nextId, name = name)
        categories += newCategory
        nextId += 1
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
            val categoryToUpdate = categories(idx)
            val updatedCategory = categoryToUpdate.copy(
              name = nameOpt.getOrElse(categoryToUpdate.name)
            )
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
