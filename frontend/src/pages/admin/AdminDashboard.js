import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Table, Button, Tabs, Tab, Spinner, Modal, Form } from 'react-bootstrap';
import { useLocation, useNavigate } from 'react-router-dom';
import adminService from '../../services/adminService';

const AdminDashboard = () => {
    const location = useLocation();
    const navigate = useNavigate();
    
    const getActiveTab = () => {
        const path = location.pathname;
        if (path.includes('/patients')) return 'patients';
        if (path.includes('/appointments')) return 'appointments';
        return 'doctors';
    };

    const handleTabSelect = (key) => {
        navigate(`/admin/${key}`);
    };
    const [doctors, setDoctors] = useState([]);
    const [patients, setPatients] = useState([]);
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    
    // Edit Modal States
    const [showDocModal, setShowDocModal] = useState(false);
    const [showPatModal, setShowPatModal] = useState(false);
    const [currentDoc, setCurrentDoc] = useState({});
    const [currentPat, setCurrentPat] = useState({});

    // Add Modal States
    const [showAddDocModal, setShowAddDocModal] = useState(false);
    const [showAddPatModal, setShowAddPatModal] = useState(false);
    const [newDoc, setNewDoc] = useState({ name: '', specialty: '', experience: '' });
    const [newPat, setNewPat] = useState({ fullName: '', dateOfBirth: '', gender: '', mobileNumber: '' });

    const fetchData = () => {
        setLoading(true);
        Promise.all([
            adminService.getAllDoctors().catch(() => []),
            adminService.getAllPatients().catch(() => []),
            adminService.getAllAppointments().catch(() => [])
        ])
        .then(([docsResponse, patsResponse, appsResponse]) => {
            setDoctors(Array.isArray(docsResponse) ? docsResponse : docsResponse?.content || []);
            setPatients(Array.isArray(patsResponse) ? patsResponse : patsResponse?.content || []);
            setAppointments(Array.isArray(appsResponse) ? appsResponse : appsResponse?.content || []);
        })
        .catch(() => alert("Failed to fetch admin data."))
        .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchData();
        // eslint-disable-next-line
    }, []);

    // --- DELETE HANDLERS ---
    const handleDeleteDoctor = (id) => {
        if(window.confirm('Are you sure you want to delete this doctor?')) {
            adminService.deleteDoctor(id).then(() => {
                alert('Doctor removed successfully');
                fetchData();
            }).catch(() => alert('Error removing doctor'));
        }
    };
    const handleDeletePatient = (id) => {
        if(window.confirm('Are you sure you want to delete this patient?')) {
            adminService.deletePatient(id).then(() => {
                alert('Patient removed successfully');
                fetchData();
            }).catch(() => alert('Error removing patient'));
        }
    };

    // --- UPDATE HANDLERS ---
    const handleUpdateDoctor = (e) => {
        e.preventDefault();
        if (!currentDoc.name || currentDoc.name.length < 3) {
            alert("Doctor name must be at least 3 characters.");
            return;
        }
        if (!currentDoc.specialty) {
            alert("Please provide a specialization.");
            return;
        }
        if (currentDoc.experience < 0) {
            alert("Experience cannot be negative.");
            return;
        }
        adminService.updateDoctor(currentDoc.id, currentDoc).then(() => {
            alert("Doctor updated successfully");
            setShowDocModal(false);
            fetchData();
        }).catch(() => alert("Failed to update"));
    };
    const handleUpdatePatient = (e) => {
        e.preventDefault();
        if (!currentPat.fullName || currentPat.fullName.length < 3) {
            alert("Patient name must be at least 3 characters.");
            return;
        }
        if (!currentPat.mobileNumber || currentPat.mobileNumber.length < 10) {
            alert("Please provide a valid 10-digit mobile number.");
            return;
        }
        adminService.updatePatient(currentPat.id, currentPat).then(() => {
            alert("Patient updated successfully");
            setShowPatModal(false);
            fetchData();
        }).catch(() => alert("Failed to update"));
    };

    // --- ADD HANDLERS ---
    const handleAddDoctor = (e) => {
        e.preventDefault();
        if (!newDoc.name || newDoc.name.length < 3) {
            alert("Doctor name must be at least 3 characters.");
            return;
        }
        if (!newDoc.specialty) {
            alert("Please provide a specialization.");
            return;
        }
        if (newDoc.experience < 0) {
            alert("Experience cannot be negative.");
            return;
        }
        adminService.addDoctor(newDoc).then(() => {
            alert("New Doctor Created Successfully!");
            setShowAddDocModal(false);
            setNewDoc({ name: '', specialty: '', experience: '' });
            fetchData();
        }).catch(() => alert("Failed to add doctor"));
    };
    const handleAddPatient = (e) => {
        e.preventDefault();
        if (!newPat.fullName || newPat.fullName.length < 3) {
            alert("Patient name must be at least 3 characters.");
            return;
        }
        if (!newPat.mobileNumber || newPat.mobileNumber.length < 10) {
            alert("Please provide a valid 10-digit mobile number.");
            return;
        }
        adminService.addPatient(newPat).then(() => {
            alert("New Patient Created Successfully!");
            setShowAddPatModal(false);
            setNewPat({ fullName: '', dateOfBirth: '', gender: '', mobileNumber: '' });
            fetchData();
        }).catch(() => alert("Failed to add patient"));
    };

    if (loading) return <div className="text-center mt-5"><Spinner animation="border" /></div>;

    return (
        <Container>
            <h2 className="mb-4">Admin Dashboard</h2>
            <Row className="mb-4">
                <Col md={4}>
                    <Card border="primary" className="text-center shadow-sm">
                        <Card.Body>
                            <Card.Title>Total Doctors</Card.Title>
                            <Card.Text className="fs-1 fw-bold text-primary">{doctors.length}</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card border="success" className="text-center shadow-sm">
                        <Card.Body>
                            <Card.Title>Total Patients</Card.Title>
                            <Card.Text className="fs-1 fw-bold text-success">{patients.length}</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card border="info" className="text-center shadow-sm">
                        <Card.Body>
                            <Card.Title>System Status</Card.Title>
                            <Card.Text className="fs-4 fw-bold text-info mt-3">Online</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Tabs activeKey={getActiveTab()} onSelect={handleTabSelect} className="mb-3">
                <Tab eventKey="doctors" title="Manage Doctors">
                    <div className="d-flex justify-content-end mb-3">
                        <Button variant="success" onClick={() => setShowAddDocModal(true)}>+ Add New Doctor</Button>
                    </div>
                    <Table striped bordered hover responsive>
                        <thead className="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Specialization</th>
                                <th>Experience (Yrs)</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {doctors.map(doc => (
                                <tr key={doc.id}>
                                    <td>{doc.id}</td>
                                    <td>{doc.name || 'N/A'}</td>
                                    <td>{doc.specialty || 'N/A'}</td>
                                    <td>{doc.experience || 'N/A'}</td>
                                    <td>
                                        <Button variant="info" size="sm" className="me-2" onClick={() => { setCurrentDoc(doc); setShowDocModal(true); }}>Edit</Button>
                                        <Button variant="danger" size="sm" onClick={() => handleDeleteDoctor(doc.id)}>Remove</Button>
                                    </td>
                                </tr>
                            ))}
                            {doctors.length === 0 && <tr><td colSpan="5" className="text-center">No doctors found</td></tr>}
                        </tbody>
                    </Table>
                </Tab>

                <Tab eventKey="patients" title="Manage Patients">
                    <div className="d-flex justify-content-end mb-3">
                        <Button variant="success" onClick={() => setShowAddPatModal(true)}>+ Add New Patient</Button>
                    </div>
                    <Table striped bordered hover responsive>
                        <thead className="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Date of Birth</th>
                                <th>Gender</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {patients.map(pat => (
                                <tr key={pat.id}>
                                    <td>{pat.id}</td>
                                    <td>{pat.fullName || 'N/A'}</td>
                                    <td>{pat.dateOfBirth || 'N/A'}</td>
                                    <td>{pat.gender || 'N/A'}</td>
                                    <td>
                                        <Button variant="info" size="sm" className="me-2" onClick={() => { setCurrentPat(pat); setShowPatModal(true); }}>Edit</Button>
                                        <Button variant="danger" size="sm" onClick={() => handleDeletePatient(pat.id)}>Remove</Button>
                                    </td>
                                </tr>
                            ))}
                             {patients.length === 0 && <tr><td colSpan="5" className="text-center">No patients found</td></tr>}
                        </tbody>
                    </Table>
                </Tab>

                <Tab eventKey="appointments" title="All Appointments">
                    <Table striped bordered hover responsive>
                        <thead className="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Date & Time</th>
                                <th>Doctor</th>
                                <th>Patient</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {appointments.map(app => (
                                <tr key={app.id}>
                                    <td>{app.id}</td>
                                    <td>{app.appointmentDate} {app.appointmentTime || ''}</td>
                                    <td>{app.doctor?.name || 'N/A'}</td>
                                    <td>{app.patient?.fullName || 'N/A'}</td>
                                    <td>
                                        <span className={`badge bg-${app.status === 'SCHEDULED' ? 'primary' : app.status === 'COMPLETED' ? 'success' : 'danger'}`}>
                                            {app.status}
                                        </span>
                                    </td>
                                </tr>
                            ))}
                            {appointments.length === 0 && <tr><td colSpan="5" className="text-center">No appointments found</td></tr>}
                        </tbody>
                    </Table>
                </Tab>
            </Tabs>

            {/* --- ADD DOCTOR MODAL --- */}
            <Modal show={showAddDocModal} onHide={() => setShowAddDocModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Add New Doctor Profile</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleAddDoctor}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Name</Form.Label>
                            <Form.Control type="text" value={newDoc.name} onChange={e => setNewDoc({...newDoc, name: e.target.value})} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Specialization</Form.Label>
                            <Form.Control type="text" value={newDoc.specialty} onChange={e => setNewDoc({...newDoc, specialty: e.target.value})} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Experience (Years)</Form.Label>
                            <Form.Control type="number" value={newDoc.experience} onChange={e => setNewDoc({...newDoc, experience: e.target.value})} required />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowAddDocModal(false)}>Cancel</Button>
                        <Button variant="success" type="submit">Create Doctor</Button>
                    </Modal.Footer>
                </Form>
            </Modal>

            {/* --- UPDATE DOCTOR MODAL --- */}
            <Modal show={showDocModal} onHide={() => setShowDocModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Edit Doctor</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleUpdateDoctor}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Name</Form.Label>
                            <Form.Control type="text" value={currentDoc.name || ''} onChange={e => setCurrentDoc({...currentDoc, name: e.target.value})} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Specialization</Form.Label>
                            <Form.Control type="text" value={currentDoc.specialty || ''} onChange={e => setCurrentDoc({...currentDoc, specialty: e.target.value})} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Experience</Form.Label>
                            <Form.Control type="number" value={currentDoc.experience || ''} onChange={e => setCurrentDoc({...currentDoc, experience: e.target.value})} required />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowDocModal(false)}>Close</Button>
                        <Button variant="primary" type="submit">Save Changes</Button>
                    </Modal.Footer>
                </Form>
            </Modal>

            {/* --- ADD PATIENT MODAL --- */}
            <Modal show={showAddPatModal} onHide={() => setShowAddPatModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Add New Patient Profile</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleAddPatient}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Full Name</Form.Label>
                            <Form.Control type="text" value={newPat.fullName} onChange={e => setNewPat({...newPat, fullName: e.target.value})} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Date of Birth</Form.Label>
                            <Form.Control type="date" value={newPat.dateOfBirth} onChange={e => setNewPat({...newPat, dateOfBirth: e.target.value})} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Gender</Form.Label>
                            <Form.Select value={newPat.gender} onChange={e => setNewPat({...newPat, gender: e.target.value})} required>
                                <option value="">Select...</option>
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                                <option value="OTHER">Other</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Contact Number</Form.Label>
                            <Form.Control type="text" value={newPat.mobileNumber} onChange={e => setNewPat({...newPat, mobileNumber: e.target.value})} required />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowAddPatModal(false)}>Cancel</Button>
                        <Button variant="success" type="submit">Create Patient</Button>
                    </Modal.Footer>
                </Form>
            </Modal>

            {/* --- UPDATE PATIENT MODAL --- */}
            <Modal show={showPatModal} onHide={() => setShowPatModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Edit Patient</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleUpdatePatient}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Full Name</Form.Label>
                            <Form.Control type="text" value={currentPat.fullName || ''} onChange={e => setCurrentPat({...currentPat, fullName: e.target.value})} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Date of Birth</Form.Label>
                            <Form.Control type="date" value={currentPat.dateOfBirth || ''} onChange={e => setCurrentPat({...currentPat, dateOfBirth: e.target.value})} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Contact Number</Form.Label>
                            <Form.Control type="text" value={currentPat.mobileNumber || ''} onChange={e => setCurrentPat({...currentPat, mobileNumber: e.target.value})} required />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowPatModal(false)}>Close</Button>
                        <Button variant="primary" type="submit">Save Changes</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
            
        </Container>
    );
};

export default AdminDashboard;
