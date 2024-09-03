import { ApolloLink } from '@apollo/client'
import { convertLocalToUTCDates } from '@common/utils/dates-for-machines.ts'

// Middleware for mutations (converting local dates to UTC)
const mutationDateConvertedLink = new ApolloLink((operation, forward) => {
  if (operation.variables) {
    // auto-magically finds date fields and convert them from local french into UTC
    operation.variables = convertLocalToUTCDates(operation.variables)
  }
  return forward(operation)
})

export default mutationDateConvertedLink
