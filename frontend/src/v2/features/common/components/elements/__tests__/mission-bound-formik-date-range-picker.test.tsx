import { describe, expect, it, vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import MissionBoundFormikDateRangePicker from '../mission-bound-formik-date-range-picker'
import * as useMissionDatesModule from '../../../hooks/use-mission-dates'

describe('MissionBoundFormikDateRangePicker', () => {
  it('should render with mission dates', () => {
    vi.spyOn(useMissionDatesModule, 'useMissionDates').mockReturnValue({
      startDateTimeUtc: '2024-01-01T00:00:00Z',
      endDateTimeUtc: '2024-01-31T23:59:59Z'
    })

    const { container } = render(<MissionBoundFormikDateRangePicker name="dates" isLight={true} missionId="123" />)
    expect(container).toBeDefined()
  })

  it('should not crash when end date is undefined', () => {
    vi.spyOn(useMissionDatesModule, 'useMissionDates').mockReturnValue({
      startDateTimeUtc: '2024-01-01T00:00:00Z',
      endDateTimeUtc: undefined
    })

    const { container } = render(<MissionBoundFormikDateRangePicker name="dates" isLight={true} missionId="123" />)
    expect(container).toBeDefined()
  })
})
