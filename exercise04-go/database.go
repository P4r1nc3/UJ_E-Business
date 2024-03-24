package main

import (
	"goproject/models"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"log"

	"github.com/labstack/echo/v4"
)

func SetupDatabase() *gorm.DB {
	dsn := "root:admin12345@tcp(localhost:3306)/goproject?charset=utf8mb4&parseTime=True&loc=Local"
	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Panic("failed to connect database")
	}

	db.AutoMigrate(&models.Product{})
	db.AutoMigrate(&models.Cart{})
	db.AutoMigrate(&models.Category{})

	return db
}

func SetupEcho() *echo.Echo {
	e := echo.New()

	db := SetupDatabase()

	e.Use(func(next echo.HandlerFunc) echo.HandlerFunc {
		return func(c echo.Context) error {
			c.Set("db", db)
			return next(c)
		}
	})

	return e
}
