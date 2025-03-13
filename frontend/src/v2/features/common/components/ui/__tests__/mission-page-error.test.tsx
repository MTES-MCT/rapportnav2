import { render } from '../../../../../../test-utils'
import MissionPageError from '../mission-page-error'

describe('MissionPageError', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<MissionPageError error={{ message: 'this is and error' }} />)
    expect(wrapper).toMatchSnapshot()
  })
})
