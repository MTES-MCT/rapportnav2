import { render } from '../../../../../../test-utils'
import TextByCnsp from '../text-by-cnsp'

describe('TextByCnsp', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<TextByCnsp />)
    expect(wrapper).toMatchSnapshot()
  })
})
