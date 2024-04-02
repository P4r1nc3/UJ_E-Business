import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const Dashboard = () => {
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const params = new URLSearchParams(location.search);
        const token = params.get('token');
        if (token) {
            localStorage.setItem('userToken', token);
            navigate('/dashboard', { replace: true });
        }
    }, [location, navigate]);
    return (
        <div className="flex justify-center items-center bg-gray-100" style={{ minHeight: 'calc(100vh - 4rem)' }}>
            <h2 className="text-2xl font-bold text-gray-900">You are logged in</h2>
        </div>
    );
};

export default Dashboard;
