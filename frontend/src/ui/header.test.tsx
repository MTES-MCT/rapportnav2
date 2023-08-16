import { render, fireEvent, screen } from '../test-utils'
import '@testing-library/jest-dom/extend-expect'
import Header from './header'
import useAuth from '../auth/use-auth'

jest.mock('../auth/use-auth') // Mock the useAuth hook

describe('Header', () => {
  it('should render a logout button when user is authenticated', () => {
    // Mock the useAuth hook to return isAuthenticated as true
    ;(useAuth as jest.Mock).mockReturnValue({
      isAuthenticated: true,
      logout: jest.fn()
    })

    render(<Header />)
    const logoutButton = screen.getByText('Logout')
    expect(logoutButton).toBeInTheDocument()
  })

  it('should call the logout function when the logout button is clicked', () => {
    // Mock the useAuth hook to return isAuthenticated as true
    ;(useAuth as jest.Mock).mockReturnValue({
      isAuthenticated: true,
      logout: jest.fn()
    })

    render(<Header />)
    const logoutButton = screen.getByText('Logout')

    fireEvent.click(logoutButton)

    // You can use the mocked implementation of the logout function to verify if it was called
    expect(useAuth().logout).toHaveBeenCalled()
  })

  it('should not render the logout button when user is not authenticated', () => {
    // Mock the useAuth hook to return isAuthenticated as false
    ;(useAuth as jest.Mock).mockReturnValue({
      isAuthenticated: false,
      logout: jest.fn()
    })

    render(<Header />)
    const logoutButton = screen.queryByText('Logout')
    expect(logoutButton).not.toBeInTheDocument()
  })
})
