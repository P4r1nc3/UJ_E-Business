import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const Dashboard = () => {
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const params = new URLSearchParams(location.search);
        const tokenFromURL = params.get('token');
        const tokenInStorage = localStorage.getItem('userToken');

        if (tokenFromURL) {
            localStorage.setItem('userToken', tokenFromURL);
            navigate('/dashboard', { replace: true });
        } else if (!tokenInStorage) {
            navigate('/login');
        }
    }, [location, navigate]);

    return (
        <div className="flex justify-center items-center bg-gray-100" style={{ minHeight: 'calc(100vh - 4rem)' }}>
            <h2 className="text-2xl font-bold text-gray-900">You are logged in</h2>
        </div>
    );
};

export default Dashboard;
