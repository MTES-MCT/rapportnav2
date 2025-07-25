import { render } from '../../../../../../test-utils'
import PageSectionWrapper from '../page-section-wrapper'

describe('PageSectionWrapper', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <PageSectionWrapper sectionBody={<>My Section Body</>} sectionHeader={<>My Section Header</>} />
    )
    expect(wrapper).toMatchSnapshot()
  })
})
