import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import VesselName from '../vessel-name'

const handleSubmit = vi.fn()
describe('VesselName', () => {
  beforeEach(() => {
    handleSubmit.mockClear()
  })
  it('should match the snapshot', () => {
    const wrapper = render(<VesselName name="ultima navire" />)
    expect(wrapper).toMatchSnapshot()
  })
})
