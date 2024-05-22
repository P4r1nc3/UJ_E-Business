package handlers

import (
	"github.com/labstack/echo/v4"
	"gobackend/models"
	"gorm.io/gorm"
	"net/http"
)

func CreateProduct(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)

	var product models.Product
	if err := c.Bind(&product); err != nil {
		return err
	}

	if result := db.Create(&product); result.Error != nil {
		return c.JSON(http.StatusInternalServerError, result.Error)
	}

	return c.JSON(http.StatusCreated, product)
}

func GetProducts(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)

	var products []models.Product
	if result := db.Find(&products); result.Error != nil {
		return c.JSON(http.StatusInternalServerError, result.Error)
	}

	return c.JSON(http.StatusOK, products)
}

func GetProductById(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	id := c.Param("id")

	var product models.Product
	if result := db.First(&product, id); result.Error != nil {
		return c.JSON(http.StatusNotFound, result.Error)
	}

	return c.JSON(http.StatusOK, product)
}

func UpdateProduct(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	id := c.Param("id")

	var product models.Product
	if result := db.First(&product, id); result.Error != nil {
		return c.JSON(http.StatusNotFound, result.Error)
	}

	var updateData models.Product
	if err := c.Bind(&updateData); err != nil {
		return err
	}

	db.Model(&product).Updates(updateData)

	return c.JSON(http.StatusOK, product)
}

func DeleteProduct(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	id := c.Param("id")

	var product models.Product
	if result := db.First(&product, id); result.Error != nil {
		return c.JSON(http.StatusNotFound, result.Error)
	}

	if result := db.Delete(&product); result.Error != nil {
		return c.JSON(http.StatusInternalServerError, result.Error)
	}

	return c.NoContent(http.StatusNoContent)
}
