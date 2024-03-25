import React from 'react';

const products = [
    {
        id: 1,
        name: 'Product 1',
        description: 'This is a description of Product 1. It is a great product.',
        price: 10.99
    },
    {
        id: 2,
        name: 'Product 2',
        description: 'This is a description of Product 2. It is an awesome product.',
        price: 20.99
    },
];

function Products() {
    return (
        <div className="p-4">
            <h2 className="text-2xl font-bold mb-6 text-center">Our Products</h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                {products.map((product) => (
                    <div key={product.id} className="bg-white rounded-lg overflow-hidden shadow-lg hover:shadow-2xl transition-shadow duration-300">
                        <img src={product.imageUrl} alt={product.name} className="w-full h-48 object-cover" />
                        <div className="p-4">
                            <h3 className="text-lg font-semibold">{product.name}</h3>
                            <p className="text-sm text-gray-600 mb-4">{product.description}</p>
                            <p className="text-lg font-bold">${product.price}</p>
                            <button className="mt-4 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition-colors duration-200">
                                Add to Cart
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Products;
