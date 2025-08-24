import React, { useState, useEffect, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  Plus, 
  Search, 
  Filter, 
  Edit, 
  Eye, 
  Trash2, 
  Target,
  DollarSign,
  Calendar,
  User,
  Building2,
  Phone,
  Mail,
  MapPin,
  TrendingUp,
  BarChart3
} from 'lucide-react';
import { leadService } from '../services/leadService';
import PageHeader from '../components/PageHeader';
import toast from 'react-hot-toast';

const Leads = () => {
  const navigate = useNavigate();
  const [leads, setLeads] = useState([]);
  const [filteredLeads, setFilteredLeads] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedSource, setSelectedSource] = useState('ALL');
  const [selectedStatus, setSelectedStatus] = useState('ALL');

  // Pipeline stages
  const [pipelineStages, setPipelineStages] = useState([
    { id: 'NEW', name: 'New Leads', color: 'bg-blue-100 text-blue-800', count: 0 },
    { id: 'CONTACTED', name: 'Contacted', color: 'bg-yellow-100 text-yellow-800', count: 0 },
    { id: 'QUALIFIED', name: 'Qualified', color: 'bg-purple-100 text-purple-800', count: 0 },
    { id: 'PROPOSAL', name: 'Proposal', color: 'bg-orange-100 text-orange-800', count: 0 },
    { id: 'NEGOTIATION', name: 'Negotiation', color: 'bg-indigo-100 text-indigo-800', count: 0 },
    { id: 'CLOSED_WON', name: 'Closed Won', color: 'bg-green-100 text-green-800', count: 0 },
    { id: 'CLOSED_LOST', name: 'Closed Lost', color: 'bg-red-100 text-red-800', count: 0 }
  ]);

  useEffect(() => {
    loadLeads();
  }, []);

  const filterLeads = useCallback(() => {
    let filtered = leads;

    // Search filter
    if (searchTerm) {
      filtered = filtered.filter(lead =>
        lead.title?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        lead.description?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        lead.customer?.companyName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        lead.customer?.contactPerson?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Source filter
    if (selectedSource !== 'ALL') {
      filtered = filtered.filter(lead => lead.leadSource === selectedSource);
    }

    // Status filter
    if (selectedStatus !== 'ALL') {
      filtered = filtered.filter(lead => lead.leadStatus === selectedStatus);
    }

    setFilteredLeads(filtered);
    updatePipelineCounts(filtered);
  }, [leads, searchTerm, selectedSource, selectedStatus]);

  useEffect(() => {
    filterLeads();
  }, [filterLeads]);

  const updatePipelineCounts = (leads) => {
    const counts = { ...pipelineStages.reduce((acc, stage) => ({ ...acc, [stage.id]: 0 }), {}) };
    leads.forEach(lead => {
      if (counts[lead.leadStatus] !== undefined) {
        counts[lead.leadStatus]++;
      }
    });
    
    setPipelineStages(prev => prev.map(stage => ({
      ...stage,
      count: counts[stage.id] || 0
    })));
  };

  const loadLeads = async () => {
    try {
      setLoading(true);
      const data = await leadService.getAllLeads();
      setLeads(data);
    } catch (error) {
      toast.error('Failed to load leads');
      console.error('Error loading leads:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteLead = async (leadId) => {
    if (window.confirm('Are you sure you want to delete this lead?')) {
      try {
        await leadService.deleteLead(leadId);
        toast.success('Lead deleted successfully');
        loadLeads();
      } catch (error) {
        toast.error('Failed to delete lead');
        console.error('Error deleting lead:', error);
      }
    }
  };

  const handleStatusChange = async (leadId, newStatus) => {
    try {
      await leadService.updateLeadStatus(leadId, newStatus);
      toast.success('Lead status updated');
      loadLeads();
    } catch (error) {
      toast.error('Failed to update lead status');
      console.error('Error updating lead status:', error);
    }
  };

  const getSourceColor = (source) => {
    switch (source) {
      case 'WEBSITE':
        return 'bg-blue-100 text-blue-800';
      case 'REFERRAL':
        return 'bg-green-100 text-green-800';
      case 'SOCIAL_MEDIA':
        return 'bg-purple-100 text-purple-800';
      case 'COLD_CALL':
        return 'bg-gray-100 text-gray-800';
      case 'EVENT':
        return 'bg-orange-100 text-orange-800';
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
      {/* Enhanced Header */}
      <PageHeader
        title="Lead Management"
        subtitle="Track and manage your sales pipeline with visual insights"
        actions={[
          {
            label: 'Add Lead',
            icon: <Plus className="h-4 w-4" />,
            onClick: () => navigate('/app/leads/new'),
            className: 'bg-green-600 hover:bg-green-700 focus:ring-green-500'
          }
        ]}
        showSearch={true}
        searchPlaceholder="Search leads..."
        onSearch={setSearchTerm}
        showFilters={false}
      />

      {/* Pipeline Overview */}
      <div className="bg-white p-6 rounded-lg shadow-sm border">
        <h3 className="text-lg font-medium text-gray-900 mb-4 flex items-center">
          <BarChart3 className="h-5 w-5 mr-2 text-primary-600" />
          Pipeline Overview
        </h3>
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-7 gap-2 sm:gap-4">
          {pipelineStages.map((stage) => (
            <div key={stage.id} className="text-center">
              <div className={`inline-flex items-center px-2 sm:px-3 py-1 rounded-full text-xs sm:text-sm font-medium ${stage.color} mb-2`}>
                {stage.name}
              </div>
              <div className="text-lg sm:text-2xl font-bold text-gray-900">{stage.count}</div>
              <div className="text-xs text-gray-500">leads</div>
            </div>
          ))}
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white p-3 sm:p-4 rounded-lg shadow-sm border">
        <div className="flex flex-col sm:flex-row gap-3 sm:gap-4">
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-1">Lead Source</label>
            <select
              value={selectedSource}
              onChange={(e) => setSelectedSource(e.target.value)}
              className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 text-sm"
            >
              <option value="ALL">All Sources</option>
              <option value="WEBSITE">Website</option>
              <option value="REFERRAL">Referral</option>
              <option value="SOCIAL_MEDIA">Social Media</option>
              <option value="COLD_CALL">Cold Call</option>
              <option value="EVENT">Event</option>
            </select>
          </div>
          
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-1">Status</label>
            <select
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
              className="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500 text-sm"
            >
              <option value="ALL">All Statuses</option>
              {pipelineStages.map(stage => (
                <option key={stage.id} value={stage.id}>{stage.name}</option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Results Summary */}
      <div className="flex items-center justify-between">
        <p className="text-sm text-gray-700">
          Showing {filteredLeads.length} of {leads.length} leads
        </p>
        <div className="text-sm text-gray-500">
          {searchTerm && `Search results for "${searchTerm}"`}
        </div>
      </div>

      {/* Kanban Board */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 2xl:grid-cols-7 gap-4 lg:gap-6">
        {pipelineStages.map((stage) => {
          const stageLeads = filteredLeads.filter(lead => lead.leadStatus === stage.id);
          
          return (
            <div key={stage.id} className="space-y-4">
              {/* Stage Header */}
              <div className="flex items-center justify-between">
                <h3 className={`text-xs sm:text-sm font-medium px-2 sm:px-3 py-1 rounded-full ${stage.color}`}>
                  {stage.name} ({stageLeads.length})
                </h3>
              </div>

              {/* Stage Column */}
              <div className="min-h-[400px] sm:min-h-[500px] lg:min-h-[600px] bg-gray-50 rounded-lg p-2 sm:p-3 space-y-2 sm:space-y-3">
                {stageLeads.map((lead) => (
                  <LeadCard
                    key={lead.id}
                    lead={lead}
                    onDelete={handleDeleteLead}
                    onStatusChange={handleStatusChange}
                    pipelineStages={pipelineStages}
                  />
                ))}
                
                {stageLeads.length === 0 && (
                  <div className="text-center py-6 sm:py-8 text-gray-400">
                    <Target className="h-6 w-6 sm:h-8 sm:w-8 mx-auto mb-2" />
                    <p className="text-xs">No leads</p>
                  </div>
                )}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

// Lead Card Component
const LeadCard = ({ lead, onDelete, onStatusChange, pipelineStages }) => {
  const [showActions, setShowActions] = useState(false);

  const getSourceColor = (source) => {
    switch (source) {
      case 'WEBSITE':
        return 'bg-blue-100 text-blue-800';
      case 'REFERRAL':
        return 'bg-green-100 text-green-800';
      case 'SOCIAL_MEDIA':
        return 'bg-purple-100 text-purple-800';
      case 'COLD_CALL':
        return 'bg-gray-100 text-gray-800';
      case 'EVENT':
        return 'bg-orange-100 text-orange-800';
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

  return (
    <div className="bg-white rounded-lg shadow-sm border hover:shadow-md transition-shadow p-2 sm:p-4">
      {/* Lead Header */}
      <div className="flex items-start justify-between mb-2 sm:mb-3">
        <div className="flex-1 min-w-0">
          <h4 className="text-xs sm:text-sm font-semibold text-gray-900 truncate">
            {lead.title || 'Untitled Lead'}
          </h4>
          <div className="flex items-center space-x-1 sm:space-x-2 mt-1">
            <span className={`inline-flex px-1 sm:px-2 py-1 text-xs font-semibold rounded-full ${getSourceColor(lead.leadSource)}`}>
              {lead.leadSource}
            </span>
          </div>
        </div>
        
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
                  to={`/app/leads/${lead.id}/edit`}
                  className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  <Edit className="h-4 w-4 mr-2" />
                  Edit
                </Link>
                <Link
                  to={`/app/leads/${lead.id}`}
                  className="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  <Eye className="h-4 w-4 mr-2" />
                  View
                </Link>
                <button
                  onClick={() => onDelete(lead.id)}
                  className="flex items-center w-full px-4 py-2 text-sm text-red-700 hover:bg-red-50"
                >
                  <Trash2 className="h-4 w-4 mr-2" />
                  Delete
                </button>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Customer Info */}
      {lead.customer && (
        <div className="mb-2 sm:mb-3">
          <div className="flex items-center text-xs text-gray-600 mb-1">
            <Building2 className="h-3 w-3 mr-1" />
            <span className="truncate">{lead.customer.companyName || 'Unknown Company'}</span>
          </div>
          {lead.customer.contactPerson && (
            <div className="flex items-center text-xs text-gray-600">
              <User className="h-3 w-3 mr-1" />
              <span className="truncate">{lead.customer.contactPerson}</span>
            </div>
          )}
        </div>
      )}

      {/* Lead Details */}
      <div className="space-y-1 sm:space-y-2 mb-2 sm:mb-3">
        {lead.estimatedValue && (
          <div className="flex items-center text-xs text-gray-600">
            <DollarSign className="h-3 w-3 mr-1" />
            {formatCurrency(lead.estimatedValue)}
          </div>
        )}
        
        {lead.createdAt && (
          <div className="flex items-center text-xs text-gray-600">
            <Calendar className="h-3 w-3 mr-1" />
            {formatDate(lead.createdAt)}
          </div>
        )}
      </div>

      {/* Description */}
      {lead.description && (
        <p className="text-xs text-gray-600 mb-2 sm:mb-3 line-clamp-2">
          {lead.description}
        </p>
      )}

      {/* Status Change Dropdown */}
      <div className="mt-2 sm:mt-3">
        <select
          value={lead.leadStatus}
          onChange={(e) => onStatusChange(lead.id, e.target.value)}
          className="block w-full px-1 sm:px-2 py-1 text-xs border border-gray-300 rounded focus:outline-none focus:ring-1 focus:ring-primary-500"
        >
          {pipelineStages.map(stage => (
            <option key={stage.id} value={stage.id}>
              {stage.name}
            </option>
          ))}
        </select>
      </div>
    </div>
  );
};

export default Leads;
