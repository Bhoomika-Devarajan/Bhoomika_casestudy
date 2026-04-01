import api from './api';

const adminService = {
    getAllDoctors: () => {
        return api.get('/admin/doctors')
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    getAllPatients: () => {
        return api.get('/admin/patients')
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    addDoctor: (doctorDTO) => {
        return api.post('/admin/doctor', doctorDTO)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    addPatient: (patientDTO) => {
        return api.post('/admin/patient', patientDTO)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    updateDoctor: (id, doctorDTO) => {
        return api.put(`/admin/doctor/${id}`, doctorDTO)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    updatePatient: (id, patientDTO) => {
        return api.put(`/admin/patient/${id}`, patientDTO)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    getAllAppointments: () => {
        return api.get('/admin/appointments')
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    rescheduleAppointment: (id, appointmentDTO) => {
        return api.put(`/admin/appointment/${id}`, appointmentDTO)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    deleteDoctor: (doctorId) => {
        return api.delete(`/admin/doctor/${doctorId}`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    deletePatient: (patientId) => {
        return api.delete(`/admin/patient/${patientId}`)
            .then(response => response.data)
            .catch(error => { throw error; });
    }
};

export default adminService;
