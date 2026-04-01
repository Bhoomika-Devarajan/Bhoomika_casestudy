import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';


const PrivateRoute = ({ children, allowedRoles }) => {
    
    const { user } = useContext(AuthContext);

   
    if (!user) {
       
        return <Navigate to="/login" />;
    }

   
    if (allowedRoles) {
       
        let hasAccess = false;
        
      
        for (let i = 0; i < allowedRoles.length; i++) {
            if (user.role && user.role.includes(allowedRoles[i])) {
                hasAccess = true;
                break;
            }
        }
        
      
        if (!hasAccess) {
            return <Navigate to="/" />;
        }
    }

    
    return children;
};

export default PrivateRoute;
