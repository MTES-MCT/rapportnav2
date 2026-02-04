import { http, HttpResponse } from 'msw'
import { setupServer } from 'msw/node'
import { LOGIN_ENDPOINT } from '../login-page.tsx'

export const loginSuccessHandler = [
  http.post(LOGIN_ENDPOINT, () => {
    return HttpResponse.json({ token: 'jwt' }, { status: 200 })
  })
]
export const loginFailedHandler = http.post(LOGIN_ENDPOINT, (_req, res, ctx) => {
  return HttpResponse.json({ message: 'Login Failed' }, { status: 400 })
})

// This configures a request mocking server with the given request handlers.
export const loginServer = () => {
  const server = setupServer(...loginSuccessHandler)
  server.listHandlers()
  return server
}
