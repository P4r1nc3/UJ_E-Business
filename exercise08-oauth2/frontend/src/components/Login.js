import React, { useState } from 'react';
import axios from 'axios';
import {useNavigate } from 'react-router-dom';
import { FcGoogle } from 'react-icons/fc';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const { data } = await axios.post('http://localhost:3001/api/user/login', { email, password });
            localStorage.setItem('userToken', data);
            navigate("/dashboard");
        } catch (error) {
            alert('Login failed: ' + error.response.data);
        }
    };

    return (
        <div style={{ minHeight: 'calc(100vh - 4rem)' }} className="flex justify-center items-center bg-gray-100">
            <div className="w-full max-w-lg bg-white shadow-md rounded px-10 pt-8 pb-10 mb-4">
                <h2 className="mb-8 text-center text-3xl font-extrabold text-gray-900">Login</h2>
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Email</label>
                        <input type="email" placeholder="Email" value={email}
                               onChange={e => setEmail(e.target.value)}
                               className="mt-1 focus:ring-indigo-500 focus:border-indigo-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
                               required />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Password</label>
                        <input type="password" placeholder="Password" value={password}
                               onChange={e => setPassword(e.target.value)}
                               className="mt-1 focus:ring-indigo-500 focus:border-indigo-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
                               required />
                    </div>
                    <button type="submit"
                            className="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        Login
                    </button>
                    <button
                        onClick={() => window.location.href='http://localhost:3001/api/user/auth/google'}
                        className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-500 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    >
                      <span className="mr-2">
                        <FcGoogle size={20} />
                      </span>
                        Login with Google
                    </button>
                </form>
            </div>
        </div>
    );
};

export default Login;
