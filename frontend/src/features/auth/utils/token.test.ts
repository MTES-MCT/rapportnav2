import AuthToken from './token.ts'
import localStorageMock from '@common/utils/localstorage-mock.ts'

describe('AuthToken', () => {
  afterEach(() => {
    localStorageMock.clear()
  })

  describe('set', () => {
    it('should set the token in localStorage', () => {
      const authToken = new AuthToken()
      const token = 'your_jwt_token_here'

      authToken.set(token)
      expect(localStorage.getItem('jwt')).toBe(token)
    })
  })

  describe('get', () => {
    it('should return the token from localStorage', () => {
      const authToken = new AuthToken()
      const token = 'your_jwt_token_here'
      localStorage.setItem('jwt', token)

      const retrievedToken = authToken.get()
      expect(retrievedToken).toBe(token)
    })

    it('should return null if the token is not set', () => {
      const authToken = new AuthToken()

      const retrievedToken = authToken.get()
      expect(retrievedToken).toBeNull()
    })
  })

  describe('remove', () => {
    it('should remove the token from localStorage', () => {
      const authToken = new AuthToken()
      const token = 'your_jwt_token_here'
      localStorage.setItem('jwt', token)

      expect(localStorage.getItem('jwt')).toBe(token)

      authToken.remove()

      expect(localStorage.getItem('jwt')).toBeNull()
    })

    it('should not throw an error when token is not set', () => {
      const authToken = new AuthToken()

      expect(() => {
        authToken.remove()
      }).not.toThrow()
    })
  })
})
