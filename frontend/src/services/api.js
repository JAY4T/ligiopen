import axios from 'axios';

const API = axios.create({
  baseURL: 'https://b667-41-81-171-250.ngrok-free.app/api/',
});

export default API;