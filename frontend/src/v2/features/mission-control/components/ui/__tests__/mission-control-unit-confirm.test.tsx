import { vi } from 'vitest'
import { fireEvent, render } from '../../../../../../test-utils'
import MissionControlUnitConfirm from '../mission-control-unit-confirm'

const handleSubmit = vi.fn()
describe('MissionControlUnitConfirm', () => {
  beforeEach(() => {
    handleSubmit.mockClear()
  })
  it('should match the snapshot', () => {
    const wrapper = render(<MissionControlUnitConfirm onSubmit={handleSubmit} />)
    expect(wrapper).toMatchSnapshot()
  })

  it('should trigger on submit with true', () => {
    const wrapper = render(<MissionControlUnitConfirm onSubmit={handleSubmit} />)
    const yesButton = wrapper.getByTestId('yes-no-toogle-yes-button')
    fireEvent.click(yesButton)
    expect(handleSubmit).toHaveBeenCalledTimes(1)
    expect(handleSubmit).toHaveBeenCalledWith(true)
  })

  it('should trigger on submit with false', () => {
    const wrapper = render(<MissionControlUnitConfirm onSubmit={handleSubmit} />)
    const noButton = wrapper.getByTestId('yes-no-toogle-no-button')
    fireEvent.click(noButton)
    expect(handleSubmit).toHaveBeenCalledTimes(1)
    expect(handleSubmit).toHaveBeenCalledWith(false)
  })
})
