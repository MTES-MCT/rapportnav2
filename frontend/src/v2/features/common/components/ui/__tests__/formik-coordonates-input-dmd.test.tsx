import { Formik } from 'formik'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { FormikCoordinateInputDMD } from '../formik-coordonates-input-dmd'

const handleSubmit = vi.fn()

describe('FormikCoordinateInputDMD', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <Formik initialValues={{ coords: [] }} onSubmit={handleSubmit}>
        <FormikCoordinateInputDMD name={'coords'} label="my coords" />
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })
})
