import React, { useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import { useCart } from '../CartContext';

function Payment() {
    const navigate = useNavigate();
    const { totalValue } = useCart();
    const [shippingData, setShippingData] = useState({
        name: '',
        address: '',
        city: '',
        zip: '',
        country: '',
    });
    const [paymentData, setPaymentData] = useState({
        cardNumber: '',
        expiryDate: '',
        cvv: '',
    });

    const handleShippingChange = (e) => {
        setShippingData({ ...shippingData, [e.target.name]: e.target.value });
    };

    const handlePaymentChange = (e) => {
        setPaymentData({ ...paymentData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const requestData = {
            ...shippingData,
            ...paymentData,
            amount: totalValue,
            cartId: 1,
        };

        try {
            await axios.post('http://localhost:8080/payment', requestData);
            alert('Thank you for your purchase!');
            navigate('/');
        } catch (error) {
            console.error('Error completing your purchase:', error);
            alert('There was an issue with your purchase. Please try again.');
        }
    };

    return (
        <div className="p-4 max-w-md mx-auto bg-white shadow-md rounded-lg">
            <h2 className="text-2xl font-bold mb-4 text-center">Complete Your Purchase</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div className="space-y-2">
                    <h3 className="text-lg font-semibold">Shipping Details</h3>
                    {['name', 'address', 'city', 'zip', 'country'].map((field) => (
                        <input
                            key={field}
                            type="text"
                            name={field}
                            placeholder={field.charAt(0).toUpperCase() + field.slice(1)}
                            value={shippingData[field]}
                            onChange={handleShippingChange}
                            required
                            className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    ))}
                </div>

                <div className="space-y-2">
                    <h3 className="text-lg font-semibold">Payment Details</h3>
                    {['cardNumber', 'expiryDate', 'cvv'].map((field) => (
                        <input
                            key={field}
                            type="text"
                            name={field}
                            placeholder={field.charAt(0).toUpperCase() + field.slice(1)}
                            value={paymentData[field]}
                            onChange={handlePaymentChange}
                            required
                            className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    ))}
                </div>

                <div className="flex justify-between items-center mt-6">
                    <Link to="/cart" className="text-blue-500 hover:underline">Back to Cart</Link>
                    <button type="submit" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-150 ease-in-out">Complete Purchase</button>
                </div>
            </form>
        </div>
    );
}

export default Payment;
