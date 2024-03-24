package routes

import (
	"github.com/labstack/echo/v4"
	"goproject/controllers"
)

func ProductRoutes(e *echo.Echo) {
	e.POST("/products", controllers.CreateProduct)
	e.GET("/products", controllers.GetProducts)
	e.GET("/products/:id", controllers.GetProduct)
	e.PUT("/products/:id", controllers.UpdateProduct)
	e.DELETE("/products/:id", controllers.DeleteProduct)
}
