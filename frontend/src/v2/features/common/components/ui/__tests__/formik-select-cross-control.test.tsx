import { Formik } from 'formik'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { CrossControl } from '../../../types/crossed-control-type'
import { FormikSelectCrossControl } from '../formik-select-crossed-control'

const handleSubmit = vi.fn()
describe('FormikSearchVessel', () => {
  it('should match the snapshot', () => {
    const crossControls: CrossControl[] = [
      {
        id: '1dd960df-5c47-4eaf-ae25-37024589aed1',
        serviceId: 6
      }
    ]
    const wrapper = render(
      <Formik initialValues={{ vesselSize: '' }} onSubmit={handleSubmit}>
        <FormikSelectCrossControl
          name="crossControl.id"
          crossControls={crossControls ?? []}
          label="Rechercher la cible d’un contrôle en cours*"
        />
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })
})
