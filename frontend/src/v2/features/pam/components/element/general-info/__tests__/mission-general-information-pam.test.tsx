import { render, screen } from '../../../../../../../test-utils.tsx'
import MissionGeneralInformationPam from '../mission-general-information-pam.tsx'

describe('MissionGeneralInformationPam Component', () => {
  it('renders', () => {
    render(<MissionGeneralInformationPam missionId={1} />)
    expect(screen.queryByTestId('Informations générales')).not.toBeInTheDocument()
  })
})
