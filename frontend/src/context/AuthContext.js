import React, { createContext, useState, useEffect } from 'react';
import {jwtDecode} from 'jwt-decode';


export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
   
    const [user, setUser] = useState(null);
   
    const [loading, setLoading] = useState(true);

  
    useEffect(() => {
        
        const token = localStorage.getItem('token');
        
        if (token) {
            try {
               
                const decodedToken = jwtDecode(token);
               
                setUser({
                    username: decodedToken.sub,
                    role: decodedToken.roles ? decodedToken.roles[0] : (decodedToken.role || decodedToken.authorities || 'PATIENT'),
                    token: token
                });
            } catch (err) {
                
                console.error("Invalid token found, removing it.", err);
                localStorage.removeItem('token');
            }
        }
        
        
        setLoading(false);
    }, []);

  
    const login = (token) => {
        localStorage.setItem('token', token);
        const decodedToken = jwtDecode(token);
        setUser({
            username: decodedToken.sub,
            role: decodedToken.roles ? decodedToken.roles[0] : (decodedToken.role || decodedToken.authorities || 'PATIENT'),
            token: token
        });
    };

    
    const logout = () => {
        localStorage.removeItem('token');
        setUser(null);
    };

    // The data and functions we want to make available to other components
    let currentRole = null;
    if (user !== null) {
        currentRole = user.role;
       
        if (typeof currentRole === 'string' && currentRole.startsWith('ROLE_')) {
            currentRole = currentRole.substring(5);
        }
    }
    const value = { user, login, logout, role: currentRole };

    return (
        <AuthContext.Provider value={value}>
            {!loading && children}
        </AuthContext.Provider>
    );
};
