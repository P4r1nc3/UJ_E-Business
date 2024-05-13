package handlers

import (
	"github.com/labstack/echo/v4"
	"gobackend/models"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

const invalidCartIDError = "Invalid cart ID"
const invalidProductIDError = "Invalid product ID"

const cartProductQuery = "cart_id = ? AND product_id = ?"

func CreateCart(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)

	newCart := models.Cart{}
	if err := db.Create(&newCart).Error; err != nil {
		return c.JSON(http.StatusInternalServerError, echo.Map{"error": "Could not create cart"})
	}

	return c.JSON(http.StatusCreated, newCart)
}

func GetCart(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	cartIdParam := c.Param("cartId")

	cartId, err := strconv.ParseUint(cartIdParam, 10, 32)
	if err != nil {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": invalidCartIDError})
	}

	var cart models.Cart
	if err := db.Preload("Products.Product").First(&cart, cartId).Error; err != nil {
		return c.JSON(http.StatusNotFound, echo.Map{"error": "Cart not found"})
	}

	return c.JSON(http.StatusOK, cart)
}

func DeleteCart(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	cartIdParam := c.Param("cartId")

	cartId, err := strconv.ParseUint(cartIdParam, 10, 32)
	if err != nil {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": invalidCartIDError})
	}

	if err := db.Where("cart_id = ?", cartId).Delete(&models.Cart{}).Error; err != nil {
		return c.JSON(http.StatusInternalServerError, echo.Map{"error": "Could not delete cart"})
	}

	return c.JSON(http.StatusOK, echo.Map{"message": "Cart deleted successfully"})
}

func AddProductToCart(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)

	cartIdParam := c.Param("cartId")
	productIdParam := c.Param("productId")

	cartId, err := strconv.ParseUint(cartIdParam, 10, 32)
	if err != nil {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": invalidCartIDError})
	}

	productId, err := strconv.ParseUint(productIdParam, 10, 32)
	if err != nil {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": invalidProductIDError})
	}

	var product models.Product
	if err := db.First(&product, productId).Error; err != nil {
		return c.JSON(http.StatusNotFound, echo.Map{"error": "Product not found"})
	}
	if !product.Available {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": "Product is not available"})
	}

	var cartProduct models.CartProduct
	if err := db.Where(cartProductQuery, cartId, productId).First(&cartProduct).Error; err == gorm.ErrRecordNotFound {
		cartProduct = models.CartProduct{
			CartID:    uint(cartId),
			ProductID: uint(productId),
			Quantity:  1,
			Price:     product.Price,
		}
		cartProduct.Price *= float64(cartProduct.Quantity)

		if err := db.Create(&cartProduct).Error; err != nil {
			return c.JSON(http.StatusInternalServerError, echo.Map{"error": "Could not add product to cart"})
		}
	} else {
		newQuantity := cartProduct.Quantity + 1
		cartProduct.Quantity = newQuantity
		cartProduct.Price = float64(newQuantity) * product.Price

		if err := db.Save(&cartProduct).Error; err != nil {
			return c.JSON(http.StatusInternalServerError, echo.Map{"error": "Could not update product quantity in cart"})
		}
	}

	return c.JSON(http.StatusOK, echo.Map{"message": "Product added/updated in cart successfully"})
}

func UpdateProductInCart(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)

	cartIdParam := c.Param("cartId")
	productIdParam := c.Param("productId")
	quantityParam := c.QueryParam("quantity")

	cartId, err := strconv.ParseUint(cartIdParam, 10, 32)
	if err != nil {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": invalidCartIDError})
	}

	productId, err := strconv.ParseUint(productIdParam, 10, 32)
	if err != nil {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": invalidProductIDError})
	}

	quantity, err := strconv.Atoi(quantityParam)
	if err != nil || quantity < 0 {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": "Invalid quantity"})
	}

	var product models.Product
	if err := db.First(&product, productId).Error; err != nil {
		return c.JSON(http.StatusNotFound, echo.Map{"error": "Product not found"})
	}

	if quantity == 0 {
		if err := db.Where(cartProductQuery, cartId, productId).Delete(&models.CartProduct{}).Error; err != nil {
			return c.JSON(http.StatusInternalServerError, echo.Map{"error": "Could not delete product from cart"})
		}
	} else {
		var cartProduct models.CartProduct
		if err := db.Where(cartProductQuery, cartId, productId).First(&cartProduct).Error; err == gorm.ErrRecordNotFound {
			cartProduct = models.CartProduct{
				CartID:    uint(cartId),
				ProductID: uint(productId),
				Quantity:  quantity,
				Price:     product.Price,
			}
		} else {
			cartProduct.Quantity = quantity
		}
		cartProduct.Price = float64(cartProduct.Quantity) * product.Price

		if err := db.Save(&cartProduct).Error; err != nil {
			return c.JSON(http.StatusInternalServerError, echo.Map{"error": "Could not update product in cart"})
		}
	}

	return c.JSON(http.StatusOK, echo.Map{"message": "Product quantity updated successfully"})
}

func DeleteProductFromCart(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	cartIdParam := c.Param("cartId")
	productIdParam := c.Param("productId")

	cartId, err := strconv.ParseUint(cartIdParam, 10, 32)
	if err != nil {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": invalidCartIDError})
	}

	productId, err := strconv.ParseUint(productIdParam, 10, 32)
	if err != nil {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": invalidProductIDError})
	}

	if err := db.Where(cartProductQuery, cartId, productId).Delete(&models.CartProduct{}).Error; err != nil {
		return c.JSON(http.StatusInternalServerError, echo.Map{"error": "Could not delete product from cart"})
	}

	return c.JSON(http.StatusOK, echo.Map{"message": "Product deleted from cart successfully"})
}
