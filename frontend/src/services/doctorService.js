import api from './api';

const doctorService = {
    getDoctorProfile: (username) => {
        return api.get(`/doctor/profile/${username}`).then(response => {
            return response.data;
        }).catch(error => {
             console.error("Failed to load secure doctor profile via backend API:", error);
             throw error;
        });
    },
    getAppointments: (doctorId) => {
        return api.get(`/doctor/${doctorId}/appointments`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    viewUpcomingAppointments: (doctorId) => {
        return api.get(`/doctor/${doctorId}/upcoming`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    viewCompletedAppointments: (doctorId) => {
        return api.get(`/doctor/${doctorId}/completed`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    confirmAppointment: (appointmentId) => {
        return api.put(`/doctor/appointment/${appointmentId}/confirm`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    rejectAppointment: (appointmentId) => {
        return api.put(`/doctor/appointment/${appointmentId}/reject`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    completeAppointment: (appointmentId) => {
        return api.put(`/doctor/appointment/${appointmentId}/complete`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    updateAppointmentStatus: (appointmentId, status) => {
        return api.put(`/appointments/${appointmentId}/status?status=${status}`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    addConsultationDetails: (appointmentId, recordDTO) => {
        return api.post(`/doctor/appointment/${appointmentId}/consultation`, recordDTO)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    addPrescription: (recordId, prescriptionDTO) => {
        return api.post(`/doctor/medicalrecord/${recordId}/prescription`, prescriptionDTO)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    viewPatientHistory: (patientId) => {
        return api.get(`/doctor/patient/${patientId}/history`)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    addMedicalRecord: (recordData) => {
        return api.post(`/medical-records`, recordData)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    updateMedicalRecord: (id, recordDTO) => {
        return api.put(`/medical-records/${id}`, recordDTO)
            .then(response => response.data)
            .catch(error => { throw error; });
    },
    getMedicalRecordByAppointment: (appointmentId) => {
        return api.get(`/medical-records/appointment/${appointmentId}`)
            .then(response => response.data)
            .catch(error => { throw error; });
    }
};

export default doctorService;
