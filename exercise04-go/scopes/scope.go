package scopes

import (
	"gorm.io/gorm"
)

func PreloadProducts(db *gorm.DB) *gorm.DB {
	return db.Preload("Products")
}

func FindByID(id string) func(db *gorm.DB) *gorm.DB {
	return func(db *gorm.DB) *gorm.DB {
		return db.Where("id = ?", id)
	}
}
