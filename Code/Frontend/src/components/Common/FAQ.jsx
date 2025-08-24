import Navbar from "./Navbar";
import React, { useState } from "react";

function FAQ() {
  const faqs = [
    {
      question:
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
      answer:
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
    },
    {
      question:
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
      answer:
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
    },
    {
      question:
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
      answer:
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
    },
    {
      question:
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
      answer:
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
    },
    {
      question:
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
      answer:
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
    },
    {
      question:
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
      answer:
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
    },
  ];
  const [openIndex, setOpenIndex] = useState(null);

  const toggleFAQ = (index) => {
    setOpenIndex(openIndex === index ? null : index);
  };
  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute  bg-gray-300 rounded-lg shadow-lg z-10 flex flex-col">
        <Navbar />
        <div className="bg-white p-4 rounded-xl shadow-md ml-2 mr-2 mb-2 flex-grow overflow-y-auto">
          <div className="space-y-4">
            {faqs.map((faq, index) => (
              <div key={index} className="border rounded-lg">
                <button
                  className="w-full flex justify-between items-center p-4 text-left"
                  onClick={() => toggleFAQ(index)}
                >
                  <span className="font-medium">{faq.question}</span>
                  <svg
                    className={`w-5 h-5 transform transition-transform ${
                      openIndex === index ? "rotate-180" : ""
                    }`}
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth="2"
                      d="M19 9l-7 7-7-7"
                    />
                  </svg>
                </button>
                {openIndex === index && (
                  <div className="p-4 pt-0 flex justify-start items-start text-gray-600">
                    {faq.answer}
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
export default FAQ;
