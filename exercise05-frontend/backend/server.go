package main

import (
	"gobackend/database"
	"gobackend/routes"
)

func main() {
	e := database.SetupEcho()

	routes.ProductRoutes(e)

	e.Start(":8080")
}
