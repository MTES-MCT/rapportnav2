import { render, screen, waitFor } from '../../../../../../test-utils'
import userEvent from '@testing-library/user-event'
import LoginForm from '../login-form'
import { describe, it, expect } from 'vitest'

describe('LoginForm', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<LoginForm />)
    expect(wrapper).toMatchSnapshot()
  })

  it('does not surface validation errors on blur/typing — only after a submit attempt', async () => {
    render(<LoginForm />)

    // Type an invalid email and blur both fields, WITHOUT submitting.
    await userEvent.type(screen.getByLabelText(/Email/), 'invalid-email')
    await userEvent.tab()
    await userEvent.type(screen.getByLabelText('Mot de passe'), 'secret')
    await userEvent.tab()

    expect(screen.queryByText("L'adresse email n'est pas une adresse valide")).not.toBeInTheDocument()

    // The error only appears once the form is submitted.
    await userEvent.click(screen.getByText('Se connecter'))
    await waitFor(() => {
      expect(screen.getByText("L'adresse email n'est pas une adresse valide")).toBeInTheDocument()
    })
  })

  it('shows required-field errors and marks the inputs invalid after submitting an empty form', async () => {
    render(<LoginForm />)

    await userEvent.click(screen.getByText('Se connecter'))

    expect(await screen.findByText("L'adresse email est requise")).toBeInTheDocument()
    expect(screen.getByText('Le mot de passe est requis')).toBeInTheDocument()
    expect(screen.getByLabelText(/Email/)).toHaveAttribute('aria-invalid', 'true')
    expect(screen.getByLabelText('Mot de passe')).toHaveAttribute('aria-invalid', 'true')
  })

  it('toggles password visibility with the "Afficher" checkbox', async () => {
    render(<LoginForm />)

    const password = screen.getByLabelText('Mot de passe')
    expect(password).toHaveAttribute('type', 'password')

    await userEvent.click(screen.getByLabelText('Afficher le mot de passe'))
    expect(password).toHaveAttribute('type', 'text')

    await userEvent.click(screen.getByLabelText('Afficher le mot de passe'))
    expect(password).toHaveAttribute('type', 'password')
  })
})
