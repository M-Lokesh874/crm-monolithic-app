import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { ArrowLeft, Home } from 'lucide-react';

const BackButton = ({ 
  fallbackPath = '/app/dashboard', 
  showHome = false,
  className = '',
  children 
}) => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleBack = () => {
    // If we can go back in history, do that
    if (window.history.length > 1) {
      navigate(-1);
    } else {
      // Otherwise, go to fallback path
      navigate(fallbackPath);
    }
  };

  const goHome = () => {
    navigate('/app/dashboard');
  };

  return (
    <div className={`flex items-center space-x-3 ${className}`}>
      <button
        onClick={handleBack}
        className="inline-flex items-center px-3 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50 hover:text-gray-900 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors"
      >
        <ArrowLeft className="h-4 w-4 mr-2" />
        {children || 'Back'}
      </button>
      
      {showHome && (
        <button
          onClick={goHome}
          className="inline-flex items-center px-3 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50 hover:text-gray-900 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors"
        >
          <Home className="h-4 w-4 mr-2" />
          Home
        </button>
      )}
    </div>
  );
};

export default BackButton;
