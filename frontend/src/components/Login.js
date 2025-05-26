import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './login.css'; // Optional for styling

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate(); // For redirection

    const handleSubmit = async (e) => {
        e.preventDefault();

        setErrorMessage(''); // Clear any previous errors

        try {
            const response = await fetch('http://localhost:8080/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },  
                body: JSON.stringify({ username, password }),
            });

            const result = await response.json();

            if (!response.ok) {
                setErrorMessage(result.message || 'Login failed. Please try again.');
                return;
            }

            // Redirect to the dashboard on successful login
            navigate('/dashboard');
        } catch (error) {
            setErrorMessage('An error occurred. Please try again later.');
            console.error('Error:', error);
        }
    };

    return (
        <div className="login-container">
            <h2>login</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Username</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                {errorMessage && <div className="error-message">{errorMessage}</div>}
                <button type="submit" className="btn">Login</button>
            </form>
        </div>
    );
};

export default Login;
