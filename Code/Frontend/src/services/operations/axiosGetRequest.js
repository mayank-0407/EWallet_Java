import axios from "axios";
import { getCookie } from "../cookieService";
import { BASE_URL } from "../../utils/constants";

export const getWithToken = async (url) => {
  const token = getCookie("token");

  try {
    const response = await axios.get(BASE_URL+url, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    return response;
  } catch (error) {
    console.error("Error in API call:", error);
    throw error;
  }
};
