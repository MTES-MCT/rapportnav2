import { render } from '../../../../../../test-utils'
import TextByCacem from '../text-by-cacem'

describe('TextByCacem', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<TextByCacem />)
    expect(wrapper).toMatchSnapshot()
  })
})
