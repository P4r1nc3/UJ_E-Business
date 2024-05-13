package main

import (
	"gobackend/database"
	"gobackend/routes"

	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
)

func main() {
	e := database.SetupEcho()

	e.Use(middleware.CORSWithConfig(middleware.CORSConfig{
		AllowOrigins: []string{"*"},
		AllowMethods: []string{echo.GET, echo.PUT, echo.PATCH, echo.POST, echo.DELETE},
	}))

	routes.ProductRoutes(e)
	routes.CartRoutes(e)
	routes.PaymentRoutes(e)

	e.Start(":8080")
}
