import { Field, FieldProps, Formik } from 'formik'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { FormikSearchPort } from '../formik-search-port'

vi.mock('../../../services/use-port-service', () => ({
  usePortListQuery: vi.fn().mockReturnValue({ data: undefined })
}))

const handleSubmit = vi.fn()
describe('FormikSearchPort', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <Formik initialValues={{ portLocode: '' }} onSubmit={handleSubmit}>
        <Field name="portLocode">
          {(field: FieldProps<string>) => (
            <FormikSearchPort name="portLocode" isLight={true} label="Port" fieldFormik={field} />
          )}
        </Field>
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })
})
