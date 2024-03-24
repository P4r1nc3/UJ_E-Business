package handlers

import (
	"github.com/labstack/echo/v4"
	"gobackend/models"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

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
		return c.JSON(http.StatusBadRequest, echo.Map{"error": "Invalid cart ID"})
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
		return c.JSON(http.StatusBadRequest, echo.Map{"error": "Invalid cart ID"})
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
		return c.JSON(http.StatusBadRequest, echo.Map{"error": "Invalid cart ID"})
	}

	productId, err := strconv.ParseUint(productIdParam, 10, 32)
	if err != nil {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": "Invalid product ID"})
	}

	var product models.Product
	if err := db.First(&product, productId).Error; err != nil {
		return c.JSON(http.StatusNotFound, echo.Map{"error": "Product not found"})
	}
	if !product.Available {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": "Product is not available"})
	}

	var cartProduct models.CartProduct
	if err := db.Where("cart_id = ? AND product_id = ?", cartId, productId).First(&cartProduct).Error; err == gorm.ErrRecordNotFound {
		// Produkt nie jest w koszyku, więc dodajemy go
		cartProduct = models.CartProduct{
			CartID:    uint(cartId),
			ProductID: uint(productId),
			Quantity:  1,
			Price:     product.Price, // Cena za jedną sztukę, bez mnożenia przez ilość
		}
		if err := db.Create(&cartProduct).Error; err != nil {
			return c.JSON(http.StatusInternalServerError, echo.Map{"error": "Could not add product to cart"})
		}
	} else {
		// Produkt jest już w koszyku, zwiększamy ilość i aktualizujemy cenę
		newQuantity := cartProduct.Quantity + 1
		cartProduct.Quantity = newQuantity
		if err := db.Save(&cartProduct).Error; err != nil {
			return c.JSON(http.StatusInternalServerError, echo.Map{"error": "Could not update product quantity in cart"})
		}
	}

	// Rekalkulacja i aktualizacja całkowitej wartości koszyka
	var total float64
	var cartProducts []models.CartProduct
	db.Where("cart_id = ?", cartId).Find(&cartProducts)
	for _, cp := range cartProducts {
		total += cp.Price * float64(cp.Quantity)
	}
	if err := db.Model(&models.Cart{}).Where("id = ?", cartId).Update("total", total).Error; err != nil {
		return c.JSON(http.StatusInternalServerError, echo.Map{"error": "Could not update cart total"})
	}

	return c.JSON(http.StatusOK, echo.Map{"message": "Product added/updated in cart successfully"})
}

func DeleteProductFromCart(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	cartIdParam := c.Param("cartId")
	productIdParam := c.Param("productId")

	cartId, err := strconv.ParseUint(cartIdParam, 10, 32)
	if err != nil {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": "Invalid cart ID"})
	}

	productId, err := strconv.ParseUint(productIdParam, 10, 32)
	if err != nil {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": "Invalid product ID"})
	}

	if err := db.Where("cart_id = ? AND product_id = ?", cartId, productId).Delete(&models.CartProduct{}).Error; err != nil {
		return c.JSON(http.StatusInternalServerError, echo.Map{"error": "Could not delete product from cart"})
	}

	var total float64
	db.Model(&models.CartProduct{}).Where("cart_id = ?", cartId).Select("sum(price * quantity)").Row().Scan(&total)
	db.Model(&models.Cart{}).Where("id = ?", cartId).Update("total", total)

	return c.JSON(http.StatusOK, echo.Map{"message": "Product deleted from cart successfully"})
}
