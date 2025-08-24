import React, { useState, useEffect, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  Plus, 
  Edit, 
  Eye, 
  Trash2, 
  User,
  Shield,
  Users as UsersIcon,
  Mail,
  Calendar,
  CheckCircle,
  XCircle
} from 'lucide-react';
import { userService } from '../services/userService';
import { useAuth } from '../context/AuthContext';
import PageHeader from '../components/PageHeader';
import toast from 'react-hot-toast';

// User Card Component
const UserCard = ({ user, onDelete, getRoleColor, getRoleIcon, formatDate, getUserActiveStatus }) => {
  const [showActions, setShowActions] = useState(false);

  return (
    <div className="bg-white rounded-lg shadow-sm border hover:shadow-md transition-shadow p-4">
      {/* User Header */}
      <div className="flex items-start justify-between mb-3">
        <div className="flex-1 min-w-0">
          <div className="flex items-center space-x-2 mb-2">
            <div className="w-10 h-10 bg-primary-100 rounded-full flex items-center justify-center">
              <span className="text-primary-600 font-semibold text-lg">
                {user.firstName?.charAt(0)}{user.lastName?.charAt(0)}
              </span>
            </div>
            <div className="min-w-0 flex-1">
              <h4 className="text-sm font-semibold text-gray-900 truncate">
                {user.firstName} {user.lastName}
              </h4>
              <p className="text-xs text-gray-500 truncate">@{user.username}</p>
            </div>
          </div>
          
          {/* Role Badge */}
          <div className="flex items-center space-x-2 mb-2">
            {getRoleIcon(user.role)}
            <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getRoleColor(user.role)}`}>
              {user.role}
            </span>
          </div>
        </div>
        
        {/* Actions Menu */}
        <div className="relative">
          <button
            onClick={() => setShowActions(!showActions)}
            className="text-gray-400 hover:text-gray-600 p-1"
          >
            <svg className="h-4 w-4" fill="currentColor" viewBox="0 0 20 20">
              <path d="M10 6a2 2 0 110-4 2 2 0 010 4zM10 12a2 2 0 110-4 2 2 0 010 4zM10 18a2 2 0 110-4 2 2 0 010 4z" />
            </svg>
          </button>
          
          {showActions && (
            <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg border z-10">
              <div className="py-1">
                <Link
                  to={`/app/users/${user.id}/edit`}
                  className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  <Edit className="h-4 w-4 mr-2" />
                  Edit
                </Link>
                <Link
                  to={`/app/users/${user.id}`}
                  className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  <Eye className="h-4 w-4 mr-2" />
                  View
                </Link>
                <button
                  onClick={() => onDelete(user.id)}
                  className="flex items-center w-full px-4 py-2 text-sm text-red-700 hover:bg-red-50"
                >
                  <Trash2 className="h-4 w-4 mr-2" />
                  Deactivate
                </button>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* User Details */}
      <div className="space-y-2 mb-3">
        <div className="flex items-center text-xs text-gray-600">
          <Mail className="h-3 w-3 mr-2" />
          <span className="truncate">{user.email}</span>
        </div>
        
        <div className="flex items-center text-xs text-gray-600">
          <Calendar className="h-3 w-3 mr-2" />
          <span>Joined {formatDate(user.createdAt)}</span>
        </div>
      </div>

      {/* Status */}
      <div className="flex items-center justify-between">
        <div className="flex items-center space-x-2">
          {getUserActiveStatus(user) ? (
            <CheckCircle className="h-4 w-4 text-green-500" />
          ) : (
            <XCircle className="h-4 w-4 text-red-500" />
          )}
          <span className={`text-xs font-medium ${getUserActiveStatus(user) ? 'text-green-600' : 'text-red-600'}`}>
            {getUserActiveStatus(user) ? 'Active' : 'Inactive'}
          </span>
        </div>
      </div>
    </div>
  );
};

// Main Users Component
const Users = () => {
  const navigate = useNavigate();
  const { user: currentUser } = useAuth();
  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedRole, setSelectedRole] = useState('ALL');
  const [selectedStatus, setSelectedStatus] = useState('ALL');

  // User roles for filtering
  const userRoles = [
    { id: 'ALL', name: 'All Roles', color: 'bg-gray-100 text-gray-800' },
    { id: 'ADMIN', name: 'Administrators', color: 'bg-red-100 text-red-800' },
    { id: 'MANAGER', name: 'Managers', color: 'bg-blue-100 text-blue-800' },
    { id: 'SALES_REP', name: 'Sales Representatives', color: 'bg-green-100 text-green-800' }
  ];

  // User statuses for filtering
  const userStatuses = [
    { id: 'ALL', name: 'All Statuses', color: 'bg-gray-100 text-gray-800' },
    { id: 'ACTIVE', name: 'Active Users', color: 'bg-green-100 text-green-800' },
    { id: 'INACTIVE', name: 'Inactive Users', color: 'bg-red-100 text-red-800' }
  ];

  useEffect(() => {
    // Check if user has permission to access this page
    if (currentUser?.role !== 'ADMIN' && currentUser?.role !== 'MANAGER') {
      toast.error('Access denied. You do not have permission to view user management.');
      navigate('/app/dashboard');
      return;
    }
    loadUsers();
  }, [currentUser, navigate]);

  const filterUsers = useCallback(() => {
    let filtered = users;

    // Search filter
    if (searchTerm) {
      filtered = filtered.filter(user =>
        user.username?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.lastName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.email?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Role filter
    if (selectedRole !== 'ALL') {
      filtered = filtered.filter(user => user.role === selectedRole);
    }

    // Status filter
    if (selectedStatus !== 'ALL') {
      filtered = filtered.filter(user => getUserActiveStatus(user) === (selectedStatus === 'ACTIVE'));
    }

    setFilteredUsers(filtered);
  }, [users, searchTerm, selectedRole, selectedStatus]);

  useEffect(() => {
    filterUsers();
  }, [filterUsers]);

  const loadUsers = useCallback(async () => {
    try {
      setLoading(true);
      
      // Use role-based filtering from backend
      if (currentUser?.role === 'MANAGER') {
        // Managers can only see SALES_REP users (their team)
        const data = await userService.getUsersByRole('SALES_REP');
        setUsers(data);
      } else {
        // Admins can see all users
        const data = await userService.getAllUsers();
        setUsers(data);
      }
    } catch (error) {
      if (error.response?.status === 403) {
        toast.error('Access denied. You do not have permission to view users.');
      } else {
        toast.error('Failed to load users');
      }
      console.error('Error loading users:', error);
    } finally {
      setLoading(false);
    }
  }, [currentUser?.role]);

  const handleDeleteUser = async (userId) => {
    if (window.confirm('Are you sure you want to deactivate this user? This action can be reversed later.')) {
      try {
        await userService.deleteUser(userId);
        toast.success('User deactivated successfully');
        loadUsers();
      } catch (error) {
        toast.error('Failed to deactivate user');
        console.error('Error deactivating user:', error);
      }
    }
  };

  const getRoleColor = (role) => {
    switch (role) {
      case 'ADMIN':
        return 'bg-red-100 text-red-800';
      case 'MANAGER':
        return 'bg-blue-100 text-blue-800';
      case 'SALES_REP':
        return 'bg-green-100 text-green-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getRoleIcon = (role) => {
    switch (role) {
      case 'ADMIN':
        return <Shield className="h-4 w-4" />;
      case 'MANAGER':
        return <UsersIcon className="h-4 w-4" />;
      case 'SALES_REP':
        return <User className="h-4 w-4" />;
      default:
        return <User className="h-4 w-4" />;
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString();
  };

  // Helper function to get user active status (handles both field names)
  const getUserActiveStatus = (user) => {
    const status = user.isActive !== undefined ? user.isActive : user.active;
    return status;
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Enhanced Header */}
      <PageHeader
        title="User Management"
        subtitle="Manage system users, roles, and permissions"
        actions={[
          {
            label: 'Add User',
            icon: <Plus className="h-4 w-4" />,
            onClick: () => navigate('/app/users/new'),
            className: 'bg-green-600 hover:bg-green-700 focus:ring-green-500'
          }
        ]}
        showSearch={true}
        searchPlaceholder="Search users..."
        onSearch={setSearchTerm}
        showFilters={false}
      />

      {/* Filters */}
      <div className="bg-white p-4 rounded-lg shadow-sm border">
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-1">User Role</label>
            <select
              value={selectedRole}
              onChange={(e) => setSelectedRole(e.target.value)}
              className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 text-sm"
            >
              {userRoles.map(role => (
                <option key={role.id} value={role.id}>{role.name}</option>
              ))}
            </select>
          </div>
          
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-1">Status</label>
            <select
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
              className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 text-sm"
            >
              {userStatuses.map(status => (
                <option key={status.id} value={status.id}>{status.name}</option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Results Summary */}
      <div className="flex items-center justify-between">
        <p className="text-sm text-gray-700">
          Showing {filteredUsers.length} of {users.length} users
        </p>
        <div className="text-sm text-gray-500">
          {searchTerm && `Search results for "${searchTerm}"`}
        </div>
      </div>

      {/* Users Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
                 {filteredUsers.map((user) => (
           <UserCard
             key={user.id}
             user={user}
             onDelete={handleDeleteUser}
             getRoleColor={getRoleColor}
             getRoleIcon={getRoleIcon}
             formatDate={formatDate}
             getUserActiveStatus={getUserActiveStatus}
           />
         ))}
        
        {filteredUsers.length === 0 && (
          <div className="col-span-full text-center py-12 text-gray-400">
            <User className="h-12 w-12 mx-auto mb-4" />
            <p className="text-lg font-medium">No users found</p>
            <p className="text-sm">Try adjusting your search or filters</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Users;
