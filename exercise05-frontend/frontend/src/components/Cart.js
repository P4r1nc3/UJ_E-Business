import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { useCart } from '../CartContext'; // Upewnij się, że ścieżka jest prawidłowa

function Cart() {
    const [cartItems, setCartItems] = useState([]);
    const { totalValue, updateTotalValue } = useCart(); // Zmodyfikowano, aby użyć totalValue z kontekstu

    useEffect(() => {
        fetchCartData();
    }, []);

    useEffect(() => {
        const newTotalValue = cartItems.reduce((acc, item) => acc + item.price, 0);
        updateTotalValue(newTotalValue);
    }, [cartItems, updateTotalValue]); // Dodano updateTotalValue do tablicy zależności

    const fetchCartData = async () => {
        try {
            const url = 'http://localhost:8080/carts/1';
            const response = await axios.get(url);
            setCartItems(response.data.products);
        } catch (error) {
            console.error('Error fetching cart:', error);
        }
    };

    const updateQuantity = async (productId, quantity) => {
        try {
            const url = `http://localhost:8080/carts/1/products/${productId}?quantity=${quantity}`;
            await axios.put(url);
            fetchCartData();
        } catch (error) {
            console.error('Error updating quantity:', error);
        }
    };

    const removeItem = async (productId) => {
        try {
            const url = `http://localhost:8080/carts/1/products/${productId}`;
            await axios.delete(url);
            fetchCartData();
        } catch (error) {
            console.error('Error removing item:', error);
        }
    };

    return (
        <div className="p-4">
            <h2 className="text-2xl font-bold mb-6">Your Cart</h2>
            {cartItems.length > 0 ? (
                <div>
                    {cartItems.map((item) => (
                        <div key={item.Product.productId} className="flex items-center bg-white p-4 rounded-lg shadow mb-4 justify-between">
                            <img src={item.Product.imageUrl} alt={item.Product.name} className="w-20 h-20 object-cover rounded mr-4" />
                            <div className="flex-grow">
                                <h3 className="text-lg font-semibold">{item.Product.name}</h3>
                                <p className="text-sm">{item.Product.description}</p>
                                <p className="text-lg">${parseFloat(item.price).toFixed(2)} <span className="text-sm font-normal">(${parseFloat(item.Product.price).toFixed(2)} each)</span></p>
                            </div>
                            <div className="flex items-center">
                                <button onClick={() => item.quantity > 1 && updateQuantity(item.Product.productId, item.quantity - 1)}>-</button>
                                <span className="mx-2">{item.quantity}</span>
                                <button onClick={() => updateQuantity(item.Product.productId, item.quantity + 1)}>+</button>
                                <button onClick={() => removeItem(item.Product.productId)} className="ml-4">🗑</button>
                            </div>
                        </div>
                    ))}
                    <div className="flex justify-between items-center mt-4 bg-white p-4 rounded-lg shadow">
                        <h3 className="text-lg font-semibold">Total: ${totalValue.toFixed(2)}</h3>
                        <Link to="/payment" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Proceed to Payment</Link>
                    </div>
                </div>
            ) : (
                <p>Your cart is empty.</p>
            )}
        </div>
    );
}

export default Cart;
