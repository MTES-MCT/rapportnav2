import { render } from '../../../../../../test-utils'
import MissionListPageWrapper from '../mission-list-page-wrapper'

describe('MissionListPageWrapper', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <MissionListPageWrapper
        footer={<>Footer</>}
        header={<>Header</>}
        sidebar={<>SideBar</>}
        children={<>Main Page</>}
      />
    )
    expect(wrapper).toMatchSnapshot()
  })
})
