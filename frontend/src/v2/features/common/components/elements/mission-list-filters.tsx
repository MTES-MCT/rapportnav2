import { Accent, Button, DateRangePicker, Icon, MultiSelect, Select } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { useDate } from '../../hooks/use-date.tsx'
import { useMissionType } from '../../hooks/use-mission-type.tsx'
import { CompletenessForStatsStatusEnum, MissionStatusEnum } from '../../types/mission-types.ts'
import { clearMissionListFilters, hasActiveMissionListFilters } from './mission-list-filter-utils.ts'

export enum DateMode {
  CURRENT_WEEK = 'CURRENT_WEEK',
  CURRENT_MONTH = 'CURRENT_MONTH',
  CURRENT_YEAR = 'CURRENT_YEAR',
  CUSTOM = 'CUSTOM'
}

export const DATE_MODE_LABELS: Record<DateMode, string> = {
  [DateMode.CURRENT_WEEK]: 'Semaine en cours',
  [DateMode.CURRENT_MONTH]: 'Mois en cours',
  [DateMode.CURRENT_YEAR]: 'Année en cours',
  [DateMode.CUSTOM]: 'Période spécifique'
}

const STATUS_OPTIONS = [
  { value: MissionStatusEnum.UPCOMING, label: 'À venir' },
  { value: MissionStatusEnum.IN_PROGRESS, label: 'En cours' },
  { value: MissionStatusEnum.ENDED, label: 'Terminée' },
  { value: MissionStatusEnum.UNAVAILABLE, label: 'Indisponible' }
]

const COMPLETENESS_OPTIONS = [
  { value: CompletenessForStatsStatusEnum.VALID, label: 'Complété' },
  { value: CompletenessForStatsStatusEnum.INCOMPLETE, label: 'À compléter' },
  { value: CompletenessForStatsStatusEnum.INVALID, label: 'Données invalides' }
]

interface MissionListFiltersProps {
  searchParams: URLSearchParams
  onChange: (params: URLSearchParams) => void
  dateModeOptions: { value: DateMode; label: string }[]
  // whether to show the "Type de rapport" filter (hidden on PAM, which has a single report type)
  showReportTypeFilter?: boolean
}

/**
 * Full filter bar for the mission list. Row 1 holds the date-range dropdown plus the status / completeness /
 * report-type multi-selects. Row 2 (conditional) holds the date-only custom-period picker and a reset button.
 *
 * The date filter is optional: with no `dateMode` param the list is unbounded (newest missions overall) and no
 * date params are sent. Everything is stored in the URL search params: the resolved `startDateTimeUtc` /
 * `endDateTimeUtc` (sent to the backend), a `dateMode` marker (drives the dropdown + custom row), and the three
 * enum filters as repeated params (e.g. `statuses=ENDED&statuses=IN_PROGRESS`, matching the backend binding).
 */
