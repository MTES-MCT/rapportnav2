import { http, HttpResponse } from 'msw'
import { setupServer } from 'msw/node'
import { LOGIN_ENDPOINT } from "./login.tsx";


export const loginSuccessHandler = [
  http.post(LOGIN_ENDPOINT, () => {
    // return new HttpResponse(null, {
    //   status: 202,
    // }).json({ token: 'jwt' })
    return HttpResponse.json({token: 'jwt'}, {status: 200})
    // return res(ctx.status(200), ctx.json({token: 'jwt'}))
  })
]
export const loginFailedHandler = http.post(LOGIN_ENDPOINT, (_req, res, ctx) => {
  // return HttpResponse.error().json({ message: 'Login Failed' })
  return HttpResponse.json({message: 'Login Failed'}, {status: 400})
  // return res(ctx.status(400), ctx.json({message: 'Login Failed'}))
})

// This configures a request mocking server with the given request handlers.
export const loginServer = () => {
  const server = setupServer(...loginSuccessHandler)
  server.listHandlers()
  return server
}
