package models

type Payment struct {
	Amount float64 `json:"amount"`
	CartID uint    `json:"cartId"`

	Name    string `json:"name"`
	Address string `json:"address"`
	City    string `json:"city"`
	Zip     string `json:"zip"`
	Country string `json:"country"`

	CardNumber string `json:"cardNumber"`
	ExpiryDate string `json:"expiryDate"`
	CVV        string `json:"cvv"`
}
