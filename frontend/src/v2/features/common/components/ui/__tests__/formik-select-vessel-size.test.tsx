import { Formik } from 'formik'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { FormikSelectVesselSize } from '../formik-select-vessel-size'

const handleSubmit = vi.fn()
describe('FormikSelectVesselSize', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <Formik initialValues={{ vesselSize: '' }} onSubmit={handleSubmit}>
        <FormikSelectVesselSize name="vesselSize" label="select vessel Size" />
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })
})
