import controllers.CategoryController
import models.Category
import org.scalatestplus.play._
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json

class CategoryControllerSpec extends PlaySpec with Results with MockitoSugar {

  val mockControllerComponents = Helpers.stubControllerComponents()

  "CategoryController#list" should {
    "return an OK status with a JSON list of categories" in {
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
    }
  }

  "CategoryController#get" should {
    "return an OK status with the category JSON for an existing category ID" in {
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
    }
  }

  "CategoryController#create" should {
    "create a new category and return a Created status with the category JSON" in {
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
    }
  }

  "CategoryController#update" should {
    "update an existing category and return an OK status with the updated category JSON" in {
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
    }
  }

  "CategoryController#delete" should {
    "delete an existing category and return a NoContent status" in {
      // Arrange
      val categoryId = 3L
      val categoryController = new CategoryController(mockControllerComponents)
      CategoryController.categories.clear()
      CategoryController.categories += Category(categoryId, "Delete Category")

      // Act
      val result = categoryController.delete(categoryId).apply(FakeRequest(DELETE, s"/categories/$categoryId"))

      // Assert
      status(result) mustBe NO_CONTENT
    }
  }
}

