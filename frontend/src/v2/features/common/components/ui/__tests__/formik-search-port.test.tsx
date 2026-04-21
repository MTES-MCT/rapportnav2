import { Formik } from 'formik'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { Port } from '../../../types/port-type'
import { FormikSearchPort } from '../formik-search-port'

const handleSubmit = vi.fn()
describe('FormikSearchPort', () => {
  it('should match the snapshot', () => {
    const ports: Port[] = [
      {
        locode: 'FRLEH',
        name: 'Le Havre',
        countryCode: 'FR'
      }
    ]
    const wrapper = render(
      <Formik initialValues={{ portLocode: '' }} onSubmit={handleSubmit}>
        <FormikSearchPort ports={ports} name="portLocode" isLight={true} label="Port" />
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })
})
