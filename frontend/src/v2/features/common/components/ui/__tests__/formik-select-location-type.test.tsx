import { ControlMethod } from '@common/types/control-types'
import { Formik } from 'formik'
import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import { FormikSelectLocationType } from '../formik-select-location-type'

const handleSubmit = vi.fn()

describe('FormikSelectLocationType', () => {
  it('should render with SEA control method and show all options', () => {
    const wrapper = render(
      <Formik initialValues={{ locationType: '' }} onSubmit={handleSubmit}>
        <FormikSelectLocationType name="locationType" label="Lieu de l'operation" controlMethod={ControlMethod.SEA} />
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })

  it('should render with LAND control method and show only PORT and COMMUNE options', () => {
    const wrapper = render(
      <Formik initialValues={{ locationType: '' }} onSubmit={handleSubmit}>
        <FormikSelectLocationType name="locationType" label="Lieu de l'operation" controlMethod={ControlMethod.LAND} />
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })

  it('should render without control method and default to SEA options', () => {
    const wrapper = render(
      <Formik initialValues={{ locationType: '' }} onSubmit={handleSubmit}>
        <FormikSelectLocationType name="locationType" label="Lieu de l'operation" />
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })
})
