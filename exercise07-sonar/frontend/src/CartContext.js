import React, { createContext, useState, useContext, useMemo } from 'react';
import PropTypes from 'prop-types';

const CartContext = createContext();

export const useCart = () => useContext(CartContext);

export const CartProvider = ({ children }) => {
    const [totalValue, setTotalValue] = useState(0);

    const updateTotalValue = (newValue) => {
        setTotalValue(newValue);
    };

    const value = useMemo(() => ({
        totalValue,
        updateTotalValue
    }), [totalValue]);

    return (
        <CartContext.Provider value={value}>
            {children}
        </CartContext.Provider>
    );
};

CartProvider.propTypes = {
    children: PropTypes.node
};
