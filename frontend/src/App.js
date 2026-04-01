import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Layout from './components/layout/Layout';
import PrivateRoute from './components/routing/PrivateRoute';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import AdminDashboard from './pages/admin/AdminDashboard';
import PatientDashboard from './pages/patient/PatientDashboard';
import DoctorDashboard from './pages/doctor/DoctorDashboard';
import Home from './pages/Home';

// The App component is the main container for our website
function App() {
  return (
    // AuthProvider gives login information to all components inside it
    <AuthProvider>
      {/* Router enables navigation between different page URLs */}
      <Router>
        {/* Layout wraps all pages with a common Navbar and Footer */}
        <Layout>
          {/* Routes defines which component to show for which URL path */}
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            
            {/* PrivateRoute makes sure only users with specific roles can access these pages */}
            <Route path="/admin/*" element={<PrivateRoute allowedRoles={['ADMIN']}><AdminDashboard /></PrivateRoute>} />
            <Route path="/patient/*" element={<PrivateRoute allowedRoles={['PATIENT']}><PatientDashboard /></PrivateRoute>} />
            <Route path="/doctor/*" element={<PrivateRoute allowedRoles={['DOCTOR']}><DoctorDashboard /></PrivateRoute>} />
          </Routes>
        </Layout>
      </Router>
    </AuthProvider>
  );
}

export default App;
