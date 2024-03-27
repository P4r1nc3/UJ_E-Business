package routes

import (
	"github.com/labstack/echo/v4"
	"gobackend/handlers"
)

func PaymentRoutes(e *echo.Echo) {
	e.POST("/payment", handlers.SavePaymentDetails)
}
