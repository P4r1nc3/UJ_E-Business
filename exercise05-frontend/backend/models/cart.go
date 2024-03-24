package models

type Cart struct {
	CartId   uint          `gorm:"primaryKey" json:"cartId"`
	Products []CartProduct `json:"products" gorm:"foreignKey:CartID"`
}
