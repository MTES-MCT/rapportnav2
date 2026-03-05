import { describe, expect, it, vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import MissionBoundFormikDatePicker from '../mission-bound-formik-date-picker'

vi.mock('../../../hooks/use-mission-dates.tsx', () => ({
  useMissionDates: () => ({
    startDateTimeUtc: '2024-01-01T00:00:00Z',
    endDateTimeUtc: '2024-01-31T23:59:59Z'
  })
}))

describe('MissionBoundFormikDatePicker', () => {
  it('should render with mission dates', () => {
    const { container } = render(<MissionBoundFormikDatePicker name="date" label="Date" isLight={true} missionId="123" />)
    expect(container).toBeDefined()
  })
})
