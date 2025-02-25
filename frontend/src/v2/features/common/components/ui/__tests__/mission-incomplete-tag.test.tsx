import { render } from '../../../../../../test-utils'
import MissionIncompleteControlTag from '../mission-incomplete-control-tag'

describe('MissionIncompleteTag', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<MissionIncompleteControlTag nbrIncompleteControl={3} />)
    expect(wrapper).toMatchSnapshot()
  })
})
