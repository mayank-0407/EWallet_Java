import cookie from "js-cookie";
import axios from "axios";
import { BASE_URL } from "../utils/constants";

export const setCookie = (key, value) => {
  // cookie.set(key, value, { expires: 1 });
  sessionStorage.setItem(key, value);
};

export const removeCookie = (key) => {
  // cookie.remove(key);
  sessionStorage.removeItem(key);
};

export const getCookie = (key) => {
  // return cookie.get(key);
  return sessionStorage.getItem(key);
};

export const setAuthentication = (token) => {
  setCookie("token", token);
};

export const logOut = () => {
  removeCookie("token");
};

export const isLogin = async () => {
  const token = getCookie("token");

  if (token) {
    try {
      const res = await axios.post(
        `${BASE_URL}/api/auth/validate?token=${token}`
      );
      if (res.status == 200) return true;
      logOut();
    } catch (error) {
      logOut();
      console.error("Error validating token:", error);
      return false;
    }
  }
  return false;
};
