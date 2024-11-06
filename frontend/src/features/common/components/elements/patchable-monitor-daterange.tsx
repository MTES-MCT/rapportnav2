import { FC } from 'react'
import DateRangePicker from '@common/components/elements/dates/daterange-picker.tsx'
import { DateRange } from '@mtes-mct/monitor-ui/types/definitions'
import useMissionDates from '@features/pam/mission/hooks/use-mission-dates.tsx'
import { useParams } from 'react-router-dom'

type PatchableMonitorDateRangeProps = {
  onChange: (startDateTimeUtc: string, endDateTimeUtc: string) => void
  startDateTimeUtc?: string
  endDateTimeUtc?: string
  label: string
  isLight?: boolean
  dateValidation?: boolean
}
const PatchableMonitorDateRange: FC<PatchableMonitorDateRangeProps> = ({
  onChange,
  startDateTimeUtc,
  endDateTimeUtc,
  label,
  isLight,
  dateValidation
}) => {
  const { missionId } = useParams()
  const missionDates = useMissionDates(missionId)

  const onChangeDates = (value?: DateRange) => {
    if (value) {
      const newStartDateTimeUtc = value[0]
      const newEndDateTimeUtc = value[1]
      onChange(newStartDateTimeUtc, newEndDateTimeUtc)
    }
  }

  return (
    <DateRangePicker
      selectedRange={[startDateTimeUtc, endDateTimeUtc]}
      allowedRange={dateValidation ? missionDates : undefined}
      label={label}
      withTime={true}
      isCompact={true}
      onChange={async (nextValue?: DateRange) => {
        onChangeDates(nextValue)
      }}
      isLight={isLight}
    />
  )
}

export default PatchableMonitorDateRange
