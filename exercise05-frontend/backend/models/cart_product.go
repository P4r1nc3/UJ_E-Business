package models

type CartProduct struct {
	CardProductId uint    `gorm:"primaryKey" json:"cartProductId"`
	CartID        uint    `json:"cartId"`
	ProductID     uint    `json:"productId"`
	Product       Product `gorm:"foreignKey:ProductID"`
	Quantity      int     `json:"quantity"`
	Price         float64 `json:"price"`
}
