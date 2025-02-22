import { render } from '../../../../../../test-utils'
import IconVesselSailingLeisure from '../icon-vessel-sailing-leisure'

describe('IconVesselSailingLeisure', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<IconVesselSailingLeisure />)
    expect(wrapper).toMatchSnapshot()
  })
})
