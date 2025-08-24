import React from 'react';
import { Plus, Search, Filter } from 'lucide-react';
import BackButton from './BackButton';
import Breadcrumb from './Breadcrumb';

const PageHeader = ({ 
  title, 
  subtitle, 
  showBack = false, 
  showBreadcrumb = true,
  backPath,
  showHome = false,
  actions = [],
  searchPlaceholder,
  onSearch,
  showSearch = false,
  showFilters = false,
  onFiltersClick
}) => {
  return (
    <div className="bg-white border-b border-gray-200 px-6 py-4">
      {/* Breadcrumb Navigation */}
      {showBreadcrumb && <Breadcrumb />}
      
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between">
        <div className="flex-1 min-w-0">
          {/* Back Button */}
          {showBack && (
            <div className="mb-4">
              <BackButton fallbackPath={backPath} showHome={showHome} />
            </div>
          )}
          
          {/* Title and Subtitle */}
          <div>
            <h1 className="text-2xl font-bold text-gray-900 sm:text-3xl">
              {title}
            </h1>
            {subtitle && (
              <p className="mt-2 text-sm text-gray-600 max-w-4xl">
                {subtitle}
              </p>
            )}
          </div>
        </div>
        
        {/* Actions */}
        <div className="mt-4 sm:mt-0 sm:ml-4 flex flex-col sm:flex-row gap-3">
          {/* Search */}
          {showSearch && (
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <Search className="h-5 w-5 text-gray-400" />
              </div>
              <input
                type="text"
                placeholder={searchPlaceholder || "Search..."}
                onChange={(e) => onSearch?.(e.target.value)}
                className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-1 focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
              />
            </div>
          )}
          
          {/* Filters */}
          {showFilters && (
            <button
              onClick={onFiltersClick}
              className="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors"
            >
              <Filter className="h-4 w-4 mr-2" />
              Filters
            </button>
          )}
          
          {/* Action Buttons */}
          {actions.map((action, index) => (
            <button
              key={index}
              onClick={action.onClick}
              className={`inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors ${action.className || ''}`}
            >
              {action.icon && <span className="mr-2">{action.icon}</span>}
              {action.label}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default PageHeader;
