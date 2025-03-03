import { render } from '../../../../../../../test-utils.tsx'
import MissionGeneralInformationPamBody from '../mission-general-information-pam-body.tsx'

describe('MissionGeneralInformationPamBody Component', () => {
  it('renders', () => {
    render(<MissionGeneralInformationPamBody missionId={1} generalInfos={undefined} />)
  })
})
