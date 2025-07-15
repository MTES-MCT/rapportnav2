import { vi } from 'vitest'
import { fireEvent, render } from '../../../../../../test-utils'
import DialogQuestion from '../dialog-question'

const onSubmit = vi.fn()
describe('DialogQuestion', () => {
  beforeEach(() => {
    onSubmit.mockClear()
  })
  it('should match the snapshot', () => {
    const wrapper = render(
      <DialogQuestion
        type="danger"
        title="Suppression de la mission"
        question="Voulez vous vraiment supprimer cette mission?"
        onSubmit={onSubmit}
      />
    )
    expect(wrapper).toMatchSnapshot()
  })

  it('should trigger on submit with true', () => {
    const wrapper = render(<DialogQuestion type="danger" title="" question="" onSubmit={onSubmit} />)
    const confirmButton = wrapper.getByTestId('dialog-question-confirm-button')
    fireEvent.click(confirmButton)
    expect(onSubmit).toHaveBeenCalledTimes(1)
    expect(onSubmit).toHaveBeenCalledWith(true)
  })

  it('should trigger on submit with false', () => {
    const wrapper = render(<DialogQuestion type="danger" title="" question="" onSubmit={onSubmit} />)
    const confirmButton = wrapper.getByTestId('dialog-question-cancel-button')
    fireEvent.click(confirmButton)
    expect(onSubmit).toHaveBeenCalledTimes(1)
    expect(onSubmit).toHaveBeenCalledWith(false)
  })
})
