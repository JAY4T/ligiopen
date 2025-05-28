import axios from 'axios';

const API = axios.create({
  baseURL: 'https://2442-41-81-187-107.ngrok-free.app/api/',
});

export default API;