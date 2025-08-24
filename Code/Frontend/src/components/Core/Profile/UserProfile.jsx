function EditProfile() {
    return (
      <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
        <div className="lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute bg-gray-300 rounded-lg shadow-lg z-10 flex flex-col">
          <Navbar />
          <div className="bg-white p-4 rounded-xl shadow-md ml-2 mr-2 mb-2 flex-grow overflow-y-auto"></div>
        </div>
      </div>
    );
  }
  export default EditProfile;