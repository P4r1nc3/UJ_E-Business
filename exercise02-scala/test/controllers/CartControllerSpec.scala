import controllers.{CartController, ProductController}
import models.{Cart, CartItem, Product}
import org.scalatestplus.play._
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.{JsValue, Json}

class CartControllerSpec extends PlaySpec with Results with MockitoSugar {

  val mockControllerComponents = Helpers.stubControllerComponents()

  "CartController#list" should {
    "return an OK status with a JSON list of carts that verifies the cart details" in {
      // Arrange
      val cartController = new CartController(mockControllerComponents)
      CartController.carts.clear()
      CartController.carts += Cart(1, List(CartItem(1, 2)))

      // Act
      val result = cartController.list().apply(FakeRequest(GET, "/carts"))

      // Assert
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      val json: JsValue = contentAsJson(result)
      (json \ 0 \ "id").as[Long] mustBe 1
      (json \ 0 \ "items" \ 0 \ "productId").as[Long] mustBe 1
      (json \ 0 \ "items" \ 0 \ "quantity").as[Int] mustBe 2
    }
  }

  "CartController#get" should {
    "return an OK status with the cart JSON for an existing cart ID and verify the cart contents" in {
      // Arrange
      val cartId = 1L
      val cartController = new CartController(mockControllerComponents)
      CartController.carts.clear()
      CartController.carts += Cart(cartId, List(CartItem(1, 2)))

      // Act
      val result = cartController.get(cartId).apply(FakeRequest(GET, s"/carts/$cartId"))

      // Assert
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      val cartJson: JsValue = contentAsJson(result)
      (cartJson \ "id").as[Long] mustBe cartId
      (cartJson \ "items" \ 0 \ "productId").as[Long] mustBe 1
      (cartJson \ "items" \ 0 \ "quantity").as[Int] mustBe 2
    }
  }

  "CartController#create" should {
    "create a new cart and return a Created status with the cart JSON verifying the cart structure and content" in {
      // Arrange
      val cartController = new CartController(mockControllerComponents)
      CartController.carts.clear()
      ProductController.products += Product(1, 1, "Test Product", "A product for testing purposes", 99.99)
      val newCartJson = Json.obj(
        "items" -> Json.arr(
          Json.obj("productId" -> 1, "quantity" -> 2)
        )
      )

      // Act
      val fakeRequest = FakeRequest(POST, "/carts")
        .withHeaders("Content-Type" -> "application/json")
        .withJsonBody(newCartJson)
      val result = cartController.create().apply(fakeRequest)

      // Assert
      status(result) mustBe CREATED
      contentType(result) mustBe Some("application/json")
      val cartJson: JsValue = contentAsJson(result)
      (cartJson \ "id").as[Long] mustBe 1
      (cartJson \ "items" \ 0 \ "productId").as[Long] mustBe 1
      (cartJson \ "items" \ 0 \ "quantity").as[Int] mustBe 2
    }
  }

  "CartController#update" should {
    "update an existing cart and return an OK status with the updated cart JSON verifying changes" in {
      // Arrange
      val cartId = 2L
      val cartController = new CartController(mockControllerComponents)
      CartController.carts.clear()
      CartController.carts += Cart(cartId, List(CartItem(1, 1)))
      ProductController.products += Product(1, 1, "Another Product", "Another product for testing", 49.99)
      val updateJson = Json.obj(
        "items" -> Json.arr(
          Json.obj("productId" -> 1, "quantity" -> 3)
        )
      )

      // Act
      val result = cartController.update(cartId).apply(FakeRequest(PUT, s"/carts/$cartId").withJsonBody(updateJson))

      // Assert
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      val updatedCartJson: JsValue = contentAsJson(result)
      (updatedCartJson \ "items" \ 0 \ "quantity").as[Int] mustBe 3
    }
  }

  "CartController#delete" should {
    "delete an existing cart and return a NoContent status while ensuring the cart is removed from the list" in {
      // Arrange
      val cartId = 3L
      val cartController = new CartController(mockControllerComponents)
      CartController.carts.clear()
      CartController.carts += Cart(cartId, List(CartItem(1, 2)))

      // Act
      val result = cartController.delete(cartId).apply(FakeRequest(DELETE, s"/carts/$cartId"))

      // Assert
      status(result) mustBe NO_CONTENT
      CartController.carts.exists(_.id == cartId) mustBe false
    }
  }
}
