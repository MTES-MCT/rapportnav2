import { ApolloClient, ApolloLink, createHttpLink, InMemoryCache } from '@apollo/client'
import { loadDevMessages, loadErrorMessages } from '@apollo/client/dev'
import { removeTypenameFromVariables } from '@apollo/client/link/remove-typename'
import { RetryLink } from '@apollo/client/link/retry'
import mutationDateConvertedLink from './links/mutation-date-converter-link.ts'
import queryDateConverterLink from './links/query-date-converter-link.ts'
import logTimeLink from './links/log-time-link.ts'
import authLink from './links/auth-link.ts'
import errorLink from './links/error-link.ts'

const removeTypenameLink = removeTypenameFromVariables()

if (import.meta.env.DEV) {
  // Adds messages only in a dev environment
  loadDevMessages()
  loadErrorMessages()
}

const httpLink = createHttpLink({
  uri: '/graphql'
})

const retryLink = new RetryLink()

export const apolloCache = new InMemoryCache({})

const client = new ApolloClient({
  cache: apolloCache,
  link: ApolloLink.from([
    removeTypenameLink,
    mutationDateConvertedLink,
    queryDateConverterLink,
    authLink,
    errorLink,
    logTimeLink,
    retryLink,
    httpLink
  ]),
  credentials: 'include',
  connectToDevTools: true
})

export default client
