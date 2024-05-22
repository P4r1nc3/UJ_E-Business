package handlers

import (
	"github.com/labstack/echo/v4"
	"gobackend/models"
	"gorm.io/gorm"
	"net/http"
)

func SavePaymentDetails(c echo.Context) error {
	db := c.Get("db").(*gorm.DB)

	newPayment := models.Payment{}
	if err := c.Bind(&newPayment); err != nil {
		return c.JSON(http.StatusBadRequest, echo.Map{"error": "Invalid payment data"})
	}

	if err := db.Create(&newPayment).Error; err != nil {
		return c.JSON(http.StatusInternalServerError, echo.Map{"error": "Could not save payment details"})
	}

	return c.JSON(http.StatusCreated, newPayment)
}