const MissionListFilters: FC<MissionListFiltersProps> = ({
  searchParams,
  onChange,
  dateModeOptions,
  showReportTypeFilter = true
}) => {
  const { reportTypeOptions } = useMissionType()
  const { getTodayWeekRange, getTodayMonthRange, getTodayYearRange } = useDate()

  // null = no date filter ("Toutes les dates")
  const dateMode = searchParams.get('dateMode') as DateMode | null

  const resolveRange = (mode: DateMode) => {
    switch (mode) {
      case DateMode.CURRENT_WEEK:
        return getTodayWeekRange()
      case DateMode.CURRENT_YEAR:
        return getTodayYearRange()
      case DateMode.CURRENT_MONTH:
      default:
        return getTodayMonthRange()
    }
  }

  const hasActiveFilters = hasActiveMissionListFilters(searchParams)

  const handleMultiSelectChange = (key: string) => (values?: string[]) => {
    const next = new URLSearchParams(searchParams)
    next.delete(key)
    values?.forEach(value => next.append(key, value))
    onChange(next)
  }

  const handleDateModeChange = (mode?: DateMode) => {
    const next = new URLSearchParams(searchParams)
    if (!mode) {
      // cleared → back to no date filter
      next.delete('dateMode')
      next.delete('startDateTimeUtc')
      next.delete('endDateTimeUtc')
    } else if (mode === DateMode.CUSTOM) {
      // reveal the picker; keep any existing dates as its starting value, don't bound the query until both picked
      next.set('dateMode', mode)
    } else {
      const range = resolveRange(mode)
      next.set('dateMode', mode)
      next.set('startDateTimeUtc', range.startDateTimeUtc)
      next.set('endDateTimeUtc', range.endDateTimeUtc)
    }
    onChange(next)
  }

  const handleCustomRangeChange = (range?: [string, string] | [Date, Date] | undefined) => {
    if (!range || !range[0] || !range[1]) return
    const next = new URLSearchParams(searchParams)
    next.set('startDateTimeUtc', String(range[0]))
    next.set('endDateTimeUtc', String(range[1]))
    onChange(next)
  }

  const handleReset = () => {
    onChange(clearMissionListFilters(searchParams))
  }

  const customValue: [string, string] | undefined = (() => {
    const start = searchParams.get('startDateTimeUtc')
    const end = searchParams.get('endDateTimeUtc')
    return start && end ? [start, end] : undefined
  })()

  const showSecondRow = dateMode === DateMode.CUSTOM || hasActiveFilters

  return (
    <Stack direction="column" spacing="1rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" wrap style={{ width: '100%' }}>
          <Stack.Item style={{ minWidth: '220px', maxWidth: '300px', flex: 1 }}>
            <Select
              name="dateMode"
              label="Période"
              placeholder=""
              isCleanable={true}
              options={dateModeOptions}
              value={dateMode ?? undefined}
              onChange={handleDateModeChange}
            />
          </Stack.Item>
          <Stack.Item style={{ minWidth: '220px', maxWidth: '300px', flex: 1 }}>
            <MultiSelect
              name="statuses"
              label="Statut de la mission"
              placeholder=""
              options={STATUS_OPTIONS}
              value={searchParams.getAll('statuses')}
              onChange={handleMultiSelectChange('statuses')}
            />
          </Stack.Item>
          <Stack.Item style={{ minWidth: '220px', maxWidth: '300px', flex: 1 }}>
            <MultiSelect
              name="completenessStatuses"
              label="État des données"
              placeholder=""
              options={COMPLETENESS_OPTIONS}
              value={searchParams.getAll('completenessStatuses')}
              onChange={handleMultiSelectChange('completenessStatuses')}
            />
          </Stack.Item>
          {showReportTypeFilter && (
            <Stack.Item style={{ minWidth: '220px', maxWidth: '300px', flex: 1 }}>
              <MultiSelect
                name="reportTypes"
                label="Type de rapport"
                placeholder=""
                options={reportTypeOptions}
                value={searchParams.getAll('reportTypes')}
                onChange={handleMultiSelectChange('reportTypes')}
              />
            </Stack.Item>
          )}
          {hasActiveFilters && (
            <Stack.Item>
              <Button accent={Accent.TERTIARY} Icon={Icon.Reset} onClick={handleReset}>
                Réinitialiser les filtres
              </Button>
            </Stack.Item>
          )}
        </Stack>
      </Stack.Item>

      {showSecondRow && (
        <Stack.Item style={{ width: '100%' }}>
          <Stack
            direction="row"
            spacing="1rem"
            alignItems="flex-end"
            justifyContent="space-between"
            style={{ width: '100%' }}
          >
            <Stack.Item style={{ minWidth: '280px' }}>
              {dateMode === DateMode.CUSTOM && (
                <DateRangePicker
                  name="customPeriod"
                  label="Période spécifique"
                  isStringDate
                  withTime={false}
                  defaultValue={customValue}
                  onChange={handleCustomRangeChange}
                />
              )}
            </Stack.Item>
          </Stack>
        </Stack.Item>
      )}
    </Stack>
  )
}

export default MissionListFilters
