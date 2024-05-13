package routes

import (
	"github.com/labstack/echo/v4"
	"gobackend/handlers"
)

const cartRoute = "/carts"
const cartIDRoute = "/carts/:cartId"
const cartIDProductIDRoute = "/carts/:cartId/products/:productId"

func CartRoutes(e *echo.Echo) {
	e.POST(cartRoute, handlers.CreateCart)
	e.GET(cartIDRoute, handlers.GetCart)
	e.DELETE(cartIDRoute, handlers.DeleteCart)
	e.POST(cartIDProductIDRoute, handlers.AddProductToCart)
	e.PUT(cartIDProductIDRoute, handlers.UpdateProductInCart)
	e.DELETE(cartIDProductIDRoute, handlers.DeleteProductFromCart)
}
