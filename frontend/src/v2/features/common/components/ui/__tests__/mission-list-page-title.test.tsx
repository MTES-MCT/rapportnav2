import { render, screen } from '../../../../../../test-utils'
import MissionListPageTitle from '../mission-list-page-title'

describe('MissionListPageTitle', () => {
  it('should match the snapshot', () => {
    const user = {
      id: 2,
      firstName: 'My first name',
      lastName: 'My Last name',
      controlUnitId: 1244,
      serviceId: 3,
      serviceName: 'Jeanne-Barret',
      email: 'mybeautiful@email.com'
    }
    const wrapper = render(<MissionListPageTitle user={user} />)
    expect(wrapper).toMatchSnapshot()
  })

  it('should display name if no serviceName', () => {
    const user = {
      id: 2,
      firstName: 'My first name',
      lastName: 'My Last name',
      controlUnitId: 1244,
      email: 'mybeautiful@email.com'
    }
    render(<MissionListPageTitle user={user} />)
    expect(screen.queryByText('|')).not.toBeInTheDocument()
  })
})
