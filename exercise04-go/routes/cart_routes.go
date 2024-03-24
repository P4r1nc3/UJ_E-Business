package routes

import (
	"github.com/labstack/echo/v4"
	"goproject/controllers"
)

func CartRoutes(e *echo.Echo) {
	e.POST("/carts", controllers.CreateCart)
	e.GET("/carts/:cartId", controllers.GetCart)
	e.DELETE("/carts/:cartId", controllers.DeleteCart)
	e.POST("/carts/:cartId/products/:productId", controllers.AddProductToCart)
	e.DELETE("/carts/:cartId/products/:productId", controllers.DeleteProductFromCart)
}
