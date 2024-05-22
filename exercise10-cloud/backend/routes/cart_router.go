package routes

import (
	"github.com/labstack/echo/v4"
	"gobackend/handlers"
)

func CartRoutes(e *echo.Echo) {
	e.POST("/carts", handlers.CreateCart)
	e.GET("/carts/:cartId", handlers.GetCart)
	e.DELETE("/carts/:cartId", handlers.DeleteCart)
	e.POST("/carts/:cartId/products/:productId", handlers.AddProductToCart)
	e.PUT("/carts/:cartId/products/:productId", handlers.UpdateProductInCart)
	e.DELETE("/carts/:cartId/products/:productId", handlers.DeleteProductFromCart)
}
