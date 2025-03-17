import { render, screen } from '../../../../../../test-utils'
import MissionNatinfTagFish from '../mission-natinfs-tag-fish'

describe('MissionNatinfTag', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<MissionNatinfTagFish natinf={12345} />)
    expect(wrapper).toMatchSnapshot()
  })

  it('should display on infraction if not', () => {
    render(<MissionNatinfTagFish />)
    expect(screen.getByText('Sans infraction')).toBeInTheDocument()
  })
})
