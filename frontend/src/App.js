import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Login from './components/Login.js';
import Register from './components/Register.js';
import Dashboard from './components/Dashboard.js';

function App() {
    return (
        <Router>
            <nav>
                <Link to="/">Home</Link> | <Link to="/login">Login</Link> | <Link to="/register">Register</Link>
            </nav>
            <Routes>
                <Route path="/" element={<h1>Welcome to the App</h1>} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/dashboard" element={<Dashboard />} /> {/* Add Dashboard route */}
            </Routes>
        </Router>
    );
}

export default App;
