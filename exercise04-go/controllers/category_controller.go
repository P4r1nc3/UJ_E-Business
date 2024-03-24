package controllers

import (
	"github.com/labstack/echo/v4"
	"goproject/models"
	"goproject/scopes"
	"gorm.io/gorm"
	"net/http"
)

func CreateCategory(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	category := new(models.Category)
	if err := c.Bind(category); err != nil {
		return err
	}
	db.Create(&category)
	return c.JSON(http.StatusCreated, category)
}

func GetCategories(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)
	var categories []models.Category
	scopes.PreloadProducts(db).Find(&categories)
	return c.JSON(http.StatusOK, categories)
}

func GetCategory(c echo.Context) error {
	id := c.Param("id")
	db := c.Get("db").(*gorm.DB)
	var category models.Category
	result := scopes.PreloadProducts(scopes.FindByID(id)(db)).First(&category)
	if result.Error != nil {
		return c.JSON(http.StatusNotFound, "Category not found")
	}
	return c.JSON(http.StatusOK, category)
}

func UpdateCategory(c echo.Context) error {
	id := c.Param("id")
	db := c.Get("db").(*gorm.DB)
	var category models.Category
	if err := scopes.FindByID(id)(db).First(&category).Error; err != nil {
		return c.JSON(http.StatusNotFound, "Category not found")
	}
	if err := c.Bind(&category); err != nil {
		return err
	}
	db.Save(&category)
	return c.JSON(http.StatusOK, category)
}

func DeleteCategory(c echo.Context) error {
	id := c.Param("id")
	db := c.Get("db").(*gorm.DB)
	var category models.Category
	if err := scopes.FindByID(id)(db).First(&category).Error; err != nil {
		return c.JSON(http.StatusNotFound, "Category not found")
	}
	db.Delete(&category)
	return c.NoContent(http.StatusNoContent)
}
