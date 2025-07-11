import controllers.{CategoryController, ProductController}
import models.{Category, Product}
import org.scalatestplus.play._
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.{JsValue, Json}

class ProductControllerSpec extends PlaySpec with Results with MockitoSugar {

  val mockControllerComponents = Helpers.stubControllerComponents()

  "ProductController#list" should {
    "return an OK status with a JSON list of products that contains all product details" in {
      // Arrange
      val productController = new ProductController(mockControllerComponents)
      ProductController.products.clear()
      ProductController.products += Product(1, 1, "Test Product", "A product for testing purposes", 99.99)

      // Act
      val result = productController.list().apply(FakeRequest(GET, "/products"))

      // Assert
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("Test Product")
      (contentAsJson(result) \ 0 \ "name").as[String] mustBe "Test Product"
      (contentAsJson(result) \ 0 \ "description").as[String] mustBe "A product for testing purposes"
      (contentAsJson(result) \ 0 \ "price").as[BigDecimal] mustBe 99.99
    }
  }

  "ProductController#get" should {
    "return an OK status with the product JSON for an existing product ID that matches expected values" in {
      // Arrange
      val productId = 1L
      val productController = new ProductController(mockControllerComponents)
      ProductController.products.clear()
      ProductController.products += Product(productId, 1, "Get Product", "A product for getting", 50.00)

      // Act
      val result = productController.get(productId).apply(FakeRequest(GET, s"/products/$productId"))

      // Assert
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("Get Product")
      val productJson: JsValue = contentAsJson(result)
      (productJson \ "id").as[Long] mustBe productId
      (productJson \ "categoryId").as[Long] mustBe 1
      (productJson \ "name").as[String] mustBe "Get Product"
      (productJson \ "description").as[String] mustBe "A product for getting"
      (productJson \ "price").as[BigDecimal] mustBe 50.00
    }
  }

  "ProductController#create" should {
    "create a new product and return a Created status with the product JSON that confirms the product details" in {
      // Arrange
      CategoryController.categories += Category(1, "Test Category")
      val productController = new ProductController(mockControllerComponents)
      ProductController.products.clear()
      val newProductJson = Json.obj(
        "categoryId" -> 1,
        "name" -> "Create Product",
        "description" -> "A new product for creation test",
        "price" -> 75.99
      )

      // Act
      val fakeRequest = FakeRequest(POST, "/products")
        .withHeaders("Content-Type" -> "application/json")
        .withJsonBody(newProductJson)
      val result = productController.create().apply(fakeRequest)

      // Assert
      status(result) mustBe CREATED
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("Create Product")
      val productJson: JsValue = contentAsJson(result)
      (productJson \ "name").as[String] mustBe "Create Product"
      (productJson \ "description").as[String] mustBe "A new product for creation test"
      (productJson \ "price").as[BigDecimal] mustBe 75.99
    }
  }

  "ProductController#update" should {
    "update an existing product and return an OK status with the updated product JSON reflecting changes" in {
      // Arrange
      val productId = 2L
      val productController = new ProductController(mockControllerComponents)
      ProductController.products.clear()
      ProductController.products += Product(productId, 1, "Update Product", "A product before update", 20.00)
      val updateJson = Json.obj(
        "name" -> "Updated Product",
        "description" -> "A product after update",
        "price" -> 99.99
      )

      // Act
      val result = productController.update(productId).apply(FakeRequest(PUT, s"/products/$productId").withJsonBody(updateJson))

      // Assert
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("Updated Product")
      val productJson: JsValue = contentAsJson(result)
      (productJson \ "name").as[String] mustBe "Updated Product"
      (productJson \ "description").as[String] mustBe "A product after update"
      (productJson \ "price").as[BigDecimal] mustBe 99.99
    }
  }

  "ProductController#delete" should {
    "delete an existing product and return a NoContent status while ensuring the product is removed from the list" in {
      // Arrange
      val productId = 3L
      val productController = new ProductController(mockControllerComponents)
      ProductController.products.clear()
      ProductController.products += Product(productId, 1, "Delete Product", "A product to be deleted", 30.00)

      // Act
      val result = productController.delete(productId).apply(FakeRequest(DELETE, s"/products/$productId"))

      // Assert
      status(result) mustBe NO_CONTENT
      ProductController.products.exists(_.id == productId) mustBe false
    }
  }
}
