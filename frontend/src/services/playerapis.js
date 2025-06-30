import axios from "axios";

const PLAYER_API = axios.create({
  baseURL: "http://167.71.35.181:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

export default PLAYER_API;