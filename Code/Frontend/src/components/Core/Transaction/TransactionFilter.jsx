import React, { useState } from "react";
import { FaFilter } from "react-icons/fa";

const TransactionFilter = ({ onFilter }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [filters, setFilters] = useState({
    date: "",
    status: "",
    type: "",
  });

  const toggleOpen = () => {
    setIsOpen(!isOpen);
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setFilters({ ...filters, [name]: value });
  };

  const handleApplyFilter = () => {
    onFilter(filters);
    setIsOpen(false);
  };

  const handleClearFilter = () => {
    setFilters({ date: "", status: "", type: "" });
    onFilter({ date: "", status: "", type: "" });
    setIsOpen(false);
  };

  return (
    <div className="relative inline-block text-left">
      <div>
        <button
          type="button"
          className="inline-flex justify-center w-full rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          id="menu-button"
          aria-expanded={isOpen}
          aria-haspopup="true"
          onClick={toggleOpen}
        >
          <FaFilter className="mr-2 -ml-1 h-5 w-5 text-gray-400" />
          Filter
          <svg
            className="-mr-1 ml-2 h-5 w-5"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 20 20"
            fill="currentColor"
            aria-hidden="true"
          >
            <path
              fillRule="evenodd"
              d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
              clipRule="evenodd"
            />
          </svg>
        </button>
      </div>

      {isOpen && (
        <div
          className="absolute right-0 z-10 mt-2 w-56 origin-top-right rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none"
          role="menu"
          aria-orientation="vertical"
          aria-labelledby="menu-button"
        >
          <div className="py-1" role="none">
            <div className="px-4 py-2 text-sm text-gray-700" role="menuitem">
              <label
                htmlFor="filter-date"
                className="block text-gray-500 text-xs font-semibold mb-1"
              >
                Date
              </label>
              <input
                type="date"
                id="filter-date"
                name="date"
                value={filters.date}
                onChange={handleInputChange}
                className="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md"
                placeholder="YYYY-MM-DD"
              />
            </div>
            <div className="px-4 py-2 text-sm text-gray-700" role="menuitem">
              <label
                htmlFor="filter-status"
                className="block text-gray-500 text-xs font-semibold mb-1"
              >
                Status
              </label>
              <select
                id="filter-status"
                name="status"
                value={filters.status}
                onChange={handleInputChange}
                className="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md"
              >
                <option value="">All</option>
                <option value="Processing">Processing</option>
                <option value="Success">Success</option>
                <option value="Failed">Failed</option>
              </select>
            </div>
            <div className="px-4 py-2 text-sm text-gray-700" role="menuitem">
              <label
                htmlFor="filter-type"
                className="block text-gray-500 text-xs font-semibold mb-1"
              >
                Type
              </label>
              <select
                id="filter-type"
                name="type"
                value={filters.type}
                onChange={handleInputChange}
                className="shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border-gray-300 rounded-md"
              >
                <option value="">All</option>
                <option value="debit">Debit</option>
                <option value="credit">Credit</option>
              </select>
            </div>
            <div className="flex justify-end px-4 py-3 bg-gray-50 sm:px-6">
              <button
                type="button"
                className="inline-flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                onClick={handleClearFilter}
              >
                Clear
              </button>
              <button
                type="button"
                className="ml-3 inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                onClick={handleApplyFilter}
              >
                Apply
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default TransactionFilter;
