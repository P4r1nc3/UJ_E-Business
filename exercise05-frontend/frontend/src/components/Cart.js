import React, { useState, useEffect } from 'react';
import axios from 'axios';

function Cart() {
    const [cartItems, setCartItems] = useState([]);

    useEffect(() => {
        const fetchCartData = async () => {
            try {
                const response = await axios.get('http://localhost:8080/carts/1');
                setCartItems(response.data.products);
            } catch (error) {
                console.error('Error fetching cart:', error);
            }
        };

        fetchCartData();
    }, []);

    return (
        <div className="p-4">
            <h2 className="text-2xl font-bold mb-6 text-center">Your Cart</h2>
            {cartItems.length > 0 ? (
                cartItems.map((item) => (
                    <div key={item.cartProductId} className="flex items-center bg-white p-4 rounded-lg shadow mb-4">
                        <img src={item.Product.imageUrl} alt={item.Product.name} className="w-20 h-20 object-cover rounded mr-4" />
                        <div>
                            <h3 className="text-lg font-semibold">{item.Product.name}</h3>
                            <p className="text-sm">{item.Product.description}</p>
                            <p className="text-lg font-bold">${item.price}</p>
                            <p>Quantity: {item.quantity}</p>
                        </div>
                    </div>
                ))
            ) : (
                <p>Your cart is empty.</p>
            )}
        </div>
    );
}

export default Cart;
