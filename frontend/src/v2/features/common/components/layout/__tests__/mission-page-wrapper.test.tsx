import { render, screen } from '../../../../../../test-utils'
import MissionPageWrapper from '../mission-page-wrapper'

describe('MissionPageWrapper', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <MissionPageWrapper
        missionFooter={<>Footer</>}
        missionHeader={<>Header</>}
        missionAction={<>Action</>}
        missionTimeLine={<>Timeline</>}
        missionGeneralInformations={<>Général informations</>}
      />
    )
    expect(wrapper).toMatchSnapshot()
  })

  it('should show loading message', () => {
    render(<MissionPageWrapper isLoading={true} missionFooter={<>Footer</>} missionHeader={<>Header</>} />)
    expect(screen.getByText('Chargement...')).toBeInTheDocument()
  })

  it('should show error message', () => {
    render(
      <MissionPageWrapper
        missionFooter={<>Footer</>}
        missionHeader={<>Header</>}
        error={{ message: 'My beautiful error' }}
      />
    )
    expect(screen.getByText('My beautiful error')).toBeInTheDocument()
  })
})
