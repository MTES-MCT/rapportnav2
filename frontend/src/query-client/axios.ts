import AuthToken from '@features/auth/utils/token'
import axios from 'axios'
import { triggerGlobalLogout } from '../v2/features/auth/hooks/use-auth.tsx'

const axiosInstance = axios.create({
  baseURL: '/api/v2/', //TODO: import.meta.env.REACT_APP_BASE_URL,
  // Send the ProConnect session cookie (JSESSIONID) on every request. The legacy
  // JWT path is unaffected (it relies on the Authorization header set below).
  withCredentials: true,
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
  error => Promise.reject(error instanceof Error ? error : new Error(error))
)

axiosInstance.interceptors.response.use(
  response => response,
  error => {
    // 403 (legacy entry point) and 401 (ProConnect entry point / expired session)
    // both mean "no longer authenticated" -> clean up and send back to login.
    if (error.response?.status === 403 || error.response?.status === 401) {
      triggerGlobalLogout() // 🚀 clean + navigate
    }

    // Extract meaningful info from RFC 7807 ProblemDetail response
    const problemDetail = error.response?.data
    if (problemDetail?.detail) {
      error.message = problemDetail.detail
    }

    // handle 400s
    if (parseInt(error.response?.status, 10) === 400 && problemDetail?.data?.fieldErrors?.length) {
      const fieldMessages = problemDetail.data.fieldErrors
        .map((err: { field: string; message: string }) => err.message)
        .join(', ')
      error.message = `${problemDetail.detail}: ${fieldMessages}`
    }

    // Attach problem detail info to error for downstream handling
    error.status = error.response?.status
    error.problemDetail = problemDetail

    return Promise.reject(error instanceof Error ? error : new Error(error))
  }
)

export default axiosInstance
