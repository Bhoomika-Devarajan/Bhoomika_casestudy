import React, { useState, useContext } from 'react';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';
import { useNavigate, Link } from 'react-router-dom';
import authService from '../../services/authService';
import { AuthContext } from '../../context/AuthContext';

const Login = () => {
   
    const [credentials, setCredentials] = useState({ username: '', password: '' });
    
    const { login } = useContext(AuthContext);
   
    const navigate = useNavigate();

   
    const handleChange = (event) => {
        const fieldName = event.target.name;    // e.g., 'email' or 'password'
        const fieldValue = event.target.value;  // e.g., 'test@example.com'
        
       
        setCredentials({ ...credentials, [fieldName]: fieldValue });
    };

    const handleSubmit = (event) => {
        // preventDefault stops the page from reloading (default form behavior)
        event.preventDefault();
        
        if (!credentials.username) {
            alert("Please enter a valid username before proceeding.");
            return;
        }
        if (!credentials.password) {
            alert("Please enter your password.");
            return;
        }
        
        
        authService.login(credentials.username, credentials.password)
            .then(token => {
                if (token !== null && token !== undefined) {
                    // If successful, save the token and redirect to home
                    login(token);
                    alert('Login Successful!');
                    navigate('/');
                }
            })
            .catch(error => {
                // If there's an error (like a wrong password), show an alert box
                let errorMessage = 'Invalid credentials';
                if (error.response && error.response.data && error.response.data.message) {
                    errorMessage = error.response.data.message;
                }
                alert(errorMessage);
            });
    };

    return (
        <Container className="mt-5">
            <Row className="justify-content-center">
                <Col md={6}>
                    <Card className="shadow-sm border-0">
                        <Card.Body className="p-5">
                            <h2 className="text-center mb-4">Login to AmazeCare</h2>
                            <Form onSubmit={handleSubmit}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Username</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="username"
                                        value={credentials.username}
                                        onChange={handleChange}
                                        required
                                        placeholder="Enter username"
                                    />
                                </Form.Group>
                                <Form.Group className="mb-4">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control
                                        type="password"
                                        name="password"
                                        value={credentials.password}
                                        onChange={handleChange}
                                        required
                                        placeholder="Enter password"
                                    />
                                </Form.Group>
                                <Button variant="primary" type="submit" className="w-100 mb-3" size="lg">
                                    Login
                                </Button>
                            </Form>
                            <div className="text-center">
                                Don't have an account? <Link to="/register">Register here</Link>
                            </div>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default Login;
