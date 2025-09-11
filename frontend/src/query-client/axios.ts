import AuthToken from '@features/auth/utils/token'
import axios from 'axios'
import { triggerGlobalLogout } from '../v2/features/auth/hooks/use-auth.tsx'

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
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

axiosInstance.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 403) {
      triggerGlobalLogout() // 🚀 clean + navigate
    }
    return Promise.reject(error)
  }
)

export default axiosInstance
