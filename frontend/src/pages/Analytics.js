import React, { useState, useEffect } from 'react';
import { 
  BarChart3, 
  TrendingUp, 
  Users, 
  Target, 
  DollarSign,
  Building2,
  Calendar,
  Activity,
  Download,
  Filter,
  RefreshCw
} from 'lucide-react';
import { Line, Bar, Doughnut, Pie } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { customerService } from '../services/customerService';
import { leadService } from '../services/leadService';
import { useAuth } from '../context/AuthContext';
import PageHeader from '../components/PageHeader';
import toast from 'react-hot-toast';

// Register Chart.js components
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend
);

const Analytics = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    totalCustomers: 0,
    totalLeads: 0,
    totalRevenue: 0,
    conversionRate: 0,
    monthlyGrowth: 0
  });
  const [chartData, setChartData] = useState({});
  const [dateRange, setDateRange] = useState('30');

  useEffect(() => {
    loadAnalyticsData();
  }, [dateRange]);

  const loadAnalyticsData = async () => {
    try {
      setLoading(true);
      
      // Load all data for analytics
      const [customers, leads] = await Promise.all([
        customerService.getAllCustomers(),
        leadService.getAllLeads()
      ]);

      // Calculate real statistics
      const totalCustomers = customers.length;
      const totalLeads = leads.length;
      const totalRevenue = customers.reduce((sum, customer) => sum + (customer.annualRevenue || 0), 0);
      const convertedLeads = leads.filter(lead => lead.leadStatus === 'CONVERTED').length;
      const conversionRate = totalLeads > 0 ? ((convertedLeads / totalLeads) * 100).toFixed(1) : 0;

      setStats({
        totalCustomers,
        totalLeads,
        totalRevenue,
        conversionRate: parseFloat(conversionRate),
        monthlyGrowth: 12.5 // Mock growth for demo
      });

      // Prepare chart data
      prepareChartData(customers, leads);
      
    } catch (error) {
      console.error('Error loading analytics:', error);
      toast.error('Failed to load analytics data');
    } finally {
      setLoading(false);
    }
  };

  const prepareChartData = (customers, leads) => {
    // Lead Status Distribution
    const leadStatusData = {
      labels: ['New', 'Contacted', 'Qualified', 'Proposal', 'Negotiation', 'Converted', 'Lost'],
      datasets: [{
        data: [
          leads.filter(l => l.leadStatus === 'NEW').length,
          leads.filter(l => l.leadStatus === 'CONTACTED').length,
          leads.filter(l => l.leadStatus === 'QUALIFIED').length,
          leads.filter(l => l.leadStatus === 'PROPOSAL').length,
          leads.filter(l => l.leadStatus === 'NEGOTIATION').length,
          leads.filter(l => l.leadStatus === 'CONVERTED').length,
          leads.filter(l => l.leadStatus === 'LOST').length,
        ],
        backgroundColor: [
          '#3B82F6', '#10B981', '#F59E0B', '#8B5CF6', '#F97316', '#059669', '#EF4444'
        ],
        borderWidth: 2,
        borderColor: '#fff'
      }]
    };

    // Monthly Revenue Trend (mock data for demo)
    const monthlyRevenueData = {
      labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
      datasets: [{
        label: 'Revenue ($)',
        data: [45000, 52000, 48000, 61000, 55000, 68000],
        borderColor: '#3B82F6',
        backgroundColor: 'rgba(59, 130, 246, 0.1)',
        tension: 0.4,
        fill: true
      }]
    };

    // Customer Industry Distribution
    const industryData = {
      labels: ['Technology', 'Healthcare', 'Finance', 'Manufacturing', 'Retail', 'Other'],
      datasets: [{
        data: [
          customers.filter(c => c.industry === 'TECHNOLOGY').length,
          customers.filter(c => c.industry === 'HEALTHCARE').length,
          customers.filter(c => c.industry === 'FINANCE').length,
          customers.filter(c => c.industry === 'MANUFACTURING').length,
          customers.filter(c => c.industry === 'RETAIL').length,
          customers.filter(c => !['TECHNOLOGY', 'HEALTHCARE', 'FINANCE', 'MANUFACTURING', 'RETAIL'].includes(c.industry)).length,
        ],
        backgroundColor: [
          '#3B82F6', '#10B981', '#F59E0B', '#8B5CF6', '#F97316', '#6B7280'
        ],
        borderWidth: 2,
        borderColor: '#fff'
      }]
    };

    // Lead Source Performance
    const leadSourceData = {
      labels: ['Website', 'Referral', 'Cold Call', 'Social Media', 'Trade Show', 'Other'],
      datasets: [{
        label: 'Leads Generated',
        data: [
          leads.filter(l => l.leadSource === 'WEBSITE').length,
          leads.filter(l => l.leadSource === 'REFERRAL').length,
          leads.filter(l => l.leadSource === 'COLD_CALL').length,
          leads.filter(l => l.leadSource === 'SOCIAL_MEDIA').length,
          leads.filter(l => l.leadSource === 'TRADE_SHOW').length,
          leads.filter(l => !['WEBSITE', 'REFERRAL', 'COLD_CALL', 'SOCIAL_MEDIA', 'TRADE_SHOW'].includes(l.leadSource)).length,
        ],
        backgroundColor: 'rgba(59, 130, 246, 0.8)',
        borderColor: '#3B82F6',
        borderWidth: 2,
        borderRadius: 4
      }]
    };

    setChartData({
      leadStatus: leadStatusData,
      monthlyRevenue: monthlyRevenueData,
      industry: industryData,
      leadSource: leadSourceData
    });
  };

  const exportReport = (format) => {
    toast.success(`${format.toUpperCase()} report exported successfully!`);
    // In real app, this would generate and download the file
  };

  const StatCard = ({ title, value, icon: Icon, color, change, subtitle }) => (
    <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm font-medium text-gray-600">{title}</p>
          <p className="text-2xl font-bold text-gray-900">{value}</p>
          {subtitle && <p className="text-sm text-gray-500 mt-1">{subtitle}</p>}
          {change && (
            <div className={`flex items-center mt-2 text-sm ${change > 0 ? 'text-green-600' : 'text-red-600'}`}>
              <TrendingUp className={`w-4 h-4 mr-1 ${change < 0 ? 'transform rotate-180' : ''}`} />
              {Math.abs(change)}% from last month
            </div>
          )}
        </div>
        <div className={`p-3 rounded-full bg-${color}-100`}>
          <Icon className={`w-6 h-6 text-${color}-600`} />
        </div>
      </div>
    </div>
  );

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading analytics...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <PageHeader 
        title="Analytics & Reports" 
        subtitle="Comprehensive business insights and performance metrics"
        showSearch={false}
        showFilters={false}
      />
      
      {/* Analytics Controls */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <div className="flex flex-col sm:flex-row gap-3 justify-end">
          <select 
            value={dateRange} 
            onChange={(e) => setDateRange(e.target.value)}
            className="px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="7">Last 7 days</option>
            <option value="30">Last 30 days</option>
            <option value="90">Last 90 days</option>
            <option value="365">Last year</option>
          </select>
          <button
            onClick={loadAnalyticsData}
            className="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200 flex items-center space-x-2"
          >
            <RefreshCw className="w-4 h-4" />
            <span>Refresh</span>
          </button>
          <button
            onClick={() => exportReport('PDF')}
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 flex items-center space-x-2"
          >
            <Download className="w-4 h-4" />
            <span>Export PDF</span>
          </button>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Key Metrics */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <StatCard 
            title="Total Customers" 
            value={stats.totalCustomers} 
            icon={Building2} 
            color="blue"
            change={8.2}
          />
          <StatCard 
            title="Total Leads" 
            value={stats.totalLeads} 
            icon={Target} 
            color="green"
            change={12.5}
          />
          <StatCard 
            title="Total Revenue" 
            value={`$${stats.totalRevenue.toLocaleString()}`} 
            icon={DollarSign} 
            color="yellow"
            change={15.3}
          />
          <StatCard 
            title="Conversion Rate" 
            value={`${stats.conversionRate}%`} 
            icon={TrendingUp} 
            color="purple"
            change={-2.1}
          />
        </div>

        {/* Charts Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">
          {/* Lead Status Distribution */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Lead Pipeline Status</h3>
            <div className="h-80">
              <Doughnut 
                data={chartData.leadStatus} 
                options={{
                  responsive: true,
                  maintainAspectRatio: false,
                  plugins: {
                    legend: {
                      position: 'bottom'
                    }
                  }
                }}
              />
            </div>
          </div>

          {/* Monthly Revenue Trend */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Monthly Revenue Trend</h3>
            <div className="h-80">
              <Line 
                data={chartData.monthlyRevenue} 
                options={{
                  responsive: true,
                  maintainAspectRatio: false,
                  plugins: {
                    legend: {
                      display: false
                    }
                  },
                  scales: {
                    y: {
                      beginAtZero: true,
                      ticks: {
                        callback: function(value) {
                          return '$' + value.toLocaleString();
                        }
                      }
                    }
                  }
                }}
              />
            </div>
          </div>
        </div>

        {/* Additional Charts */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Customer Industry Distribution */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Customer Industry Distribution</h3>
            <div className="h-80">
              <Pie 
                data={chartData.industry} 
                options={{
                  responsive: true,
                  maintainAspectRatio: false,
                  plugins: {
                    legend: {
                      position: 'bottom'
                    }
                  }
                }}
              />
            </div>
          </div>

          {/* Lead Source Performance */}
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Lead Source Performance</h3>
            <div className="h-80">
              <Bar 
                data={chartData.leadSource} 
                options={{
                  responsive: true,
                  maintainAspectRatio: false,
                  plugins: {
                    legend: {
                      display: false
                    }
                  },
                  scales: {
                    y: {
                      beginAtZero: true
                    }
                  }
                }}
              />
            </div>
          </div>
        </div>

        {/* Role-based Insights */}
        {user?.role === 'ADMIN' && (
          <div className="mt-8 bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">System Health & Performance</h3>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="text-center">
                <div className="text-2xl font-bold text-green-600">99.9%</div>
                <div className="text-sm text-gray-600">System Uptime</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-blue-600">2.1s</div>
                <div className="text-sm text-gray-600">Average Response Time</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-purple-600">1,247</div>
                <div className="text-sm text-gray-600">Total API Calls Today</div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Analytics;
