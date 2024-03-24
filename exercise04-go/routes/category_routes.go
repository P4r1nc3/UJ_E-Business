package routes

import (
	"github.com/labstack/echo/v4"
	"goproject/controllers"
)

func CategoryRoutes(e *echo.Echo) {
	e.POST("/categories", controllers.CreateCategory)
	e.GET("/categories", controllers.GetCategories)
	e.GET("/categories/:id", controllers.GetCategory)
	e.PUT("/categories/:id", controllers.UpdateCategory)
	e.DELETE("/categories/:id", controllers.DeleteCategory)
}
