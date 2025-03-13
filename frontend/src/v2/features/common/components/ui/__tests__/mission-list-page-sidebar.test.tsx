import { Icon } from '@mtes-mct/monitor-ui'
import { render } from '../../../../../../test-utils'
import MissionListPageSidebar from '../mission-list-page-sidebar'

describe('MissionListPageSidebar', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <MissionListPageSidebar
        items={[
          {
            key: 'mission',
            url: 'mission',
            icon: Icon.MissionAction
          }
        ]}
      />
    )
    expect(wrapper).toMatchSnapshot()
  })
})
