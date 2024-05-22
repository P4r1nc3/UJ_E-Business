package models

type CartProduct struct {
	CardProductId uint    `gorm:"primaryKey" json:"-"`
	CartID        uint    `json:"-"`
	ProductID     uint    `json:"-"`
	Product       Product `gorm:"foreignKey:ProductID"`
	Quantity      int     `json:"quantity"`
	Price         float64 `json:"price"`
}
