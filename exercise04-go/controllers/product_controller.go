package controllers

import (
	"fmt"
	"github.com/labstack/echo/v4"
	"goproject/models"
	"goproject/scopes"
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
		categoryIDStr := fmt.Sprintf("%d", product.CategoryID)
		var category models.Category
		if err := scopes.FindByID(categoryIDStr)(db).First(&category).Error; err != nil {
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
	if err := scopes.FindByID(id)(db).First(&product).Error; err != nil {
		return c.JSON(http.StatusNotFound, "Product not found")
	}
	return c.JSON(http.StatusOK, product)
}

func UpdateProduct(c echo.Context) error {
	id := c.Param("id")
	db := c.Get("db").(*gorm.DB)

	var product models.Product
	if err := scopes.FindByID(id)(db).First(&product).Error; err != nil {
		return c.JSON(http.StatusNotFound, "Product not found")
	}

	var updateData models.Product
	if err := c.Bind(&updateData); err != nil {
		return err
	}

	if updateData.CategoryID != 0 && updateData.CategoryID != product.CategoryID {
		categoryIDStr := fmt.Sprintf("%d", product.CategoryID)
		var category models.Category
		if err := scopes.FindByID(categoryIDStr)(db).First(&category).Error; err != nil {
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
	if err := scopes.FindByID(id)(db).First(&product).Error; err != nil {
		return c.JSON(http.StatusNotFound, "Product not found")
	}
	db.Delete(&product)
	return c.NoContent(http.StatusNoContent)
}
