package main

import (
	"github.com/labstack/echo/v4"
	"goproject/controllers"
	"goproject/models"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

func main() {
	e := echo.New()

	dsn := "root:admin12345@tcp(localhost:3306)/goproject?charset=utf8mb4&parseTime=True&loc=Local"

	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}

	e.Use(func(next echo.HandlerFunc) echo.HandlerFunc {
		return func(c echo.Context) error {
			c.Set("db", db)
			return next(c)
		}
	})
	db.AutoMigrate(&models.Product{})

	e.POST("/products", controllers.CreateProduct)
	e.Start(":8080")
}
