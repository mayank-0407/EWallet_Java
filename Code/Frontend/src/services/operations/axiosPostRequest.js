import axios from "axios";
import { getCookie } from "../cookieService";
import { BASE_URL } from "../../utils/constants";

export const postWithToken = async (url, reqBody) => {
  const token = getCookie("token");

  try {
    const response = await axios.post(BASE_URL + url, reqBody, {
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
