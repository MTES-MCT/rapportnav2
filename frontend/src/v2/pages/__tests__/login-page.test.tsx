import { render, screen, waitFor } from '../../../test-utils.tsx'
import userEvent from '@testing-library/user-event'
import LoginPage from '../login-page.tsx'
import { loginFailedHandler, loginServer, loginSuccessHandler } from './test-server.ts'
import { afterAll, afterEach, beforeAll, describe, it, vi } from 'vitest'

const server = loginServer()

describe('Login Component', () => {
  // Establish API mocking before all tests.
  beforeAll(() => server.listen({ onUnhandledRequest: 'error' }))

  // Reset any request handlers that we may add during the tests,
  // so they don't affect other tests.
  afterEach(() => server.resetHandlers())

  // Clean up after the tests are finished.
  afterAll(() => server.close())

  it('should display validation error for invalid email address', async () => {
    render(<LoginPage />)

    // Fill in the email and password fields
    await userEvent.type(screen.getByLabelText(/Email/), 'invalid-email')
    await userEvent.type(screen.getByLabelText('Mot de passe'), 'password')

    // Click on the submit button inside the act function
    await userEvent.click(screen.getByText('Se connecter'))

    await waitFor(() => {
      // Wait for the validation message to appear
      expect(screen.getByText("L'adresse email n'est pas une adresse valide")).toBeInTheDocument()
    })
  })

  it('should set token in local storage and redirect to root path on successful form submission', async () => {
    server.use(loginSuccessHandler[0])
    // Login now triggers a full page reload (window.location.assign) to drop the
    // DSFR chunk/CSS. jsdom does not implement navigation and window.location is
    // non-configurable, so we swap in a stub (keeping origin, used by the fetch).
    const assignMock = vi.fn()
    const originalLocation = window.location
    const locationStub = new URL(originalLocation.href)
    ;(locationStub as unknown as { assign: typeof assignMock }).assign = assignMock
    Object.defineProperty(window, 'location', { configurable: true, value: locationStub })

    render(<LoginPage />)

    const submitButton = screen.getByText('Se connecter')

    // Act
    await userEvent.type(screen.getByLabelText(/Email/), 'test@example.com')
    await userEvent.type(screen.getByLabelText('Mot de passe'), 'password')
    await userEvent.click(submitButton)

    // Assert
    await waitFor(() => {
      expect(localStorage.getItem('jwt')).toEqual('jwt')
    })
    await waitFor(() => {
      expect(assignMock).toHaveBeenCalledWith('/')
    })

    Object.defineProperty(window, 'location', { configurable: true, value: originalLocation })
  })

  it('should display error message when API call fails', async () => {
    // Mock httpClient to return a rejected promise
    server.use(loginFailedHandler)

    render(<LoginPage />)

    // Fill in the email and password fields
    await userEvent.type(screen.getByLabelText(/Email/), 'test@example.com')
    await userEvent.type(screen.getByLabelText('Mot de passe'), 'password')

    // Click on the submit button
    await userEvent.click(screen.getByText('Se connecter'))

    // Wait for the error message to appear
    await waitFor(() => {
      expect(screen.getByText('La connexion a échoué. Veuillez vérifier vos identifiants.')).toBeInTheDocument()
    })
  })
})
