import { rest } from 'msw'
import { setupServer } from 'msw/node'

const LOGIN_ENDPOINT = '/api/v1/auth/login'

export const success_handlers = [
  rest.post(LOGIN_ENDPOINT, (_req, res, ctx) => {
    return res(ctx.status(200), ctx.json({ token: 'jwt' }))
  })
]
export const login_failed_handler = rest.post(LOGIN_ENDPOINT, (_req, res, ctx) => {
  return res(ctx.status(400), ctx.json({ message: 'Login Failed' }))
})

// This configures a request mocking server with the given request handlers.
export const server = setupServer(...success_handlers)
