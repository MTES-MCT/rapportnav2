import { render } from '../../../../../../test-utils'
import IconVesselPassenger from '../icon-vessel-passenger'

describe('IconVesselPassenger', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<IconVesselPassenger />)
    expect(wrapper).toMatchSnapshot()
  })
})
