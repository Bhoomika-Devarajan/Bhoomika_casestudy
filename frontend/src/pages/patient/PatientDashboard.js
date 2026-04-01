import React, { useState, useEffect, useContext } from 'react';
import { Container, Row, Col, Card, Tabs, Tab, Form, Button, Table, Spinner, Modal } from 'react-bootstrap';
import { useLocation, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import patientService from '../../services/patientService';

const PatientDashboard = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const getActiveTab = () => {
        const path = location.pathname;
        if (path.includes('/completed')) return 'completed';
        if (path.includes('/book')) return 'book';
        if (path.includes('/records')) return 'records';
        if (path.includes('/profile')) return 'profile';
        return 'upcoming';
    };

    const handleTabSelect = (key) => {
        navigate(`/patient/${key}`);
    };

    const { user } = useContext(AuthContext);
    const [patientData, setPatientData] = useState(null);
    const [doctors, setDoctors] = useState([]);
    
    // Split appointments state
    const [upcomingAppointments, setUpcomingAppointments] = useState([]);
    const [completedAppointments, setCompletedAppointments] = useState([]);
    const [records, setRecords] = useState([]);
    const [loading, setLoading] = useState(true);

    // Profile form state
    const [profileForm, setProfileForm] = useState({ fullName: '', dateOfBirth: '', mobileNumber: '', address: '' });

    // Modals
    const [showReschedule, setShowReschedule] = useState(false);
    const [rescheduleData, setRescheduleData] = useState({ appointmentId: '', appointmentDate: '', symptoms: '' });

    // Form state for booking
    const [booking, setBooking] = useState({
        doctorId: '',
        appointmentDate: '',
        appointmentTime: '10:00',
        visitType: 'Inperson',
        reason: ''
    });

    const loadInitialData = () => {
        setLoading(true);
        patientService.getPatientProfile(user.username)
            .then(profile => {
                setPatientData(profile);
                if (profile) {
                    setProfileForm({
                        fullName: profile.fullName || '',
                        dateOfBirth: profile.dateOfBirth || '',
                        mobileNumber: profile.mobileNumber || '',
                        address: profile.address || ''
                    });
                }
                
                patientService.getDoctors().then(docs => {
                    setDoctors(Array.isArray(docs) ? docs : docs.content || []);
                }).catch(err => console.warn(err));

                if (profile && profile.id) {
                    loadAppointments(profile.id);
                    
                    patientService.getMedicalRecords(profile.id).then(recs => {
                        setRecords(Array.isArray(recs) ? recs : recs.content || []);
                    }).catch(err => console.warn(err));
                }
            })
            .catch(error => {
                console.warn('Error fetching patient data', error);
            })
            .finally(() => {
                setLoading(false);
            });
    };

    const loadAppointments = (id) => {
        patientService.viewUpcomingAppointments(id).then(upApps => {
            setUpcomingAppointments(Array.isArray(upApps) ? upApps : upApps.content || []);
        }).catch(() => {});
        
        patientService.viewCompletedAppointments(id).then(compApps => {
            setCompletedAppointments(Array.isArray(compApps) ? compApps : compApps.content || []);
        }).catch(() => {});
    };

    useEffect(() => {
        if (user) loadInitialData();
        // eslint-disable-next-line
    }, [user]);

    // Handlers
    const handleProfileUpdate = (e) => {
        e.preventDefault();
        if (!profileForm.fullName || profileForm.fullName.length < 3) {
            alert("Name must be at least 3 characters.");
            return;
        }
        if (profileForm.mobileNumber && profileForm.mobileNumber.length < 10) {
            alert("Contact number should be valid (at least 10 digits).");
            return;
        }
        patientService.updatePatientProfile(patientData.id, { ...patientData, ...profileForm })
            .then(() => {
                alert("Profile updated successfully!");
                loadInitialData(); // reload
            })
            .catch(() => alert("Failed to update profile."));
    };

    const handleBookingChange = (e) => {
        setBooking({ ...booking, [e.target.name]: e.target.value });
    };

    const handleBookAppointment = (e) => {
        e.preventDefault();
        if (!booking.doctorId) {
            alert("Please select a doctor to book the appointment.");
            return;
        }
        if (!booking.appointmentDate) {
            alert("Please select a valid appointment date.");
            return;
        }
        if (!booking.reason || booking.reason.length < 5) {
            alert("Please provide a valid reason or symptom description (at least 5 characters).");
            return;
        }
        
        const appointmentData = {
            patientId: patientData?.id,
            doctorId: booking.doctorId,
            appointmentDate: booking.appointmentDate,
            appointmentTime: booking.appointmentTime,
            visitType: booking.visitType,
            symptoms: booking.reason,
            status: 'REQUESTED'
        };
        patientService.bookAppointment(appointmentData)
            .then(() => {
                alert("Appointment booked successfully!");
                if (patientData?.id) loadAppointments(patientData.id);
                setBooking({ doctorId: '', appointmentDate: '', appointmentTime: '10:00', visitType: 'Inperson', reason: '' }); // Reset form
            })
            .catch(() => alert("Failed to book appointment."));
    };

    const handleCancelAppointment = (appId) => {
        if (window.confirm("Are you sure you want to cancel this appointment?")) {
            patientService.cancelAppointment(appId)
                .then(() => {
                    alert("Appointment Cancelled.");
                    if (patientData?.id) loadAppointments(patientData.id);
                })
                .catch(() => alert("Failed to cancel."));
        }
    };

    const submitReschedule = (e) => {
        e.preventDefault();
        
        if (!rescheduleData.appointmentDate) {
            alert("Please select a valid new appointment date to reschedule.");
            return;
        }
        
        patientService.rescheduleAppointment(rescheduleData.appointmentId, {
            appointmentDate: rescheduleData.appointmentDate,
            symptoms: rescheduleData.symptoms
        })
        .then(() => {
            alert("Appointment Rescheduled!");
            setShowReschedule(false);
            if (patientData?.id) loadAppointments(patientData.id);
        })
        .catch(() => alert("Failed to reschedule."));
    };

    if (loading) return <div className="text-center mt-5"><Spinner animation="border" /></div>;

    return (
        <Container>
            <h2 className="mb-4">Patient Portal</h2>
            <Row className="mb-4">
                <Col>
                    <Card className="shadow-sm border-0 bg-primary text-white">
                        <Card.Body>
                            <Card.Title>Welcome, {patientData?.fullName || user?.username}</Card.Title>
                            <Card.Text>Manage your health, appointments, and records seamlessly.</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Tabs activeKey={getActiveTab()} onSelect={handleTabSelect} className="mb-3">
                <Tab eventKey="upcoming" title="Upcoming Appointments">
                    <Table striped bordered hover responsive>
                        <thead className="table-dark">
                            <tr>
                                <th>Date</th>
                                <th>Doctor</th>
                                <th>Symptoms</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {upcomingAppointments.map(app => (
                                <tr key={app.id}>
                                    <td>{app.appointmentDate || 'N/A'}</td>
                                    <td>{app.doctor?.name || 'Assigned Doctor'}</td>
                                    <td>{app.symptoms}</td>
                                    <td>
                                        <span className={`badge bg-${
                                            app.status === 'CONFIRMED' ? 'success' :
                                            app.status === 'REQUESTED' ? 'warning' :
                                            app.status === 'REJECTED' ? 'danger' : 'secondary'
                                        }`}>
                                            {app.status}
                                        </span>
                                    </td>
                                    <td>
                                        {(app.status === 'REQUESTED' || app.status === 'CONFIRMED') && (
                                            <>
                                                <Button variant="info" size="sm" className="me-2" onClick={() => {
                                                    setRescheduleData({ appointmentId: app.id, appointmentDate: app.appointmentDate || '', symptoms: app.symptoms || '' });
                                                    setShowReschedule(true);
                                                }}>Reschedule</Button>
                                                <Button variant="danger" size="sm" onClick={() => handleCancelAppointment(app.id)}>Cancel</Button>
                                            </>
                                        )}
                                    </td>
                                </tr>
                            ))}
                            {upcomingAppointments.length === 0 && <tr><td colSpan="5" className="text-center">No upcoming appointments.</td></tr>}
                        </tbody>
                    </Table>
                </Tab>

                <Tab eventKey="completed" title="Completed Appointments">
                    <Table striped bordered hover responsive>
                        <thead className="table-dark">
                            <tr>
                                <th>Date</th>
                                <th>Doctor</th>
                                <th>Symptoms</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {completedAppointments.map(app => (
                                <tr key={app.id}>
                                    <td>{app.appointmentDate || 'N/A'}</td>
                                    <td>{app.doctor?.name || 'Assigned Doctor'}</td>
                                    <td>{app.symptoms}</td>
                                    <td>
                                        <span className="badge bg-success">{app.status}</span>
                                    </td>
                                </tr>
                            ))}
                            {completedAppointments.length === 0 && <tr><td colSpan="4" className="text-center">No completed appointments.</td></tr>}
                        </tbody>
                    </Table>
                </Tab>

                <Tab eventKey="book" title="Book Appointment">
                    <Card className="shadow-sm border-0">
                        <Card.Body>
                            <Card.Title className="mb-4">Schedule a New Visit</Card.Title>
                            <Form onSubmit={handleBookAppointment}>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Select Doctor</Form.Label>
                                            <Form.Select name="doctorId" value={booking.doctorId} onChange={handleBookingChange} required>
                                                <option value="">Choose...</option>
                                                {doctors.map(doc => (
                                                    <option key={doc.id} value={doc.id}>
                                                        Dr. {doc.name} ({doc.specialty || 'General'})
                                                    </option>
                                                ))}
                                            </Form.Select>
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Appointment Date</Form.Label>
                                            <Form.Control type="date" name="appointmentDate" value={booking.appointmentDate} onChange={handleBookingChange} required />
                                        </Form.Group>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Appointment Time</Form.Label>
                                            <Form.Control type="time" name="appointmentTime" value={booking.appointmentTime} onChange={handleBookingChange} required />
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Visit Type</Form.Label>
                                            <Form.Select name="visitType" value={booking.visitType} onChange={handleBookingChange} required>
                                                <option value="Inperson">In-Person</option>
                                                <option value="Online">Online / Consultation</option>
                                            </Form.Select>
                                        </Form.Group>
                                    </Col>
                                </Row>
                                <Form.Group className="mb-3">
                                    <Form.Label>Symptoms / Reason for Visit</Form.Label>
                                    <Form.Control as="textarea" rows={3} name="reason" value={booking.reason} onChange={handleBookingChange} required placeholder="Briefly describe your symptoms" />
                                </Form.Group>
                                <Button variant="primary" type="submit">Book Appointment</Button>
                            </Form>
                        </Card.Body>
                    </Card>
                </Tab>

                <Tab eventKey="records" title="Medical Records">
                    <Table striped bordered hover responsive>
                        <thead className="table-dark">
                            <tr>
                                <th>Date</th>
                                <th>Symptoms</th>
                                <th>Diagnosis</th>
                                <th>Treatment Plan</th>
                                <th>Prescriptions</th>
                                <th>Doctor</th>
                            </tr>
                        </thead>
                        <tbody>
                            {records.map(rec => (
                                <tr key={rec.id}>
                                    <td>{rec.recordDate || (rec.createdAt ? new Date(rec.createdAt).toLocaleDateString() : 'N/A')}</td>
                                    <td><small>{rec.currentSymptoms || 'None'}</small></td>
                                    <td><strong>{rec.diagnosis}</strong></td>
                                    <td><small>{rec.treatmentPlan || 'General Advice'}</small></td>
                                    <td>
                                        {rec.prescriptions && rec.prescriptions.map((p, i) => (
                                            <div key={i} className="small text-muted">• {p.medicineName} ({p.morning}-{p.afternoon}-{p.evening})</div>
                                        ))}
                                        {(!rec.prescriptions || rec.prescriptions.length === 0) && (rec.prescription || 'N/A')}
                                    </td>
                                    <td>{rec.appointment?.doctor?.name || 'Assigned Doctor'}</td>
                                </tr>
                            ))}
                            {records.length === 0 && <tr><td colSpan="4" className="text-center">No medical records found.</td></tr>}
                        </tbody>
                    </Table>
                </Tab>

                <Tab eventKey="profile" title="My Profile">
                    <Card className="shadow-sm border-0">
                        <Card.Body>
                            <Card.Title className="mb-4">Update Personal Information</Card.Title>
                            <Form onSubmit={handleProfileUpdate}>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Full Name</Form.Label>
                                            <Form.Control type="text" value={profileForm.fullName} onChange={e => setProfileForm({...profileForm, fullName: e.target.value})} required />
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Date of Birth</Form.Label>
                                            <Form.Control type="date" value={profileForm.dateOfBirth} onChange={e => setProfileForm({...profileForm, dateOfBirth: e.target.value})} />
                                        </Form.Group>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Contact Number</Form.Label>
                                            <Form.Control type="text" value={profileForm.mobileNumber} onChange={e => setProfileForm({...profileForm, mobileNumber: e.target.value})} />
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>Home Address</Form.Label>
                                            <Form.Control type="text" value={profileForm.address} onChange={e => setProfileForm({...profileForm, address: e.target.value})} />
                                        </Form.Group>
                                    </Col>
                                </Row>
                                <Button variant="success" type="submit">Save Profile Changes</Button>
                            </Form>
                        </Card.Body>
                    </Card>
                </Tab>
            </Tabs>

            {/* Reschedule Modal */}
            <Modal show={showReschedule} onHide={() => setShowReschedule(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Reschedule Appointment</Modal.Title>
                </Modal.Header>
                <Form onSubmit={submitReschedule}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>New Appointment Date</Form.Label>
                            <Form.Control type="date" value={rescheduleData.appointmentDate} onChange={e => setRescheduleData({...rescheduleData, appointmentDate: e.target.value})} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Symptoms (Optional Change)</Form.Label>
                            <Form.Control as="textarea" rows={2} value={rescheduleData.symptoms} onChange={e => setRescheduleData({...rescheduleData, symptoms: e.target.value})} />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowReschedule(false)}>Cancel</Button>
                        <Button variant="primary" type="submit">Confirm Reschedule</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </Container>
    );
};

export default PatientDashboard;
