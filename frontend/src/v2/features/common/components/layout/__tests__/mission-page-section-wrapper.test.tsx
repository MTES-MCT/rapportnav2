import { render } from '../../../../../../test-utils'
import MissionPageSectionWrapper from '../mission-page-section-wrapper'

describe('MissionPageSectionWrapper', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <MissionPageSectionWrapper sectionBody={<>My Section Body</>} sectionHeader={<>My Section Header</>} />
    )
    expect(wrapper).toMatchSnapshot()
  })
})
