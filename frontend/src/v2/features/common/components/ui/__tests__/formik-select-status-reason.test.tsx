import { Formik } from 'formik'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import FormikSelectStatusReason from '../formik-select-status-reason'

const handleSubmit = vi.fn()
describe('FormikSelectStatusReason', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <Formik initialValues={{ reason: '' }} onSubmit={handleSubmit}>
        <FormikSelectStatusReason name="reason" label="Motif" />
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })
})
