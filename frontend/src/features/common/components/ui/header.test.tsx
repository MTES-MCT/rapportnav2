import { fireEvent, render, screen } from '../../../../test-utils.tsx'
import Header from './header.tsx'
import * as useAuthModule from '@features/auth/hooks/use-auth'
import { vi } from 'vitest'

const logoutMock = vi.fn()

describe('Header', () => {
  it('should render a logout button when user is authenticated', () => {
    // Mock the useAuth hook to return isAuthenticated as true
    vi.spyOn(useAuthModule, 'default').mockReturnValue({
      isAuthenticated: true,
      logout: vi.fn()
    })

    render(<Header />)
    const logoutButton = screen.getByText('Logout')
    expect(logoutButton).toBeInTheDocument()
  })

  it('should call the logout function when the logout button is clicked', () => {
    // Mock the useAuth hook to return isAuthenticated as true
    vi.spyOn(useAuthModule, 'default').mockReturnValue({
      isAuthenticated: true,
      logout: logoutMock
    })

    render(<Header />)
    const logoutButton = screen.getByText('Logout')

    fireEvent.click(logoutButton)

    // You can use the mocked implementation of the logout function to verify if it was called
    expect(logoutMock).toHaveBeenCalled()
  })

  it('should not render the logout button when user is not authenticated', () => {
    // Mock the useAuth hook to return isAuthenticated as false
    vi.spyOn(useAuthModule, 'default').mockReturnValue({
      isAuthenticated: false,
      logout: vi.fn()
    })

    render(<Header />)
    const logoutButton = screen.queryByText('Logout')
    expect(logoutButton).not.toBeInTheDocument()
  })
})
