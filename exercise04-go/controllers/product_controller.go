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
	db.Create(&product)
	return c.JSON(http.StatusCreated, product)
}
