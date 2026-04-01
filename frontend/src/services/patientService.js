import api from './api';

const patientService = {
    getPatientProfile: (username) => {
        return api.get(`/patients/profile/${username}`)
            .then(response => response.data)
            .catch(error => {
                console.error("Failed to fetch patient profile:", error);
                throw error;
            });
    },
    updatePatientProfile: (id, dto) => {
        return api.put(`/patients/${id}`, dto)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    getDoctors: () => {
        return api.get('/admin/doctors')
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    bookAppointment: (appointmentData) => {
        const { patientId, doctorId, ...dto } = appointmentData;
        return api.post(`/patients/${patientId}/appointments/${doctorId}`, dto)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    getAppointments: (patientId) => {
        return api.get(`/appointments/patient/${patientId}`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    viewUpcomingAppointments: (patientId) => {
        return api.get(`/patients/${patientId}/appointments/upcoming`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    viewCompletedAppointments: (patientId) => {
        return api.get(`/patients/${patientId}/appointments/completed`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    rescheduleAppointment: (appointmentId, dto) => {
        return api.put(`/patients/appointments/${appointmentId}/reschedule`, dto)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    cancelAppointment: (appointmentId) => {
        return api.delete(`/patients/appointments/${appointmentId}`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    getMedicalRecords: (patientId) => {
        return api.get(`/medical-records/patient/${patientId}`)
            .then(response => response.data)
            .catch(error => { throw error; });
    }
};

export default patientService;
