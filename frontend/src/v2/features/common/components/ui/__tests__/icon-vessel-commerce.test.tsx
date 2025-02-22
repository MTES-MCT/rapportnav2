import { render } from '../../../../../../test-utils'
import IconVesselCommerce from '../icon-vessel-commerce'

describe('IconVesselCommerce', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<IconVesselCommerce />)
    expect(wrapper).toMatchSnapshot()
  })
})
