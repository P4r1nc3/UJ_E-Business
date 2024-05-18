package routes

import (
	"github.com/labstack/echo/v4"
	"gobackend/handlers"
)

const productRoute = "/products"
const productIDRoute = "/products/:id"

func ProductRoutes(e *echo.Echo) {
	e.POST(productRoute, handlers.CreateProduct)
	e.GET(productRoute, handlers.GetProducts)
	e.GET(productIDRoute, handlers.GetProductById)
	e.PUT(productIDRoute, handlers.UpdateProduct)
	e.DELETE(productIDRoute, handlers.DeleteProduct)
}
