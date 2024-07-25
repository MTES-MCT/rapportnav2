import { vi } from 'vitest'
import { render, screen } from '../../test-utils.tsx'
import MissionDatetime from './mission-datetime.tsx'
import { MissionStatusEnum } from '../../types/mission-types.ts'
import { Mission } from '../../types/mission-types.ts'

const patchMissionMock = vi.fn()

vi.mock('./use-patch-mission-env', () => ({
  default: () => [patchMissionMock, { error: undefined }]
}))

const mockMission = {
  id: 1,
  startDateTimeUtc: Date.parse('2022-02-15T04:50:09Z'),
  endDateTimeUtc: Date.parse('2023-03-27T05:55:09Z'),
  status: MissionStatusEnum.IN_PROGRESS
} as unknown as Mission

describe('Mission Datetime start/end', () => {
  beforeEach(() => {
    patchMissionMock.mockReset()
  })
  afterEach(() => {
    vi.clearAllMocks()
  })

  it('should render the dates', () => {
    render(<MissionDatetime mission={mockMission} />)
    expect(patchMissionMock).not.toHaveBeenCalled()
    expect(screen.getByDisplayValue('2022-02-15 ~ 2023-03-27')).toBeInTheDocument()
  })

  // TODO find a way to trigger change on the DateTimePicker
  // it('should call update ', () => {
  //   const wrapper = render(<MissionDatetime mission={mockMission} />)
  //   const element = wrapper.getByDisplayValue('2022-02-15 ~ 2023-03-27')
  //   fireEvent.change(element, {
  //     target: { value: '2022-02-16 ~ 2023-03-27' }
  //   })
  //   fireEvent.blur(element)
  //   expect(patchMissionMock).toHaveBeenCalled()
  // })
})
