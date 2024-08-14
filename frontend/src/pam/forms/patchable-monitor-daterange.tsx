import { FC } from 'react'
import { DateRangePicker } from '@mtes-mct/monitor-ui'

type PatchableMonitorDateRangeProps = {
  onChange: (startDateTimeUtc: string, endDateTimeUtc: string) => void
  startDateTimeUtc?: string
  endDateTimeUtc?: string
  label: string
  isLight?: boolean
}
const PatchableMonitorDateRange: FC<PatchableMonitorDateRangeProps> = ({
  onChange,
  startDateTimeUtc,
  endDateTimeUtc,
  label,
  isLight
}) => {
  const onChangeDates = (value: any) => {
    const newStartDateTimeUtc = value[0].toISOString()
    const newEndDateTimeUtc = value[1].toISOString()
    debugger
    onChange(newStartDateTimeUtc, newEndDateTimeUtc)
  }

  return (
    <DateRangePicker
      defaultValue={[startDateTimeUtc ?? new Date(), endDateTimeUtc ?? new Date()]}
      label={label}
      withTime={true}
      isCompact={true}
      onChange={async (nextValue?: [Date, Date] | [string, string]) => {
        onChangeDates(nextValue)
      }}
      isLight={isLight}
    />
  )
}

export default PatchableMonitorDateRange
