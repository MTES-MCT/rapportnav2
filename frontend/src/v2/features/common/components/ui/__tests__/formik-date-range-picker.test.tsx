import { Field, FieldProps, Formik } from 'formik'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { FormikDateRangePicker } from '../formik-date-range-picker'

const handleSubmit = vi.fn()
describe('FormikDateRangePicker', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <Formik initialValues={{ dates: [] }} onSubmit={handleSubmit}>
        <Field name="dates">
          {(field: FieldProps<Date[]>) => (
            <FormikDateRangePicker label="" name="dates" isLight={true} fieldFormik={field} />
          )}
        </Field>
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })
})
