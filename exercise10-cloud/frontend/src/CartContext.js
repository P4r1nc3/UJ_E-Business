import React, { createContext, useState, useContext } from 'react';

const CartContext = createContext();

export const useCart = () => useContext(CartContext);

export const CartProvider = ({ children }) => {
    const [totalValue, setTotalValue] = useState(0);

    const updateTotalValue = (newValue) => {
        setTotalValue(newValue);
    };

    return (
        <CartContext.Provider value={{ totalValue, updateTotalValue }}>
            {children}
        </CartContext.Provider>
    );
};
