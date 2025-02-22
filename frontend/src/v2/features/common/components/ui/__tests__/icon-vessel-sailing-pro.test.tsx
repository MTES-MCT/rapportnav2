import { render } from '../../../../../../test-utils'
import IconVesselSailingPro from '../icon-vessel-sailing-pro'

describe('IconVesselSailingPro', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<IconVesselSailingPro />)
    expect(wrapper).toMatchSnapshot()
  })
})
