// src/axiosConfig.js

import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080', // Replace with your backend's base URL
  timeout: 10000, // Set a timeout value (optional)
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add interceptors if needed (e.g., for logging, error handling, or adding authorization tokens)
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API call error:', error);
    return Promise.reject(error);
  }
);

export default axiosInstance;
