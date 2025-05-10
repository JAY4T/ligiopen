import axios from 'axios';

const API = axios.create({
  baseURL: 'https://bcb3-41-81-178-95.ngrok-free.app/api/',
});

export default API;