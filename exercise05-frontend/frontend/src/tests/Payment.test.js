import React from 'react';
import { render, fireEvent, waitFor, screen } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import axios from 'axios';
import Payment from '../components/Payment';
import { CartProvider } from '../CartContext';

jest.mock('axios');
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => jest.fn(),
}));

describe('Payment Component', () => {
    beforeEach(() => {
        axios.post.mockResolvedValue({});
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    const renderWithProviders = (ui, options) => render(
        <Router>
            <CartProvider>
                {ui}
            </CartProvider>
        </Router>,
        options
    );

    test('renders Payment form correctly', () => {
        renderWithProviders(<Payment />);
        expect(screen.getByPlaceholderText('Name')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('CardNumber')).toBeInTheDocument();
        expect(screen.getByText('Complete Purchase')).toBeInTheDocument();
    });

    test('updates form fields correctly', () => {
        renderWithProviders(<Payment />);
        fireEvent.change(screen.getByPlaceholderText('Name'), { target: { value: 'John Doe' } });
        fireEvent.change(screen.getByPlaceholderText('CardNumber'), { target: { value: '1234 5678 9101 1121' } });

        expect(screen.getByPlaceholderText('Name').value).toBe('John Doe');
        expect(screen.getByPlaceholderText('CardNumber').value).toBe('1234 5678 9101 1121');
    });

    test('handles server errors on form submission', async () => {
        axios.post.mockRejectedValue(new Error('Failed to process payment'));
        jest.spyOn(window, 'alert').mockImplementation(() => {});

        renderWithProviders(<Payment />);
        fireEvent.submit(screen.getByText('Complete Purchase'));

        await waitFor(() => expect(window.alert).toHaveBeenCalledWith('There was an issue with your purchase. Please try again.'));
    });
});
