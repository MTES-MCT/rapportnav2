import { render } from '../../../../../../test-utils'
import IconVesselSchool from '../icon-vessel-school'

describe('IconVesselSchool', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<IconVesselSchool />)
    expect(wrapper).toMatchSnapshot()
  })
})
