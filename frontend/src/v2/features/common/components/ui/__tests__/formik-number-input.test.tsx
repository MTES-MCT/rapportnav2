import { describe, expect, it } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import { FormikNumberInput } from '../formik-number-input.tsx'

describe('FormikNumberInput', () => {
  it('renders an input element', () => {
    render(<FormikNumberInput name="count" label="Count" />, { formikValues: { count: undefined } })
    expect(screen.getByRole('spinbutton')).toBeInTheDocument()
  })

  it('renders with placeholder "0"', () => {
    render(<FormikNumberInput name="count" label="Count" />, { formikValues: { count: undefined } })
    expect(screen.getByPlaceholderText('0')).toBeInTheDocument()
  })

  it('renders the initial value from formik context', () => {
    render(<FormikNumberInput name="count" label="Count" />, { formikValues: { count: 42 } })
    expect(screen.getByRole('spinbutton')).toHaveValue(42)
  })

  it('renders the label when provided', () => {
    render(<FormikNumberInput name="count" label="My Label" />, { formikValues: { count: undefined } })
    expect(screen.getByText('My Label')).toBeInTheDocument()
  })
})
