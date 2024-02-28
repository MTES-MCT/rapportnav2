import { ApolloClient, ApolloLink, createHttpLink, InMemoryCache } from '@apollo/client'
import { loadDevMessages, loadErrorMessages } from '@apollo/client/dev'
import AuthToken from './auth/token'
import { setContext } from '@apollo/client/link/context'
import { RetryLink } from "@apollo/client/link/retry";
import { ErrorResponse, onError } from "@apollo/client/link/error";

const authToken = new AuthToken()

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

const errorLink = onError(({graphQLErrors, networkError, operation, forward}: ErrorResponse) => {
  const {response} = operation.getContext()

  if ([401, 403].indexOf(response.status) !== -1) {
    authToken.remove()
    window.location.replace('/login')
  }

  return forward(operation);
});

export const apolloCache = new InMemoryCache({})

const client = new ApolloClient({
  cache: apolloCache,
  link: ApolloLink.from([
    authLink,
    errorLink,
    retryLink,
    httpLink,
  ]),
  credentials: 'include',
  connectToDevTools: true
})

export default client
