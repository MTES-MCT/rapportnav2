import { describe, expect, it } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import { FormikTextAreaInput } from '../formik-textarea-input.tsx'

describe('FormikTextAreaInput', () => {
  it('renders a textarea element', () => {
    render(<FormikTextAreaInput name="observations" label="Observations" />, {
      formikValues: { observations: undefined }
    })
    expect(screen.getByRole('textbox')).toBeInTheDocument()
  })

  it('renders the initial value from formik context', () => {
    render(<FormikTextAreaInput name="observations" label="Observations" />, {
      formikValues: { observations: 'some text' }
    })
    expect(screen.getByRole('textbox')).toHaveValue('some text')
  })

  it('renders the label when provided', () => {
    render(<FormikTextAreaInput name="observations" label="My Label" />, {
      formikValues: { observations: undefined }
    })
    expect(screen.getByText('My Label')).toBeInTheDocument()
  })
})
