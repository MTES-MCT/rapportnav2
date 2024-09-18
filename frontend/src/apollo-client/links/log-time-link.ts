import { ApolloLink } from '@apollo/client'

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

export default logTimeLink
