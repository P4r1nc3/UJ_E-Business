import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Dashboard from './components/Dashboard';
import Login from './components/Login';
import Register from './components/Register';
import RequireAuth from './helpers/RequireAuth';
import Navigation from './components/Navigation';

const AppRouter = () => (
    <Router>
        <Navigation />
        <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/dashboard" element={<RequireAuth><Dashboard /></RequireAuth>} />
        </Routes>
    </Router>
);

export default AppRouter;
