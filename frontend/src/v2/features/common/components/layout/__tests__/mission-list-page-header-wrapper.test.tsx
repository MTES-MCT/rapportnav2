import { render } from '../../../../../../test-utils'
import MissionListPageHeaderWrapper from '../mission-list-page-header-wrapper'

describe('MissionListPageHeaderWrapper', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<MissionListPageHeaderWrapper title={<>Title</>} actions={<>Actions</>} />)
    expect(wrapper).toMatchSnapshot()
  })
})
