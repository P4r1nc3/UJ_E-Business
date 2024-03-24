package main

import (
	"gobackend/database"
	"gobackend/routes"
)

func main() {
	e := database.SetupEcho()

	routes.ProductRoutes(e)
	routes.CartRoutes(e)

	e.Start(":8080")
}
