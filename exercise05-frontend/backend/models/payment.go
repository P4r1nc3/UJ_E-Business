package models

import (
	"gorm.io/gorm"
	"time"
)

type Payment struct {
	gorm.Model
	Amount float64   `json:"amount"`
	Status string    `json:"status"`
	CartID uint      `json:"cartId"`
	Cart   Cart      `gorm:"foreignKey:CartID"`
	PaidAt time.Time `json:"paidAt"`
}
