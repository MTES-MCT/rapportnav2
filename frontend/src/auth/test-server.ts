import { rest } from 'msw'
import { setupServer } from 'msw/node'
import { LOGIN_ENDPOINT } from "./login.tsx";


export const loginSuccessHandler = [
  rest.post(LOGIN_ENDPOINT, (_req, res, ctx) => {
    return res(ctx.status(200), ctx.json({token: 'jwt'}))
  })
]
export const loginFailedHandler = rest.post(LOGIN_ENDPOINT, (_req, res, ctx) => {
  return res(ctx.status(400), ctx.json({message: 'Login Failed'}))
})

// This configures a request mocking server with the given request handlers.
export const loginServer = () => {
  const server = setupServer(...loginSuccessHandler)
  server.printHandlers()
  return server
}
