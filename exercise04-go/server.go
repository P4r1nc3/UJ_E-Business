package main

import (
	"goproject/controllers"
)

func main() {
	e := SetupEcho()

	// Products
	e.POST("/products", controllers.CreateProduct)
	e.GET("/products", controllers.GetProducts)
	e.GET("/products/:id", controllers.GetProduct)
	e.PUT("/products/:id", controllers.UpdateProduct)
	e.DELETE("/products/:id", controllers.DeleteProduct)

	// Categories
	e.POST("/categories", controllers.CreateCategory)
	e.GET("/categories", controllers.GetCategories)
	e.GET("/categories/:id", controllers.GetCategory)
	e.PUT("/categories/:id", controllers.UpdateCategory)
	e.DELETE("/categories/:id", controllers.DeleteCategory)

	// Carts
	e.POST("/carts", controllers.CreateCart)
	e.GET("/carts/:cartId", controllers.GetCart)
	e.DELETE("/carts/:cartId", controllers.DeleteCart)
	e.POST("/carts/:cartId/products/:productId", controllers.AddProductToCart)
	e.DELETE("/carts/:cartId/products/:productId", controllers.DeleteProductFromCart)

	e.Start(":8080")
}
