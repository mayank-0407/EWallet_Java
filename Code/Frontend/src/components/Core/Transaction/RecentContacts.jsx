import React from "react";
import { FaUserCircle } from "react-icons/fa";
import ProfileIconIntitialGenerator from "./../../../utils/profileIconIntitialGenerator";
const RecentContact = ({ user, onClick }) => (
  <div
    className="flex items-center space-x-2 p-2 hover:bg-gray-100 cursor-pointer rounded-md"
    onClick={onClick}
  >
    <div className="w-8 h-8 rounded-full bg-gray-300 flex items-center justify-center overflow-hidden">
      <ProfileIconIntitialGenerator size={32} name={user.name} />
    </div>
    <span className="text-sm">{user.name}</span>
    {/* <span className="text-sm">{user.name}</span> */}
  </div>
);

const RecentContacts = ({ contacts, onContactClick }) => (
  <div className="mt-4 w-3/4 bg-white rounded-md shadow-md p-4">
    <h3 className="text-md font-semibold mb-2">Recent Contacts</h3>
    {contacts.length > 0 ? (
      contacts
        .slice(0, 5)
        .map((contact) => (
          <RecentContact
            key={contact.transaction.id}
            user={contact}
            onClick={() => onContactClick(contact.transaction)}
          />
        ))
    ) : (
      <p className="text-sm text-gray-500">No recent contacts found.</p>
    )}
  </div>
);

export default RecentContacts;
