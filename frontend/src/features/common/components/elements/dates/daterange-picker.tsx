import { FC, useState } from 'react'
import { DateRangePickerProps } from '@mtes-mct/monitor-ui/fields/DateRangePicker'
import { DateRangePicker as MonitorUIDateRangePicker } from '@mtes-mct/monitor-ui'
import { DateRangePicker as MonitorUIDateRangePickeokr } from 'rsuite'
import { DateRange } from '@mtes-mct/monitor-ui/types/definitions'
import {
  validateRange,
  postprocessDateFromPicker,
  preprocessDateForPicker
} from '@common/components/elements/dates/utils.ts'

/**
 * An overlay to deal with correct timezones on MonitorUI's DateRangePicker
 * Their component retransforms dates so we need to re-add the timezone difference
 * @param props DateRangePickerProps
 */
const DateRangePicker: FC<DateRangePickerProps> = (props: DateRangePickerProps) => {
  const startDate = props.defaultValue && preprocessDateForPicker(new Date(props.defaultValue[0]))
  const endDate = props.defaultValue && preprocessDateForPicker(new Date(props.defaultValue[1]))

  const { error, setError } = useState<string | undefined>()

  const handleChange = (range?: DateRange) => {
    if (range) {
      const correctedRange = [
        range[0] ? postprocessDateFromPicker(range[0]) : undefined,
        range[1] ? postprocessDateFromPicker(range[1]) : undefined
      ] as DateRange
      const validation = validateRange()
      if (validation?.ok) {
        if (props.onChange) {
          props.onChange(correctedRange)
        }
      } else {
        setError(validation?.message)
      }
    }
  }

  return (
    <MonitorUIDateRangePicker {...props} defaultValue={[startDate, endDate]} onChange={handleChange} error={error} />
  )
}

export default DateRangePicker
