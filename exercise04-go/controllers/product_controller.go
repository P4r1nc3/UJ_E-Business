package controllers

import (
	"github.com/labstack/echo/v4"
	"goproject/models"
	"gorm.io/gorm"
	"net/http"
)

func CreateProduct(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	product := new(models.Product)
	if err := c.Bind(product); err != nil {
		return err
	}

	if product.CategoryID != 0 {
		var category models.Category
		if err := db.First(&category, product.CategoryID).Error; err != nil {
			return c.JSON(http.StatusBadRequest, "Category does not exist")
		}
	}

	db.Create(&product)
	return c.JSON(http.StatusCreated, product)
}

func GetProducts(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	var products []models.Product
	db.Find(&products)
	return c.JSON(http.StatusOK, products)
}

func GetProduct(c echo.Context) error {
	id := c.Param("id")
	db := c.Get("db").(*gorm.DB)
	var product models.Product
	result := db.First(&product, id)
	if result.Error != nil {
		return c.JSON(http.StatusNotFound, "Product not found")
	}
	return c.JSON(http.StatusOK, product)
}

func UpdateProduct(c echo.Context) error {
	id := c.Param("id")
	db := c.Get("db").(*gorm.DB)

	var product models.Product
	if db.First(&product, id).Error != nil {
		return c.JSON(http.StatusNotFound, "Product not found")
	}

	var updateData models.Product
	if err := c.Bind(&updateData); err != nil {
		return err
	}

	if updateData.CategoryID != 0 && updateData.CategoryID != product.CategoryID {
		var category models.Category
		if err := db.First(&category, updateData.CategoryID).Error; err != nil {
			return c.JSON(http.StatusBadRequest, "Category does not exist")
		}
	}

	if err := db.Model(&product).Updates(updateData).Error; err != nil {
		return c.JSON(http.StatusInternalServerError, "Could not update product")
	}

	return c.JSON(http.StatusOK, product)
}

func DeleteProduct(c echo.Context) error {
	id := c.Param("id")
	db := c.Get("db").(*gorm.DB)
	var product models.Product
	if db.First(&product, id).Error != nil {
		return c.JSON(http.StatusNotFound, "Product not found")
	}
	db.Delete(&product)
	return c.NoContent(http.StatusNoContent)
}
