package routes

import (
	"github.com/labstack/echo/v4"
	"gobackend/handlers"
)

func ProductRoutes(e *echo.Echo) {
	e.POST("/products", handlers.CreateProduct)
	e.GET("/products", handlers.GetProducts)
	e.GET("/products/:id", handlers.GetProductById)
	e.PUT("/products/:id", handlers.UpdateProduct)
	e.DELETE("/products/:id", handlers.DeleteProduct)
}
