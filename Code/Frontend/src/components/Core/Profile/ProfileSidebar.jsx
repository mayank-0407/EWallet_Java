import React, { useState, useEffect } from "react";
import { AiOutlineMenu, AiOutlineClose } from "react-icons/ai";
import { ImProfile } from "react-icons/im";
import { MdLogout, MdPassword, MdHelp } from "react-icons/md";
import { FaUserCircle } from "react-icons/fa";

const ProfileSidebar = () => {
  const [nav, setNav] = useState(false);
  const [message, setMessage] = useState("Welcome");

  useEffect(() => {
    const timer = setTimeout(() => {
      setMessage("");
    }, 2000);

    return () => clearTimeout(timer);
  }, []);
  const menuItems = [
    { icon: <ImProfile size={25} className="mr-4" />, text: "Edit Profile" },
    {
      icon: <MdPassword size={25} className="mr-4" />,
      text: "Change Password",
    },
    { icon: <MdHelp size={25} className="mr-4" />, text: "Help" },
    { icon: <MdLogout size={25} className="mr-4" />, text: "Logout" },
  ];

  return (
    <div className="mx-auto flex justify-between items-center">
      {/* Overlay */}
      {nav && (
        <div className="bg-black/80 fixed w-fit h-fit z-10 top-0 left-0"></div>
      )}

      {/* Right Side Drawer */}
      <div
        className={`fixed top-0 right-0 w-[30%] h-fit bg-white z-20 duration-300 ${
          nav ? "translate-x-0" : "translate-x-full"
        }`}
      >
        <AiOutlineClose
          onClick={() => setNav(false)}
          size={30}
          className="absolute left-4 top-4 cursor-pointer"
        />
        <nav>
          <ul className="flex flex-col p-4 text-gray-800">
            {menuItems.map(({ icon, text }, index) => (
              <div key={index} className="py-4">
                <li className="text-xl flex cursor-pointer w-[80%] rounded-full mx-auto p-2 hover:text-white hover:bg-black">
                  {icon} {text}
                </li>
              </div>
            ))}
          </ul>
        </nav>
      </div>
    </div>
  );
};

export default ProfileSidebar;
