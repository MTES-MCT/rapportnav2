import { vi } from 'vitest'
import { render, screen } from '../../../test-utils.tsx'
import ActionEnvDateRange from './action-env-daterange.tsx'

const patchActionMock = vi.fn()
vi.mock('./use-patch-action-env', () => ({
  default: () => [patchActionMock, { error: undefined }]
}))

const props = () => ({
  missionId: '1',
  actionId: '1',
  startDateTimeUtc: Date.parse('2022-02-15T04:50:09Z'),
  endDateTimeUtc: Date.parse('2023-03-27T05:55:09Z')
})

describe('Mission Datetime start/end', () => {
  beforeEach(() => {
    patchActionMock.mockReset()
  })
  afterEach(() => {
    vi.clearAllMocks()
  })

  it('should render the dates', () => {
    render(<ActionEnvDateRange {...props()} />)
    expect(patchActionMock).not.toHaveBeenCalled()
    expect(screen.getByDisplayValue('2022-02-15 ~ 2023-03-27')).toBeInTheDocument()
  })

  // TODO find a way to trigger change on the DateTimePicker
  // it('should call update ', () => {
  //   const wrapper = render(<ActionEnvDateRange {...props()} />)
  //   const element = wrapper.getByDisplayValue('2022-02-15 ~ 2023-03-27')
  //   fireEvent.change(element, {
  //     target: { value: '2022-02-16 ~ 2023-03-27' }
  //   })
  //   fireEvent.blur(element)
  //   expect(patchActionMock).toHaveBeenCalled()
  // })
})
