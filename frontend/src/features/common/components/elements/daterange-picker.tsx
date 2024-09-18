import { FC } from 'react'
import { DateRangePickerProps } from '@mtes-mct/monitor-ui/fields/DateRangePicker'
import { DateRangePicker as MonitorUIDateRangePicker } from '@mtes-mct/monitor-ui'
import { DateRange } from '@mtes-mct/monitor-ui/types/definitions'
import { postprocessDateFromPicker, preprocessDateForPicker } from '@common/components/elements/date-picker.tsx'

/**
 * An overlay to deal with correct timezones on MonitorUI's DateRangePicker
 * Their component retransforms dates so we need to re-add the timezone difference
 * @param props DateRangePickerProps
 */
const DateRangePicker: FC<DateRangePickerProps> = (props: DateRangePickerProps) => {
  const startDate = props.defaultValue && preprocessDateForPicker(new Date(props.defaultValue[0]))
  const endDate = props.defaultValue && preprocessDateForPicker(new Date(props.defaultValue[1]))

  const handleChange = (range?: DateRange) => {
    if (range) {
      const correctedRange = [
        range[0] ? postprocessDateFromPicker(range[0]) : undefined,
        range[1] ? postprocessDateFromPicker(range[1]) : undefined
      ] as DateRange
      if (props.onChange) {
        props.onChange(correctedRange)
      }
    }
  }

  return <MonitorUIDateRangePicker {...props} defaultValue={[startDate, endDate]} onChange={handleChange} />
}

export default DateRangePicker
