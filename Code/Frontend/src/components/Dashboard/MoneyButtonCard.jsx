// import React, {useEffect } from "react";
// import axios from "axios";
import { IoIosSend } from "react-icons/io";
import { FaPlus } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

const MoneyButtonCard = () => {
  const navigate=useNavigate();
  return (
    <div className="bg-white text-black rounded-l-md rounded-r-2xl p-6 shadow-lg mb-2 mt-0 mr-2 ml-1 w-2/5">
      <div className="flex flex-col w-full text-3xl">
        <button className="bg-[#909bff] hover:bg-gray-500 text-white py-3 px-4 mb-2 rounded-xl" onClick={()=>navigate('/sendmoney')}>
          <div className="flex flex-row justify-center items-center">
            <IoIosSend /> <p className="pl-4 cursor-pointer">Send Money</p>
          </div>
        </button>
        <button className="bg-[#6edbe8c8] hover:bg-gray-500 text-white py-3 px-4 mt-2 rounded-xl" onClick={()=>navigate('/addmoneytowallet')}>
          <div className="flex flex-row justify-center items-center">
            <FaPlus/> <p className="pl-4 cursor-pointer">Add Money</p>
          </div>
        </button>
      </div>
    </div>
  );
};
export default MoneyButtonCard;
