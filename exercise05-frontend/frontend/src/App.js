import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Products from './components/Products';
import Cart from './components/Cart';
import Payment from './components/Payment';
import { CartProvider } from './CartContext';
import './App.css';

function App() {
  return (
      <CartProvider>
        <Router>
          <div className="min-h-screen bg-gray-100">
            <nav className="bg-gray-800 text-white p-4">
              <ul className="flex space-x-4 justify-center">
                <li>
                  <Link to="/" className="hover:text-gray-300">Products</Link>
                </li>
                <li>
                  <Link to="/cart" className="hover:text-gray-300">Cart</Link>
                </li>
              </ul>
            </nav>
            <main className="p-4">
              <Routes>
                <Route path="/" element={<Products />} />
                <Route path="/cart" element={<Cart />} />
                <Route path="/payment" element={<Payment />} />
              </Routes>
            </main>
          </div>
        </Router>
      </CartProvider>
  );
}

export default App;
