import { act, renderHook, waitFor } from '../test-utils'
import useAuth from './use-auth' // Make sure to import the hook from the correct file

describe('useAuth hook', () => {
  it('should set isAuthenticated to true if token is present in localStorage', () => {
    window.localStorage.setItem('jwt', 'jwt')
    const {result} = renderHook(() => useAuth())
    expect(result.current.isAuthenticated).toBe(true)
  })

  it('should set isAuthenticated to false if token is not present in localStorage', () => {
    window.localStorage.setItem('jwt', '')
    const {result} = renderHook(() => useAuth())
    expect(result.current.isAuthenticated).toBe(false)
  })

  it('should update isAuthenticated to false and navigate to login page on logout', async () => {
    window.localStorage.setItem('jwt', 'jwt')

    const {result} = renderHook(() => useAuth())

    act(() => {
      result.current.logout()
    })

    expect(result.current.isAuthenticated).toBe(false)
    expect(window.localStorage.getItem('jwt')).toBeNull()

    await waitFor(() => {
      expect(window.location.pathname).toBe('/login')
    })
  })
})
