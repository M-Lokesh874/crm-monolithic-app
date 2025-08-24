import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { 
  Target, 
  Save,
  X,
  Building2,
  User,
  DollarSign,
  Calendar,
  Phone,
  Mail
} from 'lucide-react';
import { leadService } from '../services/leadService';
import { customerService } from '../services/customerService';
import PageHeader from '../components/PageHeader';
import toast from 'react-hot-toast';

const LeadForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditing = Boolean(id);
  
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [customers, setCustomers] = useState([]);
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    leadSource: 'WEBSITE',
    leadStatus: 'NEW',
    estimatedValue: '',
    probability: 25,
    expectedCloseDate: '',
    notes: '',
    customer: null
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    loadCustomers();
    if (isEditing) {
      loadLead();
    }
  }, [id]);

  const loadCustomers = async () => {
    try {
      const data = await customerService.getAllCustomers();
      setCustomers(data);
    } catch (error) {
      console.error('Error loading customers:', error);
    }
  };

  const loadLead = async () => {
    try {
      setLoading(true);
      const lead = await leadService.getLeadById(id);
      setFormData({
        ...lead,
        customer: lead.customer ? lead.customer.id : null
      });
    } catch (error) {
      toast.error('Failed to load lead');
      console.error('Error loading lead:', error);
      navigate('/app/leads');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // Clear error when user starts typing
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const handleCustomerChange = (customerId) => {
    const customer = customers.find(c => c.id === parseInt(customerId));
    setFormData(prev => ({
      ...prev,
      customer: customer
    }));
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.title?.trim()) {
      newErrors.title = 'Lead title is required';
    }

    if (!formData.customer) {
      newErrors.customer = 'Customer is required';
    }

    if (formData.estimatedValue && isNaN(formData.estimatedValue)) {
      newErrors.estimatedValue = 'Estimated value must be a number';
    }

    if (formData.probability && (isNaN(formData.probability) || formData.probability < 0 || formData.probability > 100)) {
      newErrors.probability = 'Probability must be between 0 and 100';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      toast.error('Please fix the errors in the form');
      return;
    }

    setSaving(true);
    
    try {
      const submitData = {
        ...formData,
        customerId: formData.customer?.id
      };

      if (isEditing) {
        await leadService.updateLead(id, submitData);
        toast.success('Lead updated successfully');
      } else {
        await leadService.createLead(submitData);
        toast.success('Lead created successfully');
      }
      navigate('/app/leads');
    } catch (error) {
      toast.error(isEditing ? 'Failed to update lead' : 'Failed to create lead');
      console.error('Error saving lead:', error);
    } finally {
      setSaving(false);
    }
  };

  const handleCancel = () => {
    navigate('/app/leads');
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
        title={isEditing ? 'Edit Lead' : 'Add New Lead'}
        subtitle={isEditing ? 'Update lead information and pipeline status' : 'Create a new lead and add it to your sales pipeline'}
        showBack={true}
        backPath="/app/leads"
        actions={[
          {
            label: 'Cancel',
            icon: <X className="h-4 w-4" />,
            onClick: handleCancel,
            className: 'bg-gray-600 hover:bg-gray-700 focus:ring-gray-500'
          }
        ]}
      />

      {/* Lead Form */}
      <form onSubmit={handleSubmit} className="bg-white shadow-sm rounded-lg border">
        <div className="p-6 space-y-6">
          {/* Lead Information */}
          <div>
            <h3 className="text-lg font-medium text-gray-900 mb-4 flex items-center">
              <Target className="h-5 w-5 mr-2 text-primary-600" />
              Lead Information
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-1">
                  Lead Title *
                </label>
                <input
                  type="text"
                  id="title"
                  name="title"
                  value={formData.title}
                  onChange={handleInputChange}
                  className={`block w-full px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm ${
                    errors.title ? 'border-red-300' : 'border-gray-300'
                  }`}
                  placeholder="e.g., New Software Implementation Project"
                />
                {errors.title && (
                  <p className="mt-1 text-sm text-red-600">{errors.title}</p>
                )}
              </div>

              <div>
                <label htmlFor="leadSource" className="block text-sm font-medium text-gray-700 mb-1">
                  Lead Source
                </label>
                <select
                  id="leadSource"
                  name="leadSource"
                  value={formData.leadSource}
                  onChange={handleInputChange}
                  className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                >
                  <option value="WEBSITE">Website</option>
                  <option value="REFERRAL">Referral</option>
                  <option value="SOCIAL_MEDIA">Social Media</option>
                  <option value="COLD_CALL">Cold Call</option>
                  <option value="EVENT">Event</option>
                </select>
              </div>

              <div>
                <label htmlFor="leadStatus" className="block text-sm font-medium text-gray-700 mb-1">
                  Lead Status
                </label>
                <select
                  id="leadStatus"
                  name="leadStatus"
                  value={formData.leadStatus}
                  onChange={handleInputChange}
                  className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                >
                  <option value="NEW">New Lead</option>
                  <option value="CONTACTED">Contacted</option>
                  <option value="QUALIFIED">Qualified</option>
                  <option value="PROPOSAL">Proposal</option>
                  <option value="NEGOTIATION">Negotiation</option>
                  <option value="CLOSED_WON">Closed Won</option>
                  <option value="CLOSED_LOST">Closed Lost</option>
                </select>
              </div>

              <div>
                <label htmlFor="expectedCloseDate" className="block text-sm font-medium text-gray-700 mb-1">
                  Expected Close Date
                </label>
                <input
                  type="date"
                  id="expectedCloseDate"
                  name="expectedCloseDate"
                  value={formData.expectedCloseDate}
                  onChange={handleInputChange}
                  className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                />
              </div>
            </div>
          </div>

          {/* Customer Selection */}
          <div>
            <h3 className="text-lg font-medium text-gray-900 mb-4 flex items-center">
              <Building2 className="h-5 w-5 mr-2 text-primary-600" />
              Customer Information
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label htmlFor="customer" className="block text-sm font-medium text-gray-700 mb-1">
                  Customer *
                </label>
                <select
                  id="customer"
                  name="customer"
                  value={formData.customer?.id || ''}
                  onChange={(e) => handleCustomerChange(e.target.value)}
                  className={`block w-full px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm ${
                    errors.customer ? 'border-red-300' : 'border-gray-300'
                  }`}
                >
                  <option value="">Select a customer</option>
                  {customers.map(customer => (
                    <option key={customer.id} value={customer.id}>
                      {customer.companyName} - {customer.contactPerson}
                    </option>
                  ))}
                </select>
                {errors.customer && (
                  <p className="mt-1 text-sm text-red-600">{errors.customer}</p>
                )}
              </div>

              {formData.customer && (
                <div className="bg-gray-50 p-4 rounded-lg">
                  <h4 className="text-sm font-medium text-gray-900 mb-2">Selected Customer</h4>
                  <div className="space-y-1 text-sm text-gray-600">
                    <div className="flex items-center">
                      <Building2 className="h-4 w-4 mr-2" />
                      {formData.customer.companyName}
                    </div>
                    {formData.customer.contactPerson && (
                      <div className="flex items-center">
                        <User className="h-4 w-4 mr-2" />
                        {formData.customer.contactPerson}
                      </div>
                    )}
                    {formData.customer.email && (
                      <div className="flex items-center">
                        <Mail className="h-4 w-4 mr-2" />
                        {formData.customer.email}
                      </div>
                    )}
                    {formData.customer.phoneNumber && (
                      <div className="flex items-center">
                        <Phone className="h-4 w-4 mr-2" />
                        {formData.customer.phoneNumber}
                      </div>
                    )}
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* Financial Information */}
          <div>
            <h3 className="text-lg font-medium text-gray-900 mb-4 flex items-center">
              <DollarSign className="h-5 w-5 mr-2 text-primary-600" />
              Financial Information
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label htmlFor="estimatedValue" className="block text-sm font-medium text-gray-700 mb-1">
                  Estimated Value
                </label>
                <input
                  type="number"
                  id="estimatedValue"
                  name="estimatedValue"
                  value={formData.estimatedValue}
                  onChange={handleInputChange}
                  className={`block w-full px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm ${
                    errors.estimatedValue ? 'border-red-300' : 'border-gray-300'
                  }`}
                  placeholder="50000"
                  min="0"
                  step="1000"
                />
                {errors.estimatedValue && (
                  <p className="mt-1 text-sm text-red-600">{errors.estimatedValue}</p>
                )}
              </div>

              <div>
                <label htmlFor="probability" className="block text-sm font-medium text-gray-700 mb-1">
                  Probability (%)
                </label>
                <input
                  type="number"
                  id="probability"
                  name="probability"
                  value={formData.probability}
                  onChange={handleInputChange}
                  className={`block w-full px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm ${
                    errors.probability ? 'border-red-300' : 'border-gray-300'
                  }`}
                  placeholder="25"
                  min="0"
                  max="100"
                  step="5"
                />
                {errors.probability && (
                  <p className="mt-1 text-sm text-red-600">{errors.probability}</p>
                )}
              </div>
            </div>
          </div>

          {/* Description and Notes */}
          <div>
            <h3 className="text-lg font-medium text-gray-900 mb-4 flex items-center">
              <Target className="h-5 w-5 mr-2 text-primary-600" />
              Additional Information
            </h3>
            <div className="space-y-4">
              <div>
                <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
                  Description
                </label>
                <textarea
                  id="description"
                  name="description"
                  rows={3}
                  value={formData.description}
                  onChange={handleInputChange}
                  className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                  placeholder="Describe the lead opportunity, requirements, and any specific details..."
                />
              </div>

              <div>
                <label htmlFor="notes" className="block text-sm font-medium text-gray-700 mb-1">
                  Notes
                </label>
                <textarea
                  id="notes"
                  name="notes"
                  rows={3}
                  value={formData.notes}
                  onChange={handleInputChange}
                  className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
                  placeholder="Internal notes, follow-up actions, or additional context..."
                />
              </div>
            </div>
          </div>
        </div>

        {/* Form Actions */}
        <div className="px-6 py-4 bg-gray-50 border-t border-gray-200 flex items-center justify-end space-x-3">
          <button
            type="button"
            onClick={handleCancel}
            className="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors"
          >
            <X className="h-4 w-4 mr-2" />
            Cancel
          </button>
          
          <button
            type="submit"
            disabled={saving}
            className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            {saving ? (
              <>
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                {isEditing ? 'Updating...' : 'Creating...'}
              </>
            ) : (
              <>
                <Save className="h-4 w-4 mr-2" />
                {isEditing ? 'Update Lead' : 'Create Lead'}
              </>
            )}
          </button>
        </div>
      </form>
    </div>
  );
};

export default LeadForm;
