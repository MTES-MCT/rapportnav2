import { setContext } from '@apollo/client/link/context'
import AuthToken from '@features/auth/utils/token.ts'

const authLink = setContext((_, { headers }) => {
  const authToken = new AuthToken()
  const token = authToken.get()
  return {
    headers: {
      ...headers,
      'Content-Type': 'application/json',
      ...(token ? { authorization: `Bearer ${token}` } : {})
    }
  }
})

export default authLink
