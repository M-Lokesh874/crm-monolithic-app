import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { ChevronRight, Home } from 'lucide-react';

const Breadcrumb = () => {
  const location = useLocation();
  const pathnames = location.pathname.split('/').filter((x) => x);

  const getBreadcrumbName = (path) => {
    const nameMap = {
      'app': 'Dashboard',
      'dashboard': 'Dashboard',
      'customers': 'Customers',
      'users': 'Users',
      'leads': 'Leads',
      'tasks': 'Tasks',
      'new': 'New',
      'edit': 'Edit'
    };
    return nameMap[path] || path.charAt(0).toUpperCase() + path.slice(1);
  };

  const buildBreadcrumbs = () => {
    const breadcrumbs = [];
    let currentPath = '';

    pathnames.forEach((name, index) => {
      currentPath += `/${name}`;
      
      // Skip certain paths or create custom logic
      if (name === 'app') {
        breadcrumbs.push({
          name: 'Home',
          path: '/',
          icon: <Home className="h-4 w-4" />
        });
      } else if (name !== 'app') {
        breadcrumbs.push({
          name: getBreadcrumbName(name),
          path: currentPath,
          isLast: index === pathnames.length - 1
        });
      }
    });

    return breadcrumbs;
  };

  const breadcrumbs = buildBreadcrumbs();

  if (breadcrumbs.length <= 1) return null;

  return (
    <nav className="flex items-center space-x-2 text-sm text-gray-500 mb-6">
      {breadcrumbs.map((breadcrumb, index) => (
        <div key={breadcrumb.path} className="flex items-center">
          {index > 0 && (
            <ChevronRight className="h-4 w-4 mx-2 text-gray-400" />
          )}
          
          {breadcrumb.isLast ? (
            <span className="text-gray-900 font-medium">
              {breadcrumb.icon && <span className="mr-1">{breadcrumb.icon}</span>}
              {breadcrumb.name}
            </span>
          ) : (
            <Link
              to={breadcrumb.path}
              className="hover:text-primary-600 transition-colors flex items-center"
            >
              {breadcrumb.icon && <span className="mr-1">{breadcrumb.icon}</span>}
              {breadcrumb.name}
            </Link>
          )}
        </div>
      ))}
    </nav>
  );
};

export default Breadcrumb;
