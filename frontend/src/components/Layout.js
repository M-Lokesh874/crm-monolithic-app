import React, { useState } from 'react';
import { Outlet, useNavigate, useLocation, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { 
  Home, 
  Users, 
  Building2, 
  Target,
  BarChart3, 
  Settings, 
  LogOut, 
  Menu, 
  X,
  User,
  CheckCircle
} from 'lucide-react';

const Layout = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const navigation = [
    { name: 'Dashboard', href: '/app/dashboard', icon: Home },
    { name: 'Customers', href: '/app/customers', icon: Building2 },
    { name: 'Leads', href: '/app/leads', icon: Target },
    { name: 'Tasks', href: '/app/tasks', icon: CheckCircle },
    // Only show Users for ADMIN and MANAGER roles
    ...(user?.role === 'ADMIN' || user?.role === 'MANAGER' ? [{ name: 'Users', href: '/app/users', icon: Users }] : []),
    { name: 'Analytics', href: '/app/analytics', icon: BarChart3 },
    { name: 'Settings', href: '/app/settings', icon: Settings },
  ];

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  const isActiveRoute = (href) => {
    return location.pathname === href || location.pathname.startsWith(href + '/');
  };

  return (
    <div className="min-h-screen" style={{ backgroundColor: 'var(--bg-secondary)' }}>
      {/* Mobile sidebar */}
      <div className={`fixed inset-0 z-50 lg:hidden ${sidebarOpen ? 'block' : 'hidden'}`}>
        <div className="fixed inset-0 bg-gray-600 bg-opacity-75" onClick={() => setSidebarOpen(false)} />
        <div className="fixed inset-y-0 left-0 flex w-64 flex-col" style={{ backgroundColor: 'var(--bg-primary)' }}>
          <div className="flex h-16 items-center justify-between px-4">
            <h1 className="text-xl font-bold" style={{ color: 'var(--text-primary)' }}>CRM System</h1>
            <button
              onClick={() => setSidebarOpen(false)}
              className="hover:opacity-80 transition-opacity"
              style={{ color: 'var(--text-secondary)' }}
            >
              <X className="h-6 w-6" />
            </button>
          </div>
                     <nav className="flex-1 space-y-1 px-2 py-4">
             {navigation.map((item) => {
               const Icon = item.icon;
               return (
                 <Link
                   key={item.name}
                   to={item.href}
                   onClick={() => setSidebarOpen(false)}
                   className={`group flex items-center px-2 py-2 text-sm font-medium rounded-md transition-colors ${
                     isActiveRoute(item.href)
                       ? 'bg-primary-100 text-primary-900'
                       : 'hover:bg-opacity-10'
                   }`}
                   style={{ 
                     color: isActiveRoute(item.href) ? undefined : 'var(--text-secondary)',
                     backgroundColor: isActiveRoute(item.href) ? undefined : 'transparent'
                   }}
                 >
                   <Icon className="mr-3 h-5 w-5" />
                   {item.name}
                 </Link>
               );
             })}
           </nav>
        </div>
      </div>

      {/* Desktop sidebar */}
      <div className="hidden lg:fixed lg:inset-y-0 lg:flex lg:w-64 lg:flex-col">
        <div className="flex flex-col flex-grow border-r" style={{ backgroundColor: 'var(--bg-primary)', borderColor: 'var(--border-color)' }}>
          <div className="flex items-center h-16 px-4 border-b" style={{ borderColor: 'var(--border-color)' }}>
            <h1 className="text-xl font-bold" style={{ color: 'var(--text-primary)' }}>CRM System</h1>
          </div>
                     <nav className="flex-1 space-y-1 px-2 py-4">
             {navigation.map((item) => {
               const Icon = item.icon;
               return (
                 <Link
                   key={item.name}
                   to={item.href}
                   className={`group flex items-center px-2 py-2 text-sm font-medium rounded-md transition-colors ${
                     isActiveRoute(item.href)
                       ? 'bg-primary-100 text-primary-900'
                       : 'hover:bg-opacity-10'
                   }`}
                   style={{ 
                     color: isActiveRoute(item.href) ? undefined : 'var(--text-secondary)',
                     backgroundColor: isActiveRoute(item.href) ? undefined : 'transparent'
                   }}
                 >
                   <Icon className="mr-3 h-5 w-5" />
                   {item.name}
                 </Link>
               );
             })}
           </nav>
        </div>
      </div>

      {/* Main content */}
      <div className="lg:pl-64">
        {/* Top bar */}
        <div className="sticky top-0 z-40 flex h-16 shrink-0 items-center gap-x-4 border-b px-4 shadow-sm sm:gap-x-6 sm:px-6 lg:px-8" style={{ backgroundColor: 'var(--bg-primary)', borderColor: 'var(--border-color)' }}>
          <button
            type="button"
            className="-m-2.5 p-2.5 lg:hidden"
            style={{ color: 'var(--text-primary)' }}
            onClick={() => setSidebarOpen(true)}
          >
            <Menu className="h-6 w-6" />
          </button>

          <div className="flex flex-1 gap-x-4 self-stretch lg:gap-x-6">
            <div className="flex flex-1"></div>
            <div className="flex items-center gap-x-4 lg:gap-x-6">
              {/* User menu */}
              <div className="relative">
                <div className="flex items-center space-x-3">
                  <div className="flex items-center space-x-2">
                    <div className="h-8 w-8 rounded-full bg-primary-600 flex items-center justify-center">
                      <User className="h-4 w-4 text-white" />
                    </div>
                    <div className="hidden md:block">
                                          <div className="text-sm font-medium" style={{ color: 'var(--text-primary)' }}>
                      {user?.firstName} {user?.lastName}
                    </div>
                    <div className="text-xs" style={{ color: 'var(--text-secondary)' }}>{user?.role}</div>
                    </div>
                  </div>
                  <button
                    onClick={handleLogout}
                    className="flex items-center space-x-2 hover:opacity-80 transition-opacity"
                    style={{ color: 'var(--text-secondary)' }}
                  >
                    <LogOut className="h-4 w-4" />
                    <span className="hidden md:block">Logout</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Page content */}
        <main className="py-6">
          <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
};

export default Layout;
