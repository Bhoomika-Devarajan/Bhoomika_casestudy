import React, { useState, useEffect, useContext } from 'react';
import { Container, Row, Col, Card, Tabs, Tab, Table, Button, Form, Modal, Spinner } from 'react-bootstrap';
import { useLocation, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import doctorService from '../../services/doctorService';

const DoctorDashboard = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const getActiveTab = () => {
        const path = location.pathname;
        if (path.includes('/completed')) return 'completed';
        if (path.includes('/history')) return 'history';
        return 'upcoming';
    };

    const handleTabSelect = (key) => {
        navigate(`/doctor/${key}`);
    };

    const { user } = useContext(AuthContext);
    const [doctorData, setDoctorData] = useState(null);
    const [upcomingAppointments, setUpcomingAppointments] = useState([]);
    const [completedAppointments, setCompletedAppointments] = useState([]);
    const [loading, setLoading] = useState(true);

    // Modal state for adding consultation attached to an appointment
    const [showConsultModal, setShowConsultModal] = useState(false);
    const [consultAppId, setConsultAppId] = useState(null);
    const [recordForm, setRecordForm] = useState({ 
        diagnosis: '', 
        currentSymptoms: '', 
        physicalExamination: '', 
        treatmentPlan: '', 
        recommendedTests: '',
        prescriptions: [] 
    });

    const [newPresc, setNewPresc] = useState({
        medicineName: '',
        morning: 1,
        afternoon: 0,
        evening: 1,
        foodInstruction: 'AF',
        durationDays: 5
    });

    // Patient History State
    const [searchPatientId, setSearchPatientId] = useState('');
    const [patientHistory, setPatientHistory] = useState([]);

    const loadInitialData = () => {
        setLoading(true);
        doctorService.getDoctorProfile(user.username)
            .then(profile => {
                setDoctorData(profile);
                if (profile && profile.id) {
                    loadAppointments(profile.id);
                }
            })
            .catch(error => console.warn('Error fetching doctor data', error))
            .finally(() => setLoading(false));
    };

    const loadAppointments = (doctorId) => {
        doctorService.viewUpcomingAppointments(doctorId).then(upApps => {
            setUpcomingAppointments(Array.isArray(upApps) ? upApps : upApps.content || []);
        }).catch(() => {});
        
        doctorService.viewCompletedAppointments(doctorId).then(compApps => {
            const apps = Array.isArray(compApps) ? compApps : compApps.content || [];
            if (apps.length === 0) {
                setCompletedAppointments([]);
                return;
            }
            Promise.all(apps.map(app => 
                doctorService.getMedicalRecordByAppointment(app.id)
                    .then(record => { return { ...app, medicalRecord: record }; })
                    .catch(() => { return app; })
            )).then(detailedApps => {
                setCompletedAppointments(detailedApps);
            });
        }).catch(() => {});
    };

    useEffect(() => {
        if (user) loadInitialData();
        // eslint-disable-next-line
    }, [user]);

    // Action Handlers
    const handleAction = (actionFn, appointmentId, successMsg) => {
        actionFn(appointmentId)
            .then(() => {
                alert(successMsg);
                if (doctorData?.id) loadAppointments(doctorData.id);
            })
            .catch(() => alert("Action failed to process."));
    };

    const handleOpenConsultModal = (appId) => {
        setConsultAppId(appId);
        setShowConsultModal(true);
    };

    const handleCloseConsultModal = () => {
        setShowConsultModal(false);
        setConsultAppId(null);
        setRecordForm({ 
            diagnosis: '', 
            currentSymptoms: '', 
            physicalExamination: '', 
            treatmentPlan: '', 
            recommendedTests: '',
            prescriptions: [] 
        });
    };

    const addPrescriptionToForm = () => {
        if(!newPresc.medicineName) return alert("Medicine name required");
        
        // Ensure values are numbers and handle empty strings as 0
        const sanitizedPresc = {
            ...newPresc,
            morning: isNaN(newPresc.morning) ? 0 : newPresc.morning,
            afternoon: isNaN(newPresc.afternoon) ? 0 : newPresc.afternoon,
            evening: isNaN(newPresc.evening) ? 0 : newPresc.evening,
            durationDays: isNaN(newPresc.durationDays) ? 1 : newPresc.durationDays
        };

        setRecordForm(prev => ({
            ...prev,
            prescriptions: [...prev.prescriptions, sanitizedPresc]
        }));
        setNewPresc({ medicineName: '', morning: 1, afternoon: 0, evening: 1, foodInstruction: 'AF', durationDays: 5 });
    };

    const removePrescriptionFromForm = (index) => {
        setRecordForm(prev => ({
            ...prev,
            prescriptions: prev.prescriptions.filter((_, i) => i !== index)
        }));
    };

    const handleAddConsultation = (e) => {
        e.preventDefault();
        
        if (!recordForm.diagnosis || recordForm.diagnosis.length < 3) {
            alert("Please provide a valid clinical diagnosis (at least 3 characters).");
            return;
        }
        
        const recordDTO = {
            diagnosis: recordForm.diagnosis,
            currentSymptoms: recordForm.currentSymptoms,
            physicalExamination: recordForm.physicalExamination,
            treatmentPlan: recordForm.treatmentPlan,
            recommendedTests: recordForm.recommendedTests,
            prescriptions: recordForm.prescriptions
        };
        
        doctorService.addConsultationDetails(consultAppId, recordDTO)
            .then(() => {
                alert("Consultation Record Added Successfully!");
                doctorService.completeAppointment(consultAppId).then(() => {
                    handleCloseConsultModal();
                    if (doctorData?.id) loadAppointments(doctorData.id);
                });
            })
            .catch(() => alert("Failed to add consultation details."));
    };

    const searchPatientHistory = (e) => {
        e.preventDefault();
        if(!searchPatientId || searchPatientId <= 0) {
            alert("Please enter a valid, strictly positive Patient ID associated with the hospital system.");
            return;
        }
        doctorService.viewPatientHistory(searchPatientId)
            .then(history => {
                const results = Array.isArray(history) ? history : history.content || [];
                if (results.length === 0) {
                    alert("Doctor doesn't have an appointment with this patient ID");
                }
                setPatientHistory(results);
            })
            .catch(() => alert("Doctor doesn't have an appointment with this patient ID"));
    };

    if (loading) return <div className="text-center mt-5"><Spinner animation="border" /></div>;

    return (
        <Container>
            <h2 className="mb-4">Doctor Portal</h2>
            <Row className="mb-4">
                <Col>
                    <Card className="shadow-sm border-0 bg-primary text-white">
                        <Card.Body>
                            <Card.Title>Welcome, {doctorData?.name || user?.username}</Card.Title>
                            <Card.Text>Specialization: {doctorData?.specialty || 'General'} | Managing your patients securely.</Card.Text>
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
                                <th>Patient Name</th>
                                <th>Patient ID</th>
                                <th>Symptoms</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {upcomingAppointments.map(app => (
                                <tr key={app.id}>
                                    <td>{app.appointmentDate || 'N/A'}</td>
                                    <td>{app.patient?.fullName || 'Unknown'}</td>
                                    <td>{app.patient?.id || 'N/A'}</td>
                                    <td>{app.symptoms}</td>
                                    <td>
                                        <span className={`badge bg-${
                                            app.status === 'REQUESTED' ? 'warning' :
                                            app.status === 'CONFIRMED' ? 'success' :
                                            app.status === 'REJECTED' ? 'danger' : 'secondary'
                                        }`}>
                                            {app.status}
                                        </span>
                                    </td>
                                    <td>
                                        {app.status === 'REQUESTED' && (
                                            <>
                                                <Button variant="info" size="sm" className="me-2" onClick={() => handleAction(doctorService.confirmAppointment, app.id, 'Appointment Confirmed!')}>Confirm</Button>
                                                <Button variant="danger" size="sm" onClick={() => handleAction(doctorService.rejectAppointment, app.id, 'Appointment Rejected.')}>Reject</Button>
                                            </>
                                        )}
                                        {app.status === 'CONFIRMED' && (
                                            <Button variant="success" size="sm" onClick={() => handleOpenConsultModal(app.id)}>Attend &amp; Add Record</Button>
                                        )}
                                    </td>
                                </tr>
                            ))}
                            {upcomingAppointments.length === 0 && <tr><td colSpan="6" className="text-center">No upcoming appointments.</td></tr>}
                        </tbody>
                    </Table>
                </Tab>

                <Tab eventKey="completed" title="Completed Consultations">
                    <Table striped bordered hover responsive>
                        <thead className="table-dark">
                            <tr>
                                <th>Date</th>
                                <th>Patient Name</th>
                                <th>Reason / Symptoms</th>
                                <th>Diagnosis</th>
                                <th>Prescription</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {completedAppointments.map(app => (
                                <tr key={app.id}>
                                    <td>{app.appointmentDate || 'N/A'} {app.appointmentTime || ''}</td>
                                    <td>{app.patient?.fullName || 'Unknown'}</td>
                                    <td>{app.symptoms}</td>
                                    <td>{app.medicalRecord?.diagnosis || 'N/A'}</td>
                                    <td>
                                        {app.medicalRecord?.prescriptions?.length > 0 ? 
                                            app.medicalRecord.prescriptions.map((p, i) => (
                                                <div key={i} className="small border-bottom pb-1 mb-1 border-secondary">
                                                    {p.medicineName} ({p.morning}-{p.afternoon}-{p.evening}) for {p.durationDays} days
                                                </div>
                                            )) : 'None'
                                        }
                                    </td>
                                    <td><span className="badge bg-success">{app.status}</span></td>
                                </tr>
                            ))}
                            {completedAppointments.length === 0 && <tr><td colSpan="6" className="text-center">No completed appointments.</td></tr>}
                        </tbody>
                    </Table>
                </Tab>

                <Tab eventKey="history" title="Search Patient History">
                    <Card className="shadow-sm border-0 mb-4">
                        <Card.Body>
                            <Form onSubmit={searchPatientHistory} className="d-flex align-items-end">
                                <Form.Group className="me-3 flex-grow-1">
                                    <Form.Label>Enter Patient ID to pull Electronic Health Records</Form.Label>
                                    <Form.Control type="number" placeholder="e.g. 1" value={searchPatientId} onChange={e => setSearchPatientId(e.target.value)} required />
                                </Form.Group>
                                <Button variant="primary" type="submit">Search Records</Button>
                            </Form>
                        </Card.Body>
                    </Card>

                    {patientHistory.length > 0 && (
                        <Table striped bordered hover responsive>
                            <thead className="table-dark">
                                <tr>
                                    <th>Date</th>
                                    <th>Patient Name</th>
                                    <th>Mode of Consultation</th>
                                    <th>Diagnosis</th>
                                    <th>Prescription</th>
                                </tr>
                            </thead>
                            <tbody>
                                {patientHistory.map(rec => (
                                    <tr key={rec.id}>
                                        <td>{rec.createdAt ? new Date(rec.createdAt).toLocaleDateString() : (rec.recordDate ? new Date(rec.recordDate).toLocaleDateString() : 'N/A')}</td>
                                        <td>{rec.appointment?.patient?.fullName || 'N/A'}</td>
                                        <td>{rec.appointment?.visitType || 'N/A'}</td>
                                        <td>{rec.diagnosis}</td>
                                        <td>
                                            {rec.prescriptions?.length > 0 ? 
                                                rec.prescriptions.map((p, i) => (
                                                    <div key={i} className="small border-bottom pb-1 mb-1 border-secondary">
                                                        {p.medicineName} ({p.morning}-{p.afternoon}-{p.evening}) for {p.durationDays} days
                                                    </div>
                                                )) : 'None'
                                            }
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    )}
                </Tab>
            </Tabs>

            {/* Consultation Form Modal */}
            <Modal show={showConsultModal} onHide={handleCloseConsultModal}>
                <Form onSubmit={handleAddConsultation}>
                    <Modal.Header closeButton>
                        <Modal.Title>Patient Consultation</Modal.Title>
                    </Modal.Header>
                    <Modal.Body className="bg-light">
                        <Row>
                            <Col md={6}>
                                <Form.Group className="mb-3">
                                    <Form.Label className="fw-bold">Diagnosis</Form.Label>
                                    <Form.Control type="text" name="diagnosis" value={recordForm.diagnosis} onChange={e => setRecordForm({...recordForm, diagnosis: e.target.value})} required placeholder="e.g. Chronic Migraine" />
                                </Form.Group>
                            </Col>
                            <Col md={6}>
                                <Form.Group className="mb-3">
                                    <Form.Label className="fw-bold">Symptoms Observed</Form.Label>
                                    <Form.Control type="text" value={recordForm.currentSymptoms} onChange={e => setRecordForm({...recordForm, currentSymptoms: e.target.value})} placeholder="e.g. Headache, Nausea" />
                                </Form.Group>
                            </Col>
                        </Row>

                        <Form.Group className="mb-3">
                            <Form.Label className="fw-bold">Physical Examination Details</Form.Label>
                            <Form.Control as="textarea" rows={2} value={recordForm.physicalExamination} onChange={e => setRecordForm({...recordForm, physicalExamination: e.target.value})} placeholder="General observation, BP, Temp etc." />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label className="fw-bold">Treatment Plan</Form.Label>
                            <Form.Control as="textarea" rows={2} value={recordForm.treatmentPlan} onChange={e => setRecordForm({...recordForm, treatmentPlan: e.target.value})} placeholder="Patient advice and steps" />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label className="fw-bold">Recommended Tests</Form.Label>
                            <Form.Control type="text" value={recordForm.recommendedTests} onChange={e => setRecordForm({...recordForm, recommendedTests: e.target.value})} placeholder="e.g. Blood Test, MRI" />
                        </Form.Group>

                        <hr />
                        <h6 className="mb-3 text-primary"><i className="bi bi-capsule"></i> Add Prescriptions</h6>
                        
                        <div className="p-3 bg-white border rounded mb-3">
                            <Row className="g-2">
                                <Col md={4}>
                                    <Form.Control size="sm" placeholder="Medicine" value={newPresc.medicineName} onChange={e => setNewPresc({...newPresc, medicineName: e.target.value})} />
                                </Col>
                                <Col md={1}>
                                    <Form.Control size="sm" type="number" placeholder="M" value={newPresc.morning} onChange={e => setNewPresc({...newPresc, morning: parseInt(e.target.value)})} />
                                </Col>
                                <Col md={1}>
                                    <Form.Control size="sm" type="number" placeholder="A" value={newPresc.afternoon} onChange={e => setNewPresc({...newPresc, afternoon: parseInt(e.target.value)})} />
                                </Col>
                                <Col md={1}>
                                    <Form.Control size="sm" type="number" placeholder="E" value={newPresc.evening} onChange={e => setNewPresc({...newPresc, evening: parseInt(e.target.value)})} />
                                </Col>
                                <Col md={3}>
                                    <Form.Select size="sm" value={newPresc.foodInstruction} onChange={e => setNewPresc({...newPresc, foodInstruction: e.target.value})}>
                                        <option value="AF">After Food</option>
                                        <option value="BF">Before Food</option>
                                    </Form.Select>
                                </Col>
                                <Col md={2}>
                                    <Button size="sm" variant="dark" className="w-100" onClick={addPrescriptionToForm}>Add</Button>
                                </Col>
                            </Row>
                        </div>

                        {recordForm.prescriptions.map((p, idx) => (
                            <div key={idx} className="d-flex justify-content-between align-items-center bg-white border p-2 mb-1 rounded small">
                                <span><strong>{p.medicineName}</strong> ({p.morning}-{p.afternoon}-{p.evening}) - {p.foodInstruction} for {p.durationDays} days</span>
                                <Button variant="link" className="text-danger p-0" onClick={() => removePrescriptionFromForm(idx)}><i className="bi bi-trash"></i></Button>
                            </div>
                        ))}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={handleCloseConsultModal}>Cancel</Button>
                        <Button variant="success" type="submit">Submit Record & Complete Visit</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </Container>
    );
};

export default DoctorDashboard;
