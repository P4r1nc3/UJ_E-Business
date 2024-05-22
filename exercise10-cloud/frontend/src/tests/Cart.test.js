import React from 'react';
import { render, waitFor, screen, fireEvent } from '@testing-library/react';
import axios from 'axios';
import { BrowserRouter as Router } from 'react-router-dom';
import Cart from '../components/Cart';
import { CartProvider } from '../CartContext';

jest.mock('axios');

const mockCartItems = [
    { Product: { productId: 1, name: "Product 1", description: "Desc 1", imageUrl: "http://example.com/p1.jpg", price: 10 }, quantity: 2, price: 20 },
    { Product: { productId: 2, name: "Product 2", description: "Desc 2", imageUrl: "http://example.com/p2.jpg", price: 15 }, quantity: 1, price: 15 }
];

describe('Cart Component', () => {
    beforeEach(() => {
        axios.get.mockResolvedValue({ data: { products: mockCartItems } });
        axios.put.mockResolvedValue({});
        axios.delete.mockResolvedValue({});
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    const customRender = (ui, { providerProps, ...renderOptions }) => {
        return render(
            <CartProvider value={providerProps}>
                <Router>{ui}</Router>
            </CartProvider>,
            renderOptions
        );
    };

    test('renders without crashing', async () => {
        customRender(<Cart />, {});
        await waitFor(() => expect(axios.get).toHaveBeenCalledTimes(1));
    });

    test('fetches and displays cart data correctly', async () => {
        customRender(<Cart />, {});
        await waitFor(() => {
            expect(screen.getByText('Product 1')).toBeInTheDocument();
            expect(screen.getByText('Desc 1')).toBeInTheDocument();
            expect(screen.getByText('$20.00')).toBeInTheDocument();
            expect(screen.getAllByText('Proceed to Payment').length).toBe(1);
        });
    });

    test('removes item from cart', async () => {
        customRender(<Cart />, {});
        await waitFor(() => fireEvent.click(screen.getAllByText('🗑')[0]));
        expect(axios.delete).toHaveBeenCalledWith('http://localhost:8080/carts/1/products/1');
    });

    test('updates total value when cart items change', async () => {
        customRender(<Cart />, {});
        await waitFor(() => fireEvent.click(screen.getAllByText('+')[0]));
    });
});
