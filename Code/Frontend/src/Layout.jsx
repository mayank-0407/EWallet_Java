import { Outlet, Navigate } from "react-router-dom";
import useAuth from "./utils/Hooks/useAuth.jsx";

const Layout = () => {
  const isLoggedIn = useAuth();

  if (isLoggedIn === null) {
    return <div>Loading...</div>;
  }

  return (
    <>
      {isLoggedIn ? <Outlet /> : <Navigate to="/login" />}
    </>
  );
};

export default Layout;
