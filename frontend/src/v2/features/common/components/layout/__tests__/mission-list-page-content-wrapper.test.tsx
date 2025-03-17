import { render } from '../../../../../../test-utils'
import MissionListPageContentWrapper from '../mission-list-page-content-wrapper'

describe('MissionListPageContentWrapper', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <MissionListPageContentWrapper
        title="my title"
        loading={false}
        hasMissions={false}
        list={<>My lists</>}
        filters={<>Title</>}
        actions={<>Actions</>}
      />
    )
    expect(wrapper).toMatchSnapshot()
  })
})
