import { FC } from 'react'
import DateRangePicker from '@common/components/elements/dates/daterange-picker.tsx'
import { DateRange } from '@mtes-mct/monitor-ui/types/definitions'

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
  isLight
}) => {
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
      label={label}
      withTime={true}
      isCompact={true}
      isRequired={true}
      onChange={async (nextValue?: DateRange) => {
        onChangeDates(nextValue)
      }}
      isLight={isLight}
    />
  )
}

export default PatchableMonitorDateRange
