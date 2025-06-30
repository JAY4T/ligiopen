import axios from 'axios';

const SPORTSDB = axios.create({
  baseURL: `https://www.thesportsdb.com/api/v1/json/${process.env.REACT_APP_SPORTSDB_API_KEY}`,
});

export default SPORTSDB;