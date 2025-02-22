import { render } from '../../../../../../test-utils'
import IconVesselFishing from '../icon-vessel-fishing'

describe('IconVesselFishing', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<IconVesselFishing />)
    expect(wrapper).toMatchSnapshot()
  })
})
