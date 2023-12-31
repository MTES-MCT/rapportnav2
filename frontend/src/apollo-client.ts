import { ApolloClient, ApolloLink, createHttpLink, InMemoryCache } from '@apollo/client'
import { loadDevMessages, loadErrorMessages } from '@apollo/client/dev'
import AuthToken from './auth/token'
import { setContext } from '@apollo/client/link/context'
import { RetryLink } from "@apollo/client/link/retry";


if (true) {
  // if (__DEV__) {
  // Adds messages only in a dev environment
  loadDevMessages()
  loadErrorMessages()
}

const httpLink = createHttpLink({
  uri: '/graphql'
})

const retryLink = new RetryLink();

const authLink = setContext((_, {headers}) => {
  const authToken = new AuthToken()
  const token = authToken.get()
  return {
    headers: {
      ...headers,
      'Content-Type': 'application/json',
      ...(token ? {authorization: `Bearer ${token}`} : {})
    }
  }
})

export const apolloCache = new InMemoryCache({})

const client = new ApolloClient({
  cache: apolloCache,
  link: ApolloLink.from([
    authLink,
    retryLink,
    httpLink,
  ]),
  credentials: 'include',
  connectToDevTools: true
})

export default client
