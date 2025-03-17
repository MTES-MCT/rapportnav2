import { Formik } from 'formik'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { StyledFormikToggle } from '../formik-styled-toogle'

const handleSubmit = vi.fn()
describe('StyledFormikToggle', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <Formik initialValues={{ toogle: false }} onSubmit={handleSubmit}>
        <StyledFormikToggle name="toogle" label="this is a style formik" />
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })
})
