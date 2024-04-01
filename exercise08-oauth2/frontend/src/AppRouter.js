import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Dashboard from './components/Dashboard';
import Login from './components/Login';
import Register from './components/Register';
import RequireAuth from './helpers/RequireAuth';

const AppRouter = () => (
    <Router>
        <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/dashboard" element={<RequireAuth><Dashboard /></RequireAuth>} />
            {/* Dodaj inne ścieżki tutaj */}
        </Routes>
    </Router>
);

export default AppRouter;
