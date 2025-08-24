import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  Users, 
  TrendingUp, 
  DollarSign,
  Plus,
  Eye,
  UserPlus,
  Target,
  BarChart3,
  CheckCircle
} from 'lucide-react';
import { customerService } from '../services/customerService';
import { leadService } from '../services/leadService';
import { useAuth } from '../context/AuthContext';
import PageHeader from '../components/PageHeader';
import toast from 'react-hot-toast';

const Dashboard = () => {
  const navigate = useNavigate();
  const [stats, setStats] = useState({
    totalCustomers: 0,
    totalUsers: 0,
    totalLeads: 0,
    totalRevenue: 0,
    growthRate: 0
  });
  const [recentCustomers, setRecentCustomers] = useState([]);
  const [recentLeads, setRecentLeads] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    loadDashboardData();
  }, [user]);

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      
      // Load customers and leads for stats
      const [customers, leads] = await Promise.all([
        customerService.getAllCustomers(),
        leadService.getAllLeads()
      ]);
      
      setRecentCustomers(customers.slice(0, 5)); // Get last 5 customers
      setRecentLeads(leads.slice(0, 5)); // Get last 5 leads
      
             // Calculate stats based on user role
       const baseStats = {
         totalCustomers: customers.length,
         totalLeads: leads.length,
         totalRevenue: customers.reduce((sum, customer) => sum + (customer.annualRevenue || 0), 0),
         growthRate: 12.5 // Hardcoded for demo
       };
       
       console.log('Dashboard Data Debug:', {
         customers: customers.length,
         leads: leads.length,
         user: user?.username,
         userRole: user?.role
       });

             // Role-based stats
       console.log('User role check:', user?.role, typeof user?.role);
       
       if (user?.role === 'ADMIN' || user?.role === 'admin') {
        setStats({
          ...baseStats,
          totalUsers: 3, // Admin sees all users
          systemHealth: 'Excellent'
        });
       } else if (user?.role === 'MANAGER' || user?.role === 'manager') {
         setStats({
           ...baseStats,
           totalUsers: 2, // Manager sees team members
           teamPerformance: 'Good'
         });
       } else if (user?.role === 'SALES_REP' || user?.role === 'salesrep' || user?.role === 'sales_rep') {
         // SALES_REP - filter leads by assigned user
        const userLeads = leads.filter(lead => {
          // Check multiple possible ways the lead might be assigned
          return (
            lead.assignedTo?.username === user?.username ||
            lead.assignedTo?.email === user?.email ||
            lead.assignedTo?.id === user?.id ||
            // If no assignedTo, show all leads for sales rep (fallback)
            !lead.assignedTo
          );
        });
        
        console.log('Sales Rep Dashboard Debug:', {
          user: user?.username,
          totalLeads: leads.length,
          userLeads: userLeads.length,
          leads: leads.map(l => ({ id: l.id, assignedTo: l.assignedTo?.username || l.assignedTo?.email || 'None' }))
        });
        
        setStats({
          ...baseStats,
          totalLeads: userLeads.length,
          conversionRate: '15%',
          monthlyTarget: '85%'
        });
       } else {
         // Fallback for any other roles - show all leads
         console.log('Unknown role, showing all leads:', user?.role);
         setStats({
           ...baseStats,
           totalLeads: leads.length,
           conversionRate: '10%',
           monthlyTarget: '80%'
         });
       }
      
    } catch (error) {
      toast.error('Failed to load dashboard data');
      console.error('Dashboard error:', error);
    } finally {
      setLoading(false);
    }
  };

  const StatCard = ({ title, value, icon: Icon, color, change, link, subtitle }) => (
    <div className="overflow-hidden shadow rounded-lg hover:shadow-md transition-all" style={{ 
      backgroundColor: 'var(--bg-primary)',
      borderColor: 'var(--border-color)',
      border: '1px solid var(--border-color)'
    }}>
      {link ? (
        <Link to={link} className="block">
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <div className={`inline-flex items-center justify-center h-12 w-12 rounded-md bg-${color}-100 text-${color}-600`}>
                  <Icon className="h-6 w-6" />
                </div>
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium truncate" style={{ color: 'var(--text-secondary)' }}>{title}</dt>
                  <dd className="text-lg font-medium" style={{ color: 'var(--text-primary)' }}>{value}</dd>
                  {subtitle && (
                    <dd className="text-xs mt-1" style={{ color: 'var(--text-secondary)' }}>{subtitle}</dd>
                  )}
                </dl>
              </div>
            </div>
          </div>
          {change && (
            <div className="px-5 py-3" style={{ backgroundColor: 'var(--bg-secondary)' }}>
              <div className="text-sm">
                <span className="text-green-600 font-medium">+{change}%</span>
                <span style={{ color: 'var(--text-secondary)' }}> from last month</span>
              </div>
            </div>
          )}
        </Link>
      ) : (
        <>
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <div className={`inline-flex items-center justify-center h-12 w-12 rounded-md bg-${color}-100 text-${color}-600`}>
                  <Icon className="h-6 w-6" />
                </div>
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium truncate" style={{ color: 'var(--text-secondary)' }}>{title}</dt>
                  <dd className="text-lg font-medium" style={{ color: 'var(--text-primary)' }}>{value}</dd>
                  {subtitle && (
                    <dd className="text-xs mt-1" style={{ color: 'var(--text-secondary)' }}>{subtitle}</dd>
                  )}
                </dl>
              </div>
            </div>
          </div>
          {change && (
            <div className="px-5 py-3" style={{ backgroundColor: 'var(--bg-secondary)' }}>
              <div className="text-sm">
                <span className="text-green-600 font-medium">+{change}%</span>
                <span style={{ color: 'var(--text-secondary)' }}> from last month</span>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );

  const QuickActionCard = ({ title, description, icon: Icon, link, color }) => (
    <Link
      to={link}
      className="group relative p-6 focus-within:ring-2 focus-within:ring-inset focus-within:ring-primary-500 rounded-lg shadow hover:shadow-md transition-all"
      style={{ 
        backgroundColor: 'var(--bg-primary)',
        borderColor: 'var(--border-color)',
        border: '1px solid var(--border-color)'
      }}
    >
      <div>
        <span className={`inline-flex p-3 rounded-lg bg-${color}-100 text-${color}-600 group-hover:bg-${color}-200 transition-colors`}>
          <Icon className="h-6 w-6" />
        </span>
      </div>
      <div className="mt-4">
        <h3 className="text-lg font-medium group-hover:text-primary-600 transition-colors" style={{ color: 'var(--text-primary)' }}>
          {title}
        </h3>
        <p className="mt-2 text-sm group-hover:text-gray-700 transition-colors" style={{ color: 'var(--text-secondary)' }}>
          {description}
        </p>
      </div>
      <span className="absolute top-6 right-6 transition-colors" style={{ color: 'var(--text-secondary)' }} aria-hidden="true">
        <svg className="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
          <path d="M20 4h1a1 1 0 00-1-1v1zm-1 12a1 1 0 102 0h-2zM8 3a1 1 0 000 2V3zM3.293 19.293a1 1 0 101.414 1.414l-1.414-1.414zM19 4v12h2V4h-2zm1-1H8v2h12V3zm-.707.293l-16 16 1.414 1.414 16-16-1.414-1.414z" />
        </svg>
      </span>
    </Link>
  );

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Enhanced Header with Navigation */}
      <PageHeader
        title="Dashboard"
        subtitle={
          user?.role === 'ADMIN' 
            ? `Welcome back, ${user?.firstName}! Here's your complete system overview.`
            : user?.role === 'MANAGER'
            ? `Welcome back, ${user?.firstName}! Here's your team's performance overview.`
            : `Welcome back, ${user?.firstName}! Here's your sales performance overview.`
        }
        actions={[
          {
            label: 'Add Customer',
            icon: <Plus className="h-4 w-4" />,
            onClick: () => navigate('/app/customers/new'),
            className: 'bg-green-600 hover:bg-green-700 focus:ring-green-500'
          }
        ]}
      />

      {/* Role-based Stats Grid */}
      <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard
          title="Total Customers"
          value={stats.totalCustomers}
          icon={Users}
          color="blue"
          change={stats.growthRate}
          link="/app/customers"
        />
        
        {user?.role === 'ADMIN' && (
          <StatCard
            title="Total Users"
            value={stats.totalUsers}
            icon={UserPlus}
            color="green"
            link="/app/users"
            subtitle="System Users"
          />
        )}
        
        {user?.role === 'MANAGER' && (
          <StatCard
            title="Team Members"
            value={stats.totalUsers}
            icon={UserPlus}
            color="green"
            link="/app/users"
            subtitle="Your Team"
          />
        )}
        
        {user?.role === 'ADMIN' && (
          <StatCard
            title="Total Leads"
            value={stats.totalLeads}
            icon={Target}
            color="purple"
            link="/app/leads"
            subtitle="All Leads"
          />
        )}
        
        {user?.role === 'MANAGER' && (
          <StatCard
            title="Team Leads"
            value={stats.totalLeads}
            icon={Target}
            color="purple"
            link="/app/leads"
            subtitle="Team Pipeline"
          />
        )}
        
        {user?.role === 'SALES_REP' && (
          <StatCard
            title="My Leads"
            value={stats.totalLeads}
            icon={Target}
            color="purple"
            link="/app/leads"
            subtitle="Your Pipeline"
          />
        )}
        
        <StatCard
          title="Total Revenue"
          value={`$${stats.totalRevenue.toLocaleString()}`}
          icon={DollarSign}
          color="yellow"
          change={8.2}
        />
        
        {user?.role === 'SALES_REP' && (
          <StatCard
            title="Conversion Rate"
            value={stats.conversionRate}
            icon={TrendingUp}
            color="purple"
            subtitle="This Month"
          />
        )}
        
        {user?.role !== 'SALES_REP' && (
          <StatCard
            title="Growth Rate"
            value={`${stats.growthRate}%`}
            icon={TrendingUp}
            color="purple"
            change={2.1}
          />
        )}
        
        {user?.role === 'SALES_REP' && (
          <StatCard
            title="Monthly Target"
            value={stats.monthlyTarget}
            icon={BarChart3}
            color="green"
            subtitle="Progress"
          />
        )}
        
        {user?.role === 'ADMIN' && (
          <StatCard
            title="System Health"
            value={stats.systemHealth}
            icon={BarChart3}
            color="green"
            subtitle="Status"
          />
        )}
        
        {user?.role === 'MANAGER' && (
          <StatCard
            title="Team Performance"
            value={stats.teamPerformance}
            icon={BarChart3}
            color="green"
            subtitle="Rating"
          />
        )}
      </div>

      {/* Role-based Quick Actions */}
      <div>
        <h2 className="text-lg font-medium text-gray-900 mb-4">Quick Actions</h2>
        <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
          <QuickActionCard
            title="Add New Customer"
            description="Create a new customer profile with contact information and details."
            icon={Plus}
            link="/app/customers/new"
            color="blue"
          />
          <QuickActionCard
            title="View Customers"
            description="Browse and manage all your customer relationships."
            icon={Users}
            link="/app/customers"
            color="green"
          />
          <QuickActionCard
            title="Manage Tasks"
            description="Track, assign, and manage team tasks and activities."
            icon={CheckCircle}
            link="/app/tasks"
            color="orange"
          />
          
                     {(user?.role === 'ADMIN' || user?.role === 'MANAGER') && (
             <QuickActionCard
               title={user?.role === 'ADMIN' ? "User Management" : "Team Overview"}
               description={user?.role === 'ADMIN' ? "Manage team members, roles, and permissions." : "Monitor your team's performance and manage resources."}
               icon={UserPlus}
               link="/app/users"
               color="purple"
             />
           )}
          
          {user?.role === 'ADMIN' && (
            <QuickActionCard
              title="System Analytics"
              description="View comprehensive system performance and health metrics."
              icon={BarChart3}
              link="/app/analytics"
              color="indigo"
            />
          )}
          
          
          
          {user?.role === 'SALES_REP' && (
            <QuickActionCard
              title="Lead Management"
              description="Track and manage your sales leads and opportunities."
              icon={Target}
              link="/app/leads"
              color="purple"
            />
          )}
          
          {user?.role === 'SALES_REP' && (
            <QuickActionCard
              title="Performance Tracking"
              description="Monitor your sales metrics and conversion rates."
              icon={BarChart3}
              link="/app/analytics"
              color="indigo"
            />
          )}
        </div>
      </div>

      {/* Recent Customers */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-medium text-gray-900">Recent Customers</h2>
          <Link
            to="/app/customers"
            className="text-sm font-medium text-primary-600 hover:text-primary-500 transition-colors"
          >
            View all
          </Link>
        </div>
        
        <div className="bg-white shadow overflow-hidden sm:rounded-md">
          <ul className="divide-y divide-gray-200">
            {recentCustomers.length > 0 ? (
              recentCustomers.map((customer) => (
                <li key={customer.id}>
                  <Link to={`/app/customers/${customer.id}/edit`} className="block hover:bg-gray-50 transition-colors">
                    <div className="px-4 py-4 sm:px-6">
                      <div className="flex items-center justify-between">
                        <div className="flex items-center">
                          <div className="flex-shrink-0">
                            <div className="h-10 w-10 rounded-full bg-primary-100 flex items-center justify-center">
                              <span className="text-sm font-medium text-primary-600">
                                {customer.companyName?.charAt(0) || customer.firstName?.charAt(0) || 'C'}
                              </span>
                            </div>
                          </div>
                          <div className="ml-4">
                            <div className="text-sm font-medium text-gray-900">
                              {customer.companyName || `${customer.firstName} ${customer.lastName}`}
                            </div>
                            <div className="text-sm text-gray-500">
                              {customer.email}
                            </div>
                          </div>
                        </div>
                        <div className="flex items-center">
                          <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                            customer.status === 'ACTIVE' ? 'bg-green-100 text-green-800' :
                            customer.status === 'INACTIVE' ? 'bg-red-100 text-red-800' :
                            'bg-gray-100 text-gray-800'
                          }`}>
                            {customer.status}
                          </span>
                          <Eye className="ml-2 h-4 w-4 text-gray-400" />
                        </div>
                      </div>
                    </div>
                  </Link>
                </li>
              ))
            ) : (
              <li className="px-4 py-8 text-center text-gray-500">
                <Users className="mx-auto h-12 w-12 text-gray-400" />
                <h3 className="mt-2 text-sm font-medium text-gray-900">No customers yet</h3>
                <p className="mt-1 text-sm text-gray-500">
                  Get started by creating your first customer.
                </p>
                <div className="mt-6">
                  <Link
                    to="/app/customers/new"
                    className="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors"
                  >
                    <Plus className="h-4 w-4 mr-2" />
                    Add Customer
                  </Link>
                </div>
              </li>
            )}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
