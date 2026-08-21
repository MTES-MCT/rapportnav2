import { vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import MissionListFilters, { DateMode, DATE_MODE_LABELS } from '../mission-list-filters.tsx'

const DATE_MODE_OPTIONS = [DateMode.CURRENT_MONTH, DateMode.CURRENT_YEAR, DateMode.CUSTOM].map(value => ({
  value,
  label: DATE_MODE_LABELS[value]
}))

describe('MissionListFilters', () => {
  test('renders the period dropdown and the three filter labels', () => {
    render(
      <MissionListFilters searchParams={new URLSearchParams()} onChange={vi.fn()} dateModeOptions={DATE_MODE_OPTIONS} />
    )
    expect(screen.getByText('Période')).toBeInTheDocument()
    expect(screen.getByText('Statut de la mission')).toBeInTheDocument()
    expect(screen.getByText('Type de rapport')).toBeInTheDocument()
  })

  test('hides the report-type filter when showReportTypeFilter is false', () => {
    render(
      <MissionListFilters
        searchParams={new URLSearchParams()}
        onChange={vi.fn()}
        dateModeOptions={DATE_MODE_OPTIONS}
        showReportTypeFilter={false}
      />
    )
    expect(screen.getByText('Statut de la mission')).toBeInTheDocument()
    expect(screen.queryByText('Type de rapport')).not.toBeInTheDocument()
  })

  test('hides the reset button when no filter is active', () => {
    render(
      <MissionListFilters searchParams={new URLSearchParams()} onChange={vi.fn()} dateModeOptions={DATE_MODE_OPTIONS} />
    )
    expect(screen.queryByText('Réinitialiser les filtres')).not.toBeInTheDocument()
  })

  test('shows the reset button when a filter is active and clears it on click', () => {
    const onChange = vi.fn()
    render(
      <MissionListFilters
        searchParams={new URLSearchParams('statuses=ENDED&dateMode=CURRENT_MONTH')}
        onChange={onChange}
        dateModeOptions={DATE_MODE_OPTIONS}
      />
    )

    const reset = screen.getByText('Réinitialiser les filtres')
    expect(reset).toBeInTheDocument()

    fireEvent.click(reset)

    expect(onChange).toHaveBeenCalledTimes(1)
    const next = onChange.mock.calls[0][0] as URLSearchParams
    expect(next.getAll('statuses')).toEqual([])
    expect(next.get('dateMode')).toBeNull()
  })
})
