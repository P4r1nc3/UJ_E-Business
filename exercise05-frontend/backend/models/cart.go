package models

import (
	"gorm.io/gorm"
)

type Cart struct {
	gorm.Model
	Products []CartProduct `json:"products" gorm:"foreignKey:CartID"`
	Total    float64       `json:"total"`
}
