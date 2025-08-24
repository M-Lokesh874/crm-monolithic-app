import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider, useAuth } from './context/AuthContext';
import { SettingsProvider } from './context/SettingsContext';
import Landing from './pages/Landing';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Customers from './pages/Customers';
import CustomerForm from './pages/CustomerForm';
import Leads from './pages/Leads';
import LeadForm from './pages/LeadForm';
import Users from './pages/Users';
import UserForm from './pages/UserForm';
import Analytics from './pages/Analytics';
import Settings from './pages/Settings';
import Tasks from './pages/Tasks';
import Layout from './components/Layout';
import './index.css';

// Protected Route Component
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  
  // Show loading spinner while checking authentication
  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }
  
  // Redirect to login if not authenticated
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  
  // Render protected content if authenticated
  return children;
};

function App() {
  return (
    <SettingsProvider>
      <AuthProvider>
        <Router>
        <div className="App">
          <Toaster
            position="top-right"
            toastOptions={{
              duration: 4000,
              style: {
                background: '#363636',
                color: '#fff',
              },
            }}
          />
          
          <Routes>
            {/* Public Routes */}
            <Route path="/" element={<Landing />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            
            {/* Protected Routes */}
            <Route path="/app" element={
              <ProtectedRoute>
                <Layout />
              </ProtectedRoute>
            }>
              <Route index element={<Navigate to="/app/dashboard" replace />} />
                                   <Route path="dashboard" element={<Dashboard />} />
                     <Route path="customers" element={<Customers />} />
                     <Route path="customers/new" element={<CustomerForm />} />
                     <Route path="customers/:id/edit" element={<CustomerForm />} />
                     <Route path="leads" element={<Leads />} />
                     <Route path="leads/new" element={<LeadForm />} />
                     <Route path="leads/:id/edit" element={<LeadForm />} />
                     <Route path="users" element={<Users />} />
                     <Route path="users/new" element={<UserForm />} />
                     <Route path="users/:id" element={<UserForm />} />
                     <Route path="users/:id/edit" element={<UserForm />} />
                     <Route path="analytics" element={<Analytics />} />
                     <Route path="settings" element={<Settings />} />
                     <Route path="tasks" element={<Tasks />} />
                     <Route path="customers/:id" element={<CustomerForm />} />
                     <Route path="leads/:id" element={<LeadForm />} />
            </Route>
          </Routes>
        </div>
      </Router>
      </AuthProvider>
    </SettingsProvider>
  );
}

export default App;
