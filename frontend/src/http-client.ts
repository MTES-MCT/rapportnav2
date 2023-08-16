import ky, { Options } from 'ky'
import AuthToken from './auth/token'
import { KyInstance } from 'ky/distribution/types/ky'

const basicConfig: Options = {
  headers: { 'Content-Type': 'application/json' }
}

export const httpClient: KyInstance = ky.create(basicConfig)

const configWithAuth = (): Options => {
  const authToken = new AuthToken()
  return {
    ...basicConfig,
    ...{
      hooks: {
        beforeRequest: [
          options => {
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
}

export const authenticatedHttpClient: KyInstance = httpClient.extend(configWithAuth())
