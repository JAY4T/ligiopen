import axios from 'axios';

const API = axios.create({
  baseURL: 'https://8381-41-81-186-94.ngrok-free.app/api/',
});

export default API;