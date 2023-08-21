import { render, fireEvent, screen } from '../test-utils'
import Header from './header'
import useAuth from '../auth/use-auth'
import { vi } from 'vitest'

vi.mock('../auth/use-auth', () => ({
  default: vi.fn()
}))

describe('Header', () => {
  it('should render a logout button when user is authenticated', () => {
    // Mock the useAuth hook to return isAuthenticated as true
    ;(useAuth as any).mockReturnValue({
      isAuthenticated: true,
      logout: vi.fn()
    })

    render(<Header />)
    const logoutButton = screen.getByText('Logout')
    expect(logoutButton).toBeInTheDocument()
  })

  it('should call the logout function when the logout button is clicked', () => {
    // Mock the useAuth hook to return isAuthenticated as true
    ;(useAuth as any).mockReturnValue({
      isAuthenticated: true,
      logout: vi.fn()
    })

    render(<Header />)
    const logoutButton = screen.getByText('Logout')

    fireEvent.click(logoutButton)

    // You can use the mocked implementation of the logout function to verify if it was called
    expect(useAuth().logout).toHaveBeenCalled()
  })

  it('should not render the logout button when user is not authenticated', () => {
    // Mock the useAuth hook to return isAuthenticated as false
    ;(useAuth as any).mockReturnValue({
      isAuthenticated: false,
      logout: vi.fn()
    })

    render(<Header />)
    const logoutButton = screen.queryByText('Logout')
    expect(logoutButton).not.toBeInTheDocument()
  })
})
