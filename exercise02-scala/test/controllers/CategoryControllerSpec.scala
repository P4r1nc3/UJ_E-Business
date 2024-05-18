import controllers.CategoryController
import models.Category
import org.scalatestplus.play._
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.{JsValue, Json}

class CategoryControllerSpec extends PlaySpec with Results with MockitoSugar {

  val mockControllerComponents = Helpers.stubControllerComponents()

  "CategoryController#list" should {
    "return an OK status with a JSON list of categories that includes all category details" in {
      // Arrange
      val categoryController = new CategoryController(mockControllerComponents)
      CategoryController.categories.clear()
      CategoryController.categories += Category(1, "Test Category")

      // Act
      val result = categoryController.list().apply(FakeRequest(GET, "/categories"))

      // Assert
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("Test Category")
      val json: JsValue = contentAsJson(result)
      (json(0) \ "id").as[Long] mustBe 1
      (json(0) \ "name").as[String] mustBe "Test Category"
    }
  }

  "CategoryController#get" should {
    "return an OK status with the category JSON for an existing category ID that matches expected values" in {
      // Arrange
      val categoryId = 1L
      val categoryController = new CategoryController(mockControllerComponents)
      CategoryController.categories.clear()
      CategoryController.categories += Category(categoryId, "Get Category")

      // Act
      val result = categoryController.get(categoryId).apply(FakeRequest(GET, s"/categories/$categoryId"))

      // Assert
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("Get Category")
      val categoryJson: JsValue = contentAsJson(result)
      (categoryJson \ "id").as[Long] mustBe categoryId
      (categoryJson \ "name").as[String] mustBe "Get Category"
    }
  }

  "CategoryController#create" should {
    "create a new category and return a Created status with the category JSON that confirms the category details" in {
      // Arrange
      val categoryController = new CategoryController(mockControllerComponents)
      CategoryController.categories.clear()
      val newCategoryJson = Json.obj("name" -> "Create Category")

      // Act
      val fakeRequest = FakeRequest(POST, "/categories")
        .withHeaders("Content-Type" -> "application/json")
        .withJsonBody(newCategoryJson)
      val result = categoryController.create().apply(fakeRequest)

      // Assert
      status(result) mustBe CREATED
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("Create Category")
      val categoryJson: JsValue = contentAsJson(result)
      (categoryJson \ "name").as[String] mustBe "Create Category"
      (categoryJson \ "id").as[Long] must be > 0L
    }
  }

  "CategoryController#update" should {
    "update an existing category and return an OK status with the updated category JSON reflecting changes" in {
      // Arrange
      val categoryId = 2L
      val categoryController = new CategoryController(mockControllerComponents)
      CategoryController.categories.clear()
      CategoryController.categories += Category(categoryId, "Update Category")
      val updateJson = Json.obj("name" -> "Updated Category")

      // Act
      val result = categoryController.update(categoryId).apply(FakeRequest(PUT, s"/categories/$categoryId").withJsonBody(updateJson))

      // Assert
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("Updated Category")
      val categoryJson: JsValue = contentAsJson(result)
      (categoryJson \ "name").as[String] mustBe "Updated Category"
    }
  }

  "CategoryController#delete" should {
    "delete an existing category and return a NoContent status while ensuring the category is removed from the list" in {
      // Arrange
      val categoryId = 3L
      val categoryController = new CategoryController(mockControllerComponents)
      CategoryController.categories.clear()
      CategoryController.categories += Category(categoryId, "Delete Category")

      // Act
      val result = categoryController.delete(categoryId).apply(FakeRequest(DELETE, s"/categories/$categoryId"))

      // Assert
      status(result) mustBe NO_CONTENT
      CategoryController.categories.exists(_.id == categoryId) mustBe false
    }
  }
}
