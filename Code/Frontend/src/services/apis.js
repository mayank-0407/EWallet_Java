import { getCookie, logOut, setCookie } from "./cookieService";
import axios from "axios";
import { BASE_URL } from "../utils/constants";
import { getWithToken } from "./operations/axiosGetRequest";

export const isLogin = async () => {
  const token = getCookie("token");

  if (token) {
    try {
      const res = await axios.get(
        `${BASE_URL}api/auth/validate?token=${token}`
      );
      if (res.status === 200) {
        return true;
      }
      logOut();
      return false;
    } catch (error) {
      logOut();
      return false;
      console.error("Error validating token:", error);
    }
  }
  logOut();
  return false;
};

export const getTokenData = async () => {
  const token = getCookie("token");

  if (token) {
    try {
      const res = await getWithToken(`api/auth/extract/data?token=${token}`);
      if (res.status === 200) {
        return res.data;
      }
    } catch (error) {
      console.error("Error while Getting token Data:", error);
    }
  }
  return null;
};

export const getUserDetailsFromToken = async () => {
  const phoneNumber = await getTokenData();
  if (phoneNumber) {
    try {
      const res = await getWithToken(`api/auth/get/userdetails/${phoneNumber}`);
      if (res.status === 200) {
        return res;
      }
    } catch (error) {
      console.error("Error while Getting token Data:", error);
    }
  }
};
