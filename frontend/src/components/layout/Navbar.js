import React, { useContext, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';

const AppNavbar = () => {
    const { user, logout } = useContext(AuthContext);
    const navigate = useNavigate();
    const [isNavCollapsed, setIsNavCollapsed] = useState(true);

    const handleNavCollapse = () => setIsNavCollapsed(!isNavCollapsed);

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const getDashboardLink = () => {
        if (!user) return '/';
        const role = String(user.role).toUpperCase();
        if (role.includes('ADMIN')) return '/admin/doctors';
        if (role.includes('DOCTOR')) return '/doctor/upcoming';
        if (role.includes('PATIENT')) return '/patient/upcoming';
        return '/';
    };

    const navbarStyle = {
        background: 'linear-gradient(135deg, #0d47a1 0%, #1976d2 100%)',
        boxShadow: '0 4px 20px rgba(0, 0, 0, 0.15)',
        padding: '0.8rem 0'
    };

    return (
        <>
            <style jsx="true">{`
                .nav-link-custom {
                    color: rgba(255, 255, 255, 0.9) !important;
                    font-weight: 500;
                    letter-spacing: 0.5px;
                    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
                    margin: 0 5px;
                    border-radius: 8px;
                    padding: 8px 16px !important;
                }
                .nav-link-custom:hover {
                    color: #ffffff !important;
                    background: rgba(255, 255, 255, 0.15);
                    transform: translateY(-2px);
                    box-shadow: 0 4px 8px rgba(0,0,0,0.1);
                }
                .navbar-brand-custom {
                    font-size: 1.6rem;
                    font-weight: 800;
                    letter-spacing: 1px;
                    color: #ffffff !important;
                    text-shadow: 0 2px 4px rgba(0,0,0,0.2);
                    transition: transform 0.3s ease;
                    display: flex;
                    align-items: center;
                    gap: 8px;
                }
                .navbar-brand-custom:hover {
                    transform: scale(1.05);
                }
                .btn-auth-custom {
                    border: 2px solid rgba(255, 255, 255, 0.6);
                    border-radius: 25px;
                    padding: 6px 24px;
                    font-weight: 600;
                    background: transparent;
                    color: #fff;
                    transition: all 0.3s ease;
                }
                .btn-auth-custom:hover {
                    background: rgba(255, 255, 255, 0.2);
                    border-color: #ffffff;
                    transform: scale(1.05);
                    color: #fff;
                }
                .btn-logout-custom {
                    background: rgba(255, 255, 255, 0.15);
                    border: 1px solid rgba(255, 255, 255, 0.3);
                }
            `}</style>
            
            <nav className="navbar navbar-expand-lg navbar-dark sticky-top" style={navbarStyle}>
                <div className="container">
                    <Link className="navbar-brand navbar-brand-custom" to={getDashboardLink()}>
                        <span style={{ fontSize: '1.8rem' }}>🏥</span> AmazeCare
                    </Link>
                    <button 
                        className="navbar-toggler border-0 shadow-none" 
                        type="button" 
                        aria-expanded={!isNavCollapsed}
                        aria-label="Toggle navigation"
                        onClick={handleNavCollapse}
                    >
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className={`${isNavCollapsed ? 'collapse' : ''} navbar-collapse mt-3 mt-lg-0`} id="navbarNav">
                        <ul className="navbar-nav me-auto">
                            <li className="nav-item">
                                <Link className="nav-link nav-link-custom" to={getDashboardLink()} onClick={() => setIsNavCollapsed(true)}>Home</Link>
                            </li>
                            {user && user.role?.includes('PATIENT') && (
                                <>
                                    <li className="nav-item">
                                        <Link className="nav-link nav-link-custom" to="/patient/upcoming" onClick={() => setIsNavCollapsed(true)}>My Appointments</Link>
                                    </li>
                                    <li className="nav-item">
                                        <Link className="nav-link nav-link-custom" to="/patient/records" onClick={() => setIsNavCollapsed(true)}>Medical Records</Link>
                                    </li>
                                </>
                            )}
                            {user && user.role?.includes('DOCTOR') && (
                                <>
                                    <li className="nav-item">
                                        <Link className="nav-link nav-link-custom" to="/doctor/upcoming" onClick={() => setIsNavCollapsed(true)}>Schedule</Link>
                                    </li>
                                    <li className="nav-item">
                                        <Link className="nav-link nav-link-custom" to="/doctor/history" onClick={() => setIsNavCollapsed(true)}>Patients History</Link>
                                    </li>
                                </>
                            )}
                            {user && user.role?.includes('ADMIN') && (
                                <>
                                    <li className="nav-item">
                                        <Link className="nav-link nav-link-custom" to="/admin/doctors" onClick={() => setIsNavCollapsed(true)}>Doctors</Link>
                                    </li>
                                    <li className="nav-item">
                                        <Link className="nav-link nav-link-custom" to="/admin/patients" onClick={() => setIsNavCollapsed(true)}>Patients</Link>
                                    </li>
                                </>
                            )}
                        </ul>
                        <ul className="navbar-nav align-items-lg-center mt-3 mt-lg-0 gap-2">
                            {user ? (
                                <li className="nav-item">
                                    <button className="btn btn-auth-custom btn-logout-custom w-100" onClick={handleLogout}>
                                        Logout ({user.username})
                                    </button>
                                </li>
                            ) : (
                                <>
                                    <li className="nav-item">
                                        <Link className="nav-link nav-link-custom text-center" to="/login" onClick={() => setIsNavCollapsed(true)}>Login</Link>
                                    </li>
                                    <li className="nav-item">
                                        <Link className="btn btn-auth-custom w-100 text-center" to="/register" onClick={() => setIsNavCollapsed(true)}>Register</Link>
                                    </li>
                                </>
                            )}
                        </ul>
                    </div>
                </div>
            </nav>
        </>
    );
};

export default AppNavbar;
