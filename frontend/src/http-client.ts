import ky, { Options } from 'ky'
import AuthToken from './auth/token'
import { KyInstance } from 'ky/distribution/types/ky'

const configWithAuth = (): Options => {
  const authToken = new AuthToken()
  return {
    prefixUrl: window.location.origin + '/api',
    headers: { 'Content-Type': 'application/json' },
    hooks: {
      beforeRequest: [
        options => {
          console.log('REQUEST TO ', options.url)
          const token = authToken.get()

          if (token) {
            options.headers.set('Authorization', `Bearer ${token}`)
          }
        }
      ],
      afterResponse: []
    }
  }
}

export const httpClient: KyInstance = ky.create(configWithAuth())
