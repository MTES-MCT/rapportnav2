import { ApolloClient, InMemoryCache, createHttpLink } from '@apollo/client'
import AuthToken from './auth/token'
import { setContext } from '@apollo/client/link/context'

const httpLink = createHttpLink({
  uri: 'http://localhost:8080/graphql'
})

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

export const apolloCache = new InMemoryCache({})

const client = new ApolloClient({
  cache: apolloCache,
  link: authLink.concat(httpLink),
  credentials: 'include',
  connectToDevTools: true
})

export default client
