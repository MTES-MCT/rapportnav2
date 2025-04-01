import { ErrorResponse, onError } from '@apollo/client/link/error'
import AuthToken from '@features/auth/utils/token.ts'

const authToken = new AuthToken()

const errorLink = onError(({ operation, forward }: ErrorResponse) => {
  const { response } = operation.getContext()

  if ([401, 403, 404, 405].indexOf(response?.status) !== -1) {
    authToken.remove()
    window.location.replace('/login')
  }

  return forward(operation)
})

export default errorLink
