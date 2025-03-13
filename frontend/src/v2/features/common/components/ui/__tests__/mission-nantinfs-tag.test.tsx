import { render } from '../../../../../../test-utils'
import MissionNatinfTag from '../mission-natinfs-tag'

describe('MissionNatinfTag', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<MissionNatinfTag natinfs={['nantinf 1', 'nantinf 2']} />)
    expect(wrapper).toMatchSnapshot()
  })
})
