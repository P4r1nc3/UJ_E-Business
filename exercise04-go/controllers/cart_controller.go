package controllers

import (
	"github.com/labstack/echo/v4"
	"goproject/models"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

func CreateCart(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	var cart models.Cart
	if err := c.Bind(&cart); err != nil {
		return err
	}
	db.Create(&cart)
	return c.JSON(http.StatusCreated, cart)
}

func AddProductToCart(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	cartID := c.Param("cartId")
	productID := c.Param("productId")

	var cart models.Cart
	if err := db.Preload("Products").First(&cart, cartID).Error; err != nil {
		return c.JSON(http.StatusNotFound, "Cart not found")
	}

	var product models.Product
	if err := db.First(&product, productID).Error; err != nil {
		return c.JSON(http.StatusNotFound, "Product not found")
	}

	cart.Products = append(cart.Products, product)
	db.Save(&cart)

	return c.JSON(http.StatusOK, cart)
}

func GetCart(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	cartID := c.Param("cartId")

	var cart models.Cart
	if err := db.Preload("Products").First(&cart, cartID).Error; err != nil {
		return c.JSON(http.StatusNotFound, "Cart not found")
	}

	return c.JSON(http.StatusOK, cart)
}

func DeleteProductFromCart(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	cartID := c.Param("cartId")
	productIDStr := c.Param("productId")

	productID, err := strconv.ParseUint(productIDStr, 10, 64)
	if err != nil {
		return c.JSON(http.StatusBadRequest, "Invalid product ID")
	}

	var cart models.Cart
	if err := db.Preload("Products").First(&cart, cartID).Error; err != nil {
		return c.JSON(http.StatusNotFound, "Cart not found")
	}

	var filteredProducts []models.Product
	for _, product := range cart.Products {
		if product.ID != uint(productID) {
			filteredProducts = append(filteredProducts, product)
		}
	}

	if err := db.Model(&cart).Association("Products").Delete(filteredProducts); err != nil {
		return c.JSON(http.StatusInternalServerError, "Could not delete product from cart")
	}

	return c.NoContent(http.StatusNoContent)
}

func DeleteCart(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	cartID := c.Param("cartId")

	var cart models.Cart
	if err := db.First(&cart, cartID).Error; err != nil {
		return c.JSON(http.StatusNotFound, "Cart not found")
	}

	db.Delete(&cart)
	return c.NoContent(http.StatusNoContent)
}
