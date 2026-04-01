import React from 'react';
import { Link } from 'react-router-dom';
import { FaHeartbeat, FaStethoscope, FaAmbulance, FaUserMd, FaSyringe, FaPills } from 'react-icons/fa';

const Home = () => {
    return (
        <div className="home-container bg-light">
            {/* Hero Section Container */}
            <div className="hero-section text-center py-5 text-white mb-5 position-relative bg-primary" style={{ 
                minHeight: '500px',
                display: 'flex',
                alignItems: 'center',
                boxShadow: '0 4px 20px rgba(0,0,0,0.1)'
            }}>
                <div className="container position-relative z-index-1">
                    <span className="badge bg-light text-primary mb-3 px-3 py-2 rounded-pill shadow-sm fw-bold">Award Winning Healthcare</span>
                    <h1 className="display-2 fw-bolder mb-4 text-white shadow-sm-text" style={{ letterSpacing: '-1px' }}>Your Health, <br/>Our Masterpiece.</h1>
                    <p className="lead mb-5 mx-auto text-white fw-light" style={{ maxWidth: '750px', fontSize: '1.35rem', opacity: '0.95', textShadow: '0 1px 3px rgba(0,0,0,0.3)' }}>
                        Experience a new era of proactive healthcare. AmazeCare combines elite medical expertise with next-generation digital convenience to put your wellbeing first.
                    </p>
                    <div className="d-flex justify-content-center gap-3 flex-wrap">
                        <Link to="/register">
                            <button type="button" className="btn btn-primary btn-lg fw-bold px-5 py-3 rounded-pill shadow hover-scale" style={{ transition: 'transform 0.3s' }}>
                                Find a Doctor Today
                            </button>
                        </Link>
                        <Link to="/login">
                            <button type="button" className="btn btn-outline-light btn-lg fw-bold px-5 py-3 rounded-pill shadow-sm hover-scale" style={{ transition: 'transform 0.3s' }}>
                                Patient Portal
                            </button>
                        </Link>
                    </div>
                </div>
            </div>

            {/* Quick Stats Banner */}
            <div className="container mb-5">
                <div className="row g-4 justify-content-center">
                    <div className="col-md-10">
                        <div className="card shadow border-0 rounded-4 bg-white p-2">
                            <div className="card-body row text-center py-4">
                                <div className="col-md-4 border-end border-light">
                                    <h2 className="display-4 fw-bold text-primary mb-1">200+</h2>
                                    <p className="text-secondary fw-semibold mb-0">Specialist Doctors</p>
                                </div>
                                <div className="col-md-4 border-end border-light">
                                    <h2 className="display-4 fw-bold text-primary mb-1">50k+</h2>
                                    <p className="text-secondary fw-semibold mb-0">Happy Patients</p>
                                </div>
                                <div className="col-md-4">
                                    <h2 className="display-4 fw-bold text-primary mb-1">24/7</h2>
                                    <p className="text-secondary fw-semibold mb-0">Emergency Care</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Core Services Features */}
            <div className="container py-5 mb-5">
                <div className="text-center mb-5">
                    <h6 className="text-primary fw-bold text-uppercase tracking-wider">Our Services</h6>
                    <h2 className="display-5 fw-bold text-dark">Comprehensive Care Solutions</h2>
                    <div className="mx-auto bg-primary mt-3" style={{ height: '4px', width: '80px', borderRadius: '2px' }}></div>
                </div>

                <div className="row g-4">
                    {[
                        { icon: <FaUserMd size={40} className="text-primary" />, title: "Expert Specialists", desc: "Consult with board-certified physicians across 30+ distinct medical branches." },
                        { icon: <FaHeartbeat size={40} className="text-danger" />, title: "Cardiology Center", desc: "State-of-the-art diagnostic imaging and cardiovascular preventative care." },
                        { icon: <FaAmbulance size={40} className="text-warning" />, title: "Emergency Rapid Response", desc: "Round-the-clock emergency trauma centers with instant triage capabilities." },
                        { icon: <FaStethoscope size={40} className="text-primary" />, title: "General Medicine", desc: "Routine check-ups, diagnostics, and preventative consultations." },
                        { icon: <FaSyringe size={40} className="text-info" />, title: "Vaccination Drives", desc: "Comprehensive pediatric and adult immunization programs." },
                        { icon: <FaPills size={40} className="text-success" />, title: "In-House Pharmacy", desc: "Instantly fulfill digital prescriptions generated straight from your dashboard." },
                    ].map((service, index) => (
                        <div className="col-lg-4 col-md-6" key={index}>
                            <div className="card h-100 shadow-sm border-0 ui-card transition-hover p-4" style={{ borderRadius: '1.25rem', backgroundColor: '#ffffff' }}>
                                <div className="card-body">
                                    <div className="mb-4 bg-light d-inline-block p-3 rounded-circle" style={{ boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.05)' }}>
                                        {service.icon}
                                    </div>
                                    <h4 className="card-title fw-bolder mb-3 text-dark">{service.title}</h4>
                                    <p className="card-text text-secondary" style={{ fontSize: '1.05rem', lineHeight: '1.6' }}>
                                        {service.desc}
                                    </p>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            {/* Call to Action Bottom */}
            <div className="bg-primary text-white py-5 mt-4">
                <div className="container text-center py-4">
                    <h2 className="fw-bold mb-4">Ready to take control of your health?</h2>
                    <p className="lead mb-4 opacity-75 mx-auto" style={{ maxWidth: '600px' }}>Join thousand of patients who trust AmazeCare for their medical needs. Registration takes less than three minutes.</p>
                    <Link to="/register">
                        <button className="btn btn-primary btn-lg rounded-pill px-5 fw-bold hover-scale">Create Free Account</button>
                    </Link>
                </div>
            </div>

            <style jsx="true">{`
                .transition-hover {
                    transition: all 0.3s ease;
                }
                .transition-hover:hover {
                    transform: translateY(-8px);
                    box-shadow: 0 1.5rem 3rem rgba(0,0,0,.1)!important;
                    border-color: #2563eb !important;
                }
                .hover-scale {
                    transition: all 0.3s ease;
                }
                .hover-scale:hover {
                    transform: scale(1.03);
                    box-shadow: 0 10px 20px rgba(0,0,0,0.1);
                }
                .shadow-sm-text {
                    text-shadow: 0 2px 5px rgba(0,0,0,0.3);
                }
                .tracking-wider {
                    letter-spacing: 0.1em;
                }
            `}</style>
        </div>
    );
};

export default Home;
