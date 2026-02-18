import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaUser, FaEnvelope, FaSignOutAlt, FaCheckCircle } from 'react-icons/fa';
import authService from '../services/authService';
import '../styles/Dashboard.css';

const Dashboard = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);

  useEffect(() => {
    const currentUser = authService.getCurrentUser();
    if (currentUser) {
      setUser(currentUser);
    }
  }, []);

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <div className="dashboard-container">
      <nav className="dashboard-nav">
        <div className="nav-brand">
          <h2>User Dashboard</h2>
        </div>
        <button onClick={handleLogout} className="logout-button">
          <FaSignOutAlt /> Logout
        </button>
      </nav>

      <div className="dashboard-content">
        <div className="welcome-card">
          <div className="welcome-header">
            <FaCheckCircle className="success-icon" />
            <h1>Welcome Back!</h1>
            <p>You are successfully logged in</p>
          </div>
        </div>

        <div className="profile-card">
          <div className="profile-header">
            <div className="profile-avatar">
              <FaUser />
            </div>
            <h2>Profile Information</h2>
          </div>

          <div className="profile-info">
            <div className="info-item">
              <div className="info-label">
                <FaUser className="info-icon" />
                <span>Full Name</span>
              </div>
              <div className="info-value">
                {user?.firstName} {user?.lastName}
              </div>
            </div>

            <div className="info-item">
              <div className="info-label">
                <FaEnvelope className="info-icon" />
                <span>Email Address</span>
              </div>
              <div className="info-value">{user?.email}</div>
            </div>

            <div className="info-item">
              <div className="info-label">
                <FaCheckCircle className="info-icon" />
                <span>Account Status</span>
              </div>
              <div className="info-value">
                <span className="status-badge">Active</span>
              </div>
            </div>
          </div>
        </div>

        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-icon blue">
              <FaUser />
            </div>
            <div className="stat-info">
              <h3>Account Type</h3>
              <p>Standard User</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon green">
              <FaCheckCircle />
            </div>
            <div className="stat-info">
              <h3>Verification</h3>
              <p>Verified</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon purple">
              <FaEnvelope />
            </div>
            <div className="stat-info">
              <h3>Email Status</h3>
              <p>Confirmed</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;