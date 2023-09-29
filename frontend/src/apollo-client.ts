import { ApolloClient, InMemoryCache, createHttpLink } from '@apollo/client'
import AuthToken from './auth/token'
import { setContext } from '@apollo/client/link/context'
import { persistCache, LocalStorageWrapper } from 'apollo3-cache-persist'

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

export const apolloCache = new InMemoryCache({
  // typePolicies: {
  //   Mission: {
  //     merge: true
  //   },
  //   Action: {
  //     merge: true
  //   }
  // }
})

await persistCache({
  cache: apolloCache,
  storage: new LocalStorageWrapper(window.localStorage)
})

const client = new ApolloClient({
  cache: apolloCache,
  link: authLink.concat(httpLink),
  credentials: 'include',
  connectToDevTools: true
})

export default client
