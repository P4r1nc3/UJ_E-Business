package models

import (
	"gorm.io/gorm"
)

type CartProduct struct {
	gorm.Model
	CartID    uint    `json:"cartId"`
	ProductID uint    `json:"productId"`
	Product   Product `gorm:"foreignKey:ProductID"`
	Quantity  int     `json:"quantity"`
	Price     float64 `json:"price"`
}
