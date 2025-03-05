import { render } from '../../../../../../../test-utils.tsx'
import MissionGeneralInformationFormPam from '../mission-general-information-form-pam.tsx'

describe('MissionGeneralInformationFormPam Component', () => {
  it('renders', () => {
    render(<MissionGeneralInformationFormPam generalInfo2={{}} onChange={vi.fn()} />)
  })
})
