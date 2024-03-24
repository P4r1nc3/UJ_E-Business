package main

import (
	"goproject/routes"
)

func main() {
	e := SetupEcho()

	routes.ProductRoutes(e)
	routes.CartRoutes(e)
	routes.CategoryRoutes(e)

	e.Start(":8080")
}
