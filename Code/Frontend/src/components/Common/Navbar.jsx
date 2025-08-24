import React, { useState, useEffect, useRef } from "react";
import { FaUserCircle } from "react-icons/fa";
import { ImProfile } from "react-icons/im";
import {
  MdLogout,
  MdPassword,
  MdHelp,
  MdPin,
  MdBusiness,
} from "react-icons/md";
import { useNavigate } from "react-router-dom";
import { getCookie, logOut } from "../../services/cookieService";
import { isLogin } from "../../services/apis";
import ProfileIconIntitialGenerator from "../../utils/profileIconIntitialGenerator";
import { toast } from "react-hot-toast";
const Navbar = () => {
  const navigate = useNavigate();
  const [isProfileOpen, setIsProfileOpen] = useState(false);
  const [fullName, setFullName] = useState("");
  const [isMerchant, setIsMerchant] = useState(false);

  const profileDropdownRef = useRef(null);

  useEffect(() => {
    const fetchName = () => {
      const getFullName = getCookie("userFullName");
      setFullName(getFullName);
    };
    fetchName();
  }, []);

  const toggleProfile = () => {
    setIsProfileOpen(!isProfileOpen);
  };

  const handleClickOutside = (event) => {
    if (
      profileDropdownRef.current &&
      !profileDropdownRef.current.contains(event.target)
    ) {
      setIsProfileOpen(false);
    }
  };

  useEffect(() => {
    if (isProfileOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    } else {
      document.removeEventListener("mousedown", handleClickOutside);
    }

    var tempIsMerchant = getCookie("isMerchant");
    setIsMerchant(tempIsMerchant === "true");

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isProfileOpen]);

  const handleEditProfile = () => {
    navigate("/editprofile");
  };

  const handleChangePassword = () => {
    navigate("/changepassword");
  };
  const handleBusinessDetails = () => {
    navigate("/businessdetails");
  };
  const handleChangeWalletPin = () => {
    navigate("/changewalletpin");
  };
  const handleFAQ = () => {
    navigate("/faq");
  };

  const handleLogout = () => {
    logOut();
    toast.success("Logged Out!");
    navigate("/login");
    setIsProfileOpen(false);
  };

  const menuItems = [
    {
      icon: <ImProfile size={20} className="mr-2" />,
      text: "Edit Profile",
      onClick: handleEditProfile,
    },
    {
      icon: <MdBusiness size={20} className="mr-2" />,
      text: "Business Detail",
      onClick: handleBusinessDetails,
    },
    {
      icon: <MdPassword size={20} className="mr-2" />,
      text: "Change Password",
      onClick: handleChangePassword,
    },
    {
      icon: <MdPin size={20} className="mr-2" />,
      text: "Change Wallet Pin",
      onClick: handleChangeWalletPin,
    },
    {
      icon: <MdHelp size={20} className="mr-2" />,
      text: "FAQs",
      onClick: handleFAQ,
    },
    {
      icon: <MdLogout size={20} className="mr-2" />,
      text: "Logout",
      onClick: handleLogout,
    },
  ];

  return (
    <nav className="bg-white rounded-xl shadow-md px-6 m-2 flex items-center justify-between relative h-16">
      <div
        className="text-2xl font-bold text-gray-600 cursor-pointer"
        onClick={() => navigate("/")}
      >
        e-wallet
      </div>
      <div className="text-gray-600 text-4xl flex justify-center items-center cursor-pointer relative">
        <div onClick={toggleProfile} className="flex items-center">
          <p className="text-lg m-2 p-1">{fullName}</p>
          {/* <FaUserCircle size={32} />   */}
          {fullName != null ? (
            <ProfileIconIntitialGenerator size={32} name={fullName} />
          ) : null}
        </div>

        {isProfileOpen && (
          <div
            ref={profileDropdownRef}
            className="absolute top-full right-0 mt-2 w-[15vw] bg-white rounded-md shadow-xl z-20"
          >
            <nav>
              <ul className="text-gray-800">
                {menuItems
                  .filter((item) => {
                    if (item.text === "Business Detail" && !isMerchant) {
                      return false; // hide if not merchant
                    }
                    return true;
                  })
                  .map(({ icon, text, onClick }, index) => (
                    <li key={index} className="py-2 px-4">
                      <button
                        onClick={onClick}
                        className="flex items-center text-sm cursor-pointer hover:bg-gray-100 w-full text-left p-2 rounded-md"
                      >
                        {icon}
                        {text}
                      </button>
                    </li>
                  ))}
              </ul>
            </nav>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
