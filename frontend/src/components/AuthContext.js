import React, { createContext, useContext, useState } from "react";

// Create Auth Context
const AuthContext = createContext();

// Provide the Auth Context to the app
export const AuthProvider = ({ children }) => {
  const [username, setUsername] = useState(null); // Store the authenticated username

  return (
    <AuthContext.Provider value={{ username, setUsername }}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook to use the Auth Context
export const useAuth = () => useContext(AuthContext);
