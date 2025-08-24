import React, { useState, useEffect, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  Plus, 
  Edit, 
  Eye, 
  Trash2, 
  Building2,
  Mail,
  Phone,
  MapPin,
  User
} from 'lucide-react';
import { customerService } from '../services/customerService';
import PageHeader from '../components/PageHeader';
import toast from 'react-hot-toast';

const Customers = () => {
  const navigate = useNavigate();
  const [customers, setCustomers] = useState([]);
  const [filteredCustomers, setFilteredCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('ALL');
  const [selectedType, setSelectedType] = useState('ALL');

  useEffect(() => {
    loadCustomers();
  }, []);

  const filterCustomers = useCallback(() => {
    let filtered = customers;

    // Search filter
    if (searchTerm) {
      filtered = filtered.filter(customer =>
        customer.companyName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        customer.contactPerson?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        customer.email?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Status filter
    if (selectedStatus !== 'ALL') {
      filtered = filtered.filter(customer => customer.status === selectedStatus);
    }

    // Type filter
    if (selectedType !== 'ALL') {
      filtered = filtered.filter(customer => customer.customerType === selectedType);
    }

    setFilteredCustomers(filtered);
  }, [customers, searchTerm, selectedStatus, selectedType]);

  useEffect(() => {
    filterCustomers();
  }, [filterCustomers]);

  const loadCustomers = async () => {
    try {
      setLoading(true);
      const data = await customerService.getAllCustomers();
      setCustomers(data);
    } catch (error) {
      toast.error('Failed to load customers');
      console.error('Error loading customers:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteCustomer = async (customerId) => {
    if (window.confirm('Are you sure you want to delete this customer?')) {
      try {
        await customerService.deleteCustomer(customerId);
        toast.success('Customer deleted successfully');
        loadCustomers(); // Reload the list
      } catch (error) {
        toast.error('Failed to delete customer');
        console.error('Error deleting customer:', error);
      }
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'ACTIVE':
        return 'bg-green-100 text-green-800';
      case 'INACTIVE':
        return 'bg-red-100 text-red-800';
      case 'PROSPECT':
        return 'bg-blue-100 text-blue-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getTypeColor = (type) => {
    switch (type) {
      case 'CUSTOMER':
        return 'bg-purple-100 text-purple-800';
      case 'PROSPECT':
        return 'bg-blue-100 text-blue-800';
      case 'PARTNER':
        return 'bg-orange-100 text-orange-800';
      case 'VENDOR':
        return 'bg-gray-100 text-gray-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const formatCurrency = (amount) => {
    if (!amount) return '$0';
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(amount);
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString();
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
      {/* Enhanced Header with Search and Actions */}
      <PageHeader
        title="Customers"
        subtitle="Manage your customer relationships and business contacts"
        actions={[
          {
            label: 'Add Customer',
            icon: <Plus className="h-4 w-4" />,
            onClick: () => navigate('/app/customers/new'),
            className: 'bg-green-600 hover:bg-green-700 focus:ring-green-500'
          }
        ]}
        showSearch={true}
        searchPlaceholder="Search customers..."
        onSearch={setSearchTerm}
        showFilters={false}
      />

      {/* Filters */}
      <div className="bg-white p-4 rounded-lg shadow-sm border">
        <div className="flex flex-wrap gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Status</label>
            <select
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
              className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
            >
              <option value="ALL">All Statuses</option>
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
              <option value="PROSPECT">Prospect</option>
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Type</label>
            <select
              value={selectedType}
              onChange={(e) => setSelectedType(e.target.value)}
              className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
            >
              <option value="ALL">All Types</option>
              <option value="CUSTOMER">Customer</option>
              <option value="PROSPECT">Prospect</option>
              <option value="PARTNER">Partner</option>
              <option value="VENDOR">Vendor</option>
            </select>
          </div>
        </div>
      </div>

      {/* Results Summary */}
      <div className="flex items-center justify-between">
        <p className="text-sm text-gray-700">
          Showing {filteredCustomers.length} of {customers.length} customers
        </p>
        <div className="text-sm text-gray-500">
          {searchTerm && `Search results for "${searchTerm}"`}
        </div>
      </div>

      {/* Customers Grid */}
      {filteredCustomers.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredCustomers.map((customer) => (
            <div key={customer.id} className="bg-white rounded-lg shadow-sm border hover:shadow-md transition-shadow">
              <div className="p-6">
                {/* Header */}
                <div className="flex items-start justify-between mb-4">
                  <div className="flex items-center">
                    <div className="h-10 w-10 rounded-full bg-primary-100 flex items-center justify-center">
                      <Building2 className="h-5 w-5 text-primary-600" />
                    </div>
                    <div className="ml-3">
                      <h3 className="text-lg font-semibold text-gray-900">
                        {customer.companyName || 'Unnamed Company'}
                      </h3>
                      <div className="flex items-center space-x-2 mt-1">
                        <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getStatusColor(customer.status)}`}>
                          {customer.status}
                        </span>
                        <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getTypeColor(customer.customerType)}`}>
                          {customer.customerType}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Contact Info */}
                <div className="space-y-3 mb-4">
                  {customer.contactPerson && (
                    <div className="flex items-center text-sm text-gray-600">
                      <User className="h-4 w-4 mr-2 text-gray-400" />
                      {customer.contactPerson}
                    </div>
                  )}
                  
                  {customer.email && (
                    <div className="flex items-center text-sm text-gray-600">
                      <Mail className="h-4 w-4 mr-2 text-gray-400" />
                      <a href={`mailto:${customer.email}`} className="hover:text-primary-600">
                        {customer.email}
                      </a>
                    </div>
                  )}
                  
                  {customer.phoneNumber && (
                    <div className="flex items-center text-sm text-gray-600">
                      <Phone className="h-4 w-4 mr-2 text-gray-400" />
                      <a href={`tel:${customer.phoneNumber}`} className="hover:text-primary-600">
                        {customer.phoneNumber}
                      </a>
                    </div>
                  )}
                  
                  {customer.city && customer.state && (
                    <div className="flex items-center text-sm text-gray-600">
                      <MapPin className="h-4 w-4 mr-2 text-gray-400" />
                      {customer.city}, {customer.state}
                    </div>
                  )}
                </div>

                {/* Business Info */}
                <div className="border-t pt-4 space-y-2">
                  {customer.annualRevenue && (
                    <div className="flex items-center justify-between text-sm">
                      <span className="text-gray-600">Annual Revenue:</span>
                      <span className="font-medium text-gray-900">{formatCurrency(customer.annualRevenue)}</span>
                    </div>
                  )}
                  
                  {customer.employeeCount && (
                    <div className="flex items-center justify-between text-sm">
                      <span className="text-gray-600">Employees:</span>
                      <span className="font-medium text-gray-900">{customer.employeeCount}</span>
                    </div>
                  )}
                  
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-gray-600">Created:</span>
                    <span className="font-medium text-gray-900">{formatDate(customer.createdAt)}</span>
                  </div>
                </div>

                {/* Actions */}
                <div className="border-t pt-4 mt-4 flex items-center justify-between">
                  <div className="flex space-x-2">
                    <Link
                      to={`/app/customers/${customer.id}/edit`}
                      className="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors"
                    >
                      <Edit className="h-4 w-4 mr-1" />
                      Edit
                    </Link>
                    
                    <button
                      onClick={() => handleDeleteCustomer(customer.id)}
                      className="inline-flex items-center px-3 py-2 border border-red-300 shadow-sm text-sm leading-4 font-medium rounded-md text-red-700 bg-white hover:bg-red-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 transition-colors"
                    >
                      <Trash2 className="h-4 w-4 mr-1" />
                      Delete
                    </button>
                  </div>
                  
                  <Link
                    to={`/app/customers/${customer.id}`}
                    className="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-primary-700 bg-primary-100 hover:bg-primary-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors"
                  >
                    <Eye className="h-4 w-4 mr-1" />
                    View
                  </Link>
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="text-center py-12">
          <Building2 className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">
            {searchTerm ? 'No customers found' : 'No customers yet'}
          </h3>
          <p className="mt-1 text-sm text-gray-500">
            {searchTerm 
              ? `No customers match "${searchTerm}". Try adjusting your search.`
              : 'Get started by creating your first customer.'
            }
          </p>
          {!searchTerm && (
            <div className="mt-6">
              <Link
                to="/app/customers/new"
                className="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors"
              >
                <Plus className="h-4 w-4 mr-2" />
                Add Customer
              </Link>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default Customers;
