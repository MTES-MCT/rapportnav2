import { ApolloLink } from '@apollo/client'
import { convertUTCToLocalISO } from '@common/utils/dates-for-machines.ts'

// Middleware for queries (converting UTC dates to local time)
const queryDateConverterLink = new ApolloLink((operation, forward) => {
  return forward(operation).map(response => {
    if (response.data) {
      // auto-magically finds date fields and convert them from UTC into local french
      response.data = convertUTCToLocalISO(response.data)
    }
    return response
  })
})

export default queryDateConverterLink
