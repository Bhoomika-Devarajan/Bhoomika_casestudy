import React, { useState } from 'react';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';
import { useNavigate, Link } from 'react-router-dom';
import authService from '../../services/authService';

const Register = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        role: 'PATIENT'
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!formData.username || formData.username.length < 3) {
            alert('Username must be at least 3 characters long.');
            return;
        }
        if (!formData.password || formData.password.length < 6) {
            alert('Password must be at least 6 characters long.');
            return;
        }

        authService.register(formData)
            .then(() => {
                alert('Registration successful. Please login!');
                navigate('/login');
            })
            .catch(error => {
                alert(error.response?.data?.message || 'Registration failed');
            });
    };

    return (
        <Container className="mt-5 mb-5">
            <Row className="justify-content-center">
                <Col md={8}>
                    <Card className="shadow-sm border-0">
                        <Card.Body className="p-5">
                            <h2 className="text-center mb-4">Register for AmazeCare</h2>
                            <Form onSubmit={handleSubmit}>
                                <Row>
                                    <Col md={12}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Username</Form.Label>
                                            <Form.Control type="text" name="username" value={formData.username} onChange={handleChange} required />
                                        </Form.Group>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col md={12}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Password</Form.Label>
                                            <Form.Control type="password" name="password" value={formData.password} onChange={handleChange} required />
                                        </Form.Group>
                                    </Col>
                                </Row>

                                <Form.Group className="mb-4">
                                    <Form.Label>Register As:</Form.Label>
                                    <Form.Select name="role" value={formData.role} onChange={handleChange}>
                                        <option value="PATIENT">Patient</option>
                                        <option value="DOCTOR">Doctor</option>
                                        <option value="ADMIN">Admin</option>
                                    </Form.Select>
                                </Form.Group>

                                <Button variant="primary" type="submit" className="w-100 mb-3" size="lg">
                                    Register Account
                                </Button>
                            </Form>
                            <div className="text-center">
                                Already have an account? <Link to="/login">Login here</Link>
                            </div>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default Register;
