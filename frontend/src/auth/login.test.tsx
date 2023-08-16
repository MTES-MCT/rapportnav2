import React from 'react'
import { render, screen, waitFor } from '../test-utils'
import userEvent from '@testing-library/user-event'
import Login from './login'
import { server, login_failed_handler } from './test-server'

describe('Login Component', () => {
  // Establish API mocking before all tests.
  beforeAll(() => server.listen({}))

  // Reset any request handlers that we may add during the tests,
  // so they don't affect other tests.
  afterEach(() => server.resetHandlers())

  // Clean up after the tests are finished.
  afterAll(() => server.close())

  it('should display validation error for invalid email address', async () => {
    render(<Login />)

    // Fill in the email and password fields
    await userEvent.type(screen.getByLabelText('Email'), 'invalid-email')
    await userEvent.type(screen.getByLabelText('Mot de passe'), 'password')

    // Click on the submit button inside the act function
    await userEvent.click(screen.getByText('Se connecter'))

    await waitFor(() => {
      // Wait for the validation message to appear
      expect(screen.getByText("L'adresse email n'est pas une adresse valide")).toBeInTheDocument()
    })
  })

  it('should display validation error for empty email and password fields', async () => {
    render(<Login />)

    // Click on the submit button without filling in any fields
    await userEvent.click(screen.getByText('Se connecter'))

    // Wait for the validation messages to appear
    await waitFor(() => {
      expect(screen.getByText("L'adresse email est requise")).toBeInTheDocument()
      expect(screen.getByText('Le mot de passe est requis')).toBeInTheDocument()
    })
  })

  it('should set token in local storage and go to root path on successful form submission', async () => {
    // set router to a different route to correctly test the redirection - avoiding false positives
    window.history.pushState({}, '', '/login')

    render(<Login />)

    const submitButton = screen.getByText('Se connecter')

    // Act
    await userEvent.type(screen.getByLabelText('Email'), 'test@example.com')
    await userEvent.type(screen.getByLabelText('Mot de passe'), 'password')
    await userEvent.click(submitButton)

    // Assert
    await waitFor(() => {
      expect(window.location.pathname).toEqual('/')
    })
    await waitFor(() => {
      expect(localStorage.getItem('jwt')).toEqual('jwt')
    })
  })

  it('should display error message when API call fails', async () => {
    // Mock httpClient to return a rejected promise
    server.use(login_failed_handler)

    render(<Login />)

    // Fill in the email and password fields
    await userEvent.type(screen.getByLabelText('Email'), 'test@example.com')
    await userEvent.type(screen.getByLabelText('Mot de passe'), 'password')

    // Click on the submit button
    await userEvent.click(screen.getByText('Se connecter'))

    // Wait for the error message to appear
    await waitFor(() => {
      expect(screen.getByText('La connexion a échoué. Veuillez vérifier vos identifiants.')).toBeInTheDocument()
    })
  })
})
