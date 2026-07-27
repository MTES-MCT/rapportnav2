import { describe, expect, it } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import { StyledFormikTextInput } from '../formik-text-input.tsx'

describe('FormikTextInput', () => {
  it('renders a text input element', () => {
    render(<StyledFormikTextInput name="note" label="Note" />, { formikValues: { note: undefined } })
    expect(screen.getByRole('textbox')).toBeInTheDocument()
  })

  it('renders the initial value from formik context', () => {
    render(<StyledFormikTextInput name="note" label="Note" />, { formikValues: { note: 'hello' } })
    expect(screen.getByRole('textbox')).toHaveValue('hello')
  })

  it('renders the label when provided', () => {
    render(<StyledFormikTextInput name="note" label="My Label" />, { formikValues: { note: undefined } })
    expect(screen.getByText('My Label')).toBeInTheDocument()
  })
})
