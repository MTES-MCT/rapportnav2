import AuthToken from '@features/auth/utils/token'
import axios from 'axios'

const axiosInstance = axios.create({
  baseURL: '/api/v2/', //TODO: import.meta.env.REACT_APP_BASE_URL,
  withCredentials: false,
  headers: {
    Accept: 'application/json',
    'Content-type': 'application/json'
  },
  timeout: 900000
})

axiosInstance.interceptors.request.use(
  config => {
    const token = new AuthToken().get()
    const auth = token ? `Bearer ${token}` : ''
    config.headers['Authorization'] = auth
    return config
  },
  error => Promise.reject(error)
)

export default axiosInstance
