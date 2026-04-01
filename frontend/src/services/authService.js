import api from './api';

const authService = {
    login: (username, password) => {
        return api.post('/auth/login', { username: username, password: password })
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    
    register: (userData) => {
        let rolePath = 'patient';
        if (userData.role === 'DOCTOR') rolePath = 'doctor';
        if (userData.role === 'ADMIN') rolePath = 'admin';
        
        return api.post(`/auth/register/${rolePath}`, {
            username: userData.username,
            password: userData.password
        })
        .then(response => response.data)
        .catch(error => { throw error; });
    }
};

export default authService;
