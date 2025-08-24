import { useState, useEffect } from "react";
import { isLogin } from "../../services/apis";
import { useNavigate } from "react-router-dom";
import { logOut } from "../../services/cookieService";

const useAuth = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const checkIfLoggedIn = async () => {
      try {
        const loggedIn = await isLogin();
        setIsLoggedIn(loggedIn);
      } catch (e) {
        setIsLoggedIn(false);
        logOut();
        navigate("/login");
      }
    };

    checkIfLoggedIn();
  },[]);

  return isLoggedIn;
};

export default useAuth;
