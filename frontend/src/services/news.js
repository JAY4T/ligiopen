import axios from "axios";

const NEWS_API = axios.create({
  baseURL: "http://localhost:1337/api"
});

export default NEWS_API;