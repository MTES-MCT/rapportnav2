import { Formik } from 'formik'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { Vessel } from '../../../types/vessel-type'
import { FormikSearchVessel } from '../formik-search-vessel'

const handleSubmit = vi.fn()
describe('FormikSearchVessel', () => {
  it('should match the snapshot', () => {
    const vessels: Vessel[] = [
      {
        externalReferenceNumber: 'MFA32',
        vesselId: 1544146,
        vesselName: 'Ariete I'
      }
    ]
    const wrapper = render(
      <Formik initialValues={{ vesselSize: '' }} onSubmit={handleSubmit}>
        <FormikSearchVessel
          vessels={vessels}
          name="crossControl.vesselId"
          isLight={true}
          label="Nom du navire contrôlée"
        />
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })
})
