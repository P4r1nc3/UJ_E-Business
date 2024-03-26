package models

type Product struct {
	ProductId   uint    `gorm:"primaryKey" json:"productId"`
	Name        string  `json:"name"`
	Description string  `json:"description"`
	Price       float64 `json:"price"`
	Available   bool    `json:"available"`
	ImageURL    string  `json:"imageUrl"`
}
