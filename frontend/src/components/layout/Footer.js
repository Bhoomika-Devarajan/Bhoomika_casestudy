import React from 'react';
import { Link } from 'react-router-dom';
import { FaPhoneAlt, FaEnvelope, FaMapMarkerAlt, FaFacebookF, FaTwitter, FaLinkedinIn, FaInstagram } from 'react-icons/fa';

const Footer = () => {
    return (
        <footer className="bg-dark text-light pt-5 pb-3 mt-auto border-top border-secondary">
            <div className="container">
                <div className="row g-4 mb-4">
                    {/* Brand Section */}
                    <div className="col-lg-4 col-md-6 mb-4 mb-md-0">
                        <h4 className="text-primary fw-bold mb-3">🏥 AmazeCare</h4>
                        <p className="text-secondary" style={{ fontSize: '0.95rem', lineHeight: '1.6' }}>
                            Delivering world-class healthcare with state-of-the-art facilities. Your health and comfort are our absolute highest priority.
                        </p>
                        <div className="d-flex gap-3 mt-4">
                            <a href="#" className="btn btn-outline-secondary rounded-circle px-2 py-1"><FaFacebookF /></a>
                            <a href="#" className="btn btn-outline-secondary rounded-circle px-2 py-1"><FaTwitter /></a>
                            <a href="#" className="btn btn-outline-secondary rounded-circle px-2 py-1"><FaLinkedinIn /></a>
                            <a href="#" className="btn btn-outline-secondary rounded-circle px-2 py-1"><FaInstagram /></a>
                        </div>
                    </div>

                    {/* Quick Links */}
                    <div className="col-lg-2 col-md-6 mb-4 mb-md-0">
                        <h5 className="fw-bold mb-3 text-white">Quick Links</h5>
                        <ul className="list-unstyled d-flex flex-column gap-2">
                            <li><Link to="/" className="text-secondary text-decoration-none hover-white">Home</Link></li>
                            <li><Link to="/register" className="text-secondary text-decoration-none hover-white">Join as Patient</Link></li>
                            <li><Link to="/login" className="text-secondary text-decoration-none hover-white">Portal Login</Link></li>
                            <li><a href="#" className="text-secondary text-decoration-none hover-white">Our Departments</a></li>
                        </ul>
                    </div>

                    {/* Services */}
                    <div className="col-lg-3 col-md-6 mb-4 mb-md-0">
                        <h5 className="fw-bold mb-3 text-white">Top Specialties</h5>
                        <ul className="list-unstyled d-flex flex-column gap-2">
                            <li><a href="#" className="text-secondary text-decoration-none hover-white">Cardiology Diagnostics</a></li>
                            <li><a href="#" className="text-secondary text-decoration-none hover-white">Neurology Therapy</a></li>
                            <li><a href="#" className="text-secondary text-decoration-none hover-white">Pediatric Outreach</a></li>
                            <li><a href="#" className="text-secondary text-decoration-none hover-white">Emergency Services</a></li>
                        </ul>
                    </div>

                    {/* Contact Info */}
                    <div className="col-lg-3 col-md-6">
                        <h5 className="fw-bold mb-3 text-white">Contact Us</h5>
                        <ul className="list-unstyled d-flex flex-column gap-3 text-secondary">
                            <li className="d-flex align-items-center gap-3">
                                <FaMapMarkerAlt className="text-primary" />
                                <span>123 Health Avenue, Med City, NY 10001</span>
                            </li>
                            <li className="d-flex align-items-center gap-3">
                                <FaPhoneAlt className="text-primary" />
                                <span>+1 (800) 123-4567</span>
                            </li>
                            <li className="d-flex align-items-center gap-3">
                                <FaEnvelope className="text-primary" />
                                <span>support@amazecare.com</span>
                            </li>
                        </ul>
                    </div>
                </div>

                <hr className="border-secondary" />

                <div className="text-center pt-2">
                    <p className="mb-0 text-secondary" style={{ fontSize: '0.9rem' }}>
                        &copy; {new Date().getFullYear()} AmazeCare Hospital Management System. All rights reserved. Designed for Excellence.
                    </p>
                </div>
            </div>

            <style jsx="true">{`
                .hover-white:hover {
                    color: white !important;
                    text-decoration: underline !important;
                    transition: all 0.2s ease;
                }
            `}</style>
        </footer>
    );
};

export default Footer;
