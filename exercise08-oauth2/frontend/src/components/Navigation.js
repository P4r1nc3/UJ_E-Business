import React from 'react';
import { Link } from 'react-router-dom';

const Navigation = () => {
    const isAuthenticated = localStorage.getItem('userToken');

    return (
        <nav>
            {isAuthenticated ? (
                <div>
                    <Link to="/dashboard">Dashboard</Link>
                    <button onClick={() => {
                        localStorage.removeItem('userToken');
                        window.location.href = '/login';
                    }}>Logout</button>
                </div>
            ) : (
                <div>
                    <Link to="/login">Login</Link>
                    <Link to="/register">Register</Link>
                </div>
            )}
        </nav>
    );
};

export default Navigation;
