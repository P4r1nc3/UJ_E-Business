import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Navigation = () => {
    const { isAuthenticated, logout } = useAuth();
    const navigate = useNavigate();

    return (
        <nav className="bg-gray-800 text-white p-4">
            <div className="container mx-auto flex justify-end items-center">
                {isAuthenticated ? (
                    <div className="flex gap-4">
                        <Link to="/dashboard" className="hover:text-gray-300">Dashboard</Link>
                        <button onClick={() => {
                            logout();
                            navigate('/login');
                        }} className="hover:text-gray-300">
                            Logout
                        </button>
                    </div>
                ) : (
                    <div className="flex gap-4">
                        <Link to="/login" className="hover:text-gray-300">Login</Link>
                        <Link to="/register" className="hover:text-gray-300">Register</Link>
                    </div>
                )}
            </div>
        </nav>
    );
};

export default Navigation;
