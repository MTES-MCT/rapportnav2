import { ApolloClient, ApolloLink, createHttpLink, InMemoryCache } from '@apollo/client'
import { loadDevMessages, loadErrorMessages } from '@apollo/client/dev'
import { setContext } from '@apollo/client/link/context'
import { ErrorResponse, onError } from '@apollo/client/link/error'
import { removeTypenameFromVariables } from '@apollo/client/link/remove-typename'
import { RetryLink } from '@apollo/client/link/retry'
import AuthToken from './auth/token'

const removeTypenameLink = removeTypenameFromVariables()

const authToken = new AuthToken()

if (import.meta.env.DEV) {
  // Adds messages only in a dev environment
  loadDevMessages()
  loadErrorMessages()
}

const httpLink = createHttpLink({
  uri: '/graphql'
})

const retryLink = new RetryLink()

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

const errorLink = onError(({ operation, forward }: ErrorResponse) => {
  const { response } = operation.getContext()

  if ([401, 403, 404].indexOf(response.status) !== -1) {
    authToken.remove()
    window.location.replace('/login')
  }

  return forward(operation)
})

/**
 * ApolloLink to store when the last update to the cache was made
 */
const logTimeLink = new ApolloLink((operation, forward) => {
  return forward(operation).map(data => {
    const newDate = new Date().getTime().toString()
    localStorage.setItem('lastSyncTimestamp', newDate)
    window.dispatchEvent(
      new StorageEvent('storage', {
        key: 'lastSyncTimestamp',
        newValue: newDate
      })
    )
    return data
  })
})

export const apolloCache = new InMemoryCache({})

const client = new ApolloClient({
  cache: apolloCache,
  link: ApolloLink.from([removeTypenameLink, authLink, errorLink, logTimeLink, retryLink, httpLink]),
  credentials: 'include',
  connectToDevTools: true
})

export default client
