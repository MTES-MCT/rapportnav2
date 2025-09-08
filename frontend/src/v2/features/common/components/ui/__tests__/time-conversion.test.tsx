import * as dateFnsTz from 'date-fns-tz'
import { afterEach, beforeEach, describe, it, vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import TimeConversion from '../time-conversion.tsx'

// Mock useDate hook
vi.mock('@/hooks/useDate', () => ({
  useDate: () => ({
    formatTime: (date: Date) => date.toISOString().split('T')[1].substring(0, 5) // HH:mm from ISO
  })
}))

describe('TimeConversion', () => {
  let originalTimeZone: string

  beforeEach(() => {
    originalTimeZone = Intl.DateTimeFormat().resolvedOptions().timeZone
    vi.useFakeTimers()
    vi.setSystemTime(new Date('2025-06-18T14:30:00Z')) // UTC 14:30
  })

  afterEach(() => {
    vi.useRealTimers()
  })

  it('renders correct UTC and local time', () => {
    render(<TimeConversion />)

    // Assert UTC time
    expect(screen.getByText(/UTC: 14:30/)).toBeInTheDocument()

    // Assert local time
    const localTime = dateFnsTz.formatInTimeZone(new Date('2025-06-18T14:30:00Z'), originalTimeZone, 'HH:mm')
    expect(screen.getByText(new RegExp(`Heure locale: ${localTime}`))).toBeInTheDocument()
  })
})
