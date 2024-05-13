import React from 'react';
import { render, waitFor, screen, fireEvent } from '@testing-library/react';
import axios from 'axios';
import Products from '../components/Products';

jest.mock('axios');

const mockProducts = [
    { id: 1, name: "Product A", description: "Description A", price: "10", imageUrl: "http://example.com/a.jpg", productId: 1 },
    { id: 2, name: "Product B", description: "Description B", price: "20", imageUrl: "http://example.com/b.jpg", productId: 2 },
];

describe('Products Component', () => {
    beforeEach(() => {
        axios.get.mockResolvedValue({ data: mockProducts });
        axios.post.mockResolvedValue({});
        jest.spyOn(console, 'error').mockImplementation(() => {});
    });

    afterEach(() => {
        jest.restoreAllMocks();
    });

    test('renders without crashing', async () => {
        render(<Products />);
        await waitFor(() => expect(axios.get).toHaveBeenCalledTimes(1));
    });

    test('fetches products on component mount and renders them', async () => {
        render(<Products />);
        await waitFor(() => expect(screen.getByText('Product A')).toBeInTheDocument());
        expect(screen.getByText('Product B')).toBeInTheDocument();
        expect(screen.getAllByRole('img').length).toBe(2);
    });

    test('displays error message on API failure', async () => {
        axios.get.mockRejectedValue(new Error('Failed to fetch'));
        render(<Products />);
        await waitFor(() => expect(console.error).toHaveBeenCalledWith('There has been a problem with your fetch operation:', expect.any(Error)));
    });

    test('add to cart calls API with correct URL', async () => {
        render(<Products />);
        await waitFor(() => fireEvent.click(screen.getAllByText('Add to Cart')[0]));
        expect(axios.post).toHaveBeenCalledWith('http://localhost:8080/carts/1/products/1');
    });

    test('product elements are correctly styled', async () => {
        render(<Products />);
        await waitFor(() => {
            const addButton = screen.getAllByText('Add to Cart')[0].closest('button');
            expect(addButton).toHaveClass('bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition-colors duration-200');
        });
    });
});
