import { FC, useEffect, useState } from 'react'
import { DatePickerProps as MonitorUIDatePickerProps } from '@mtes-mct/monitor-ui/fields/DatePicker'
import { DatePicker as MonitorUIDatePicker } from '@mtes-mct/monitor-ui'
import {
  validate,
  preprocessDateForPicker,
  postprocessDateFromPicker
} from '@common/components/elements/dates/utils.ts'

type DatePickerProps = MonitorUIDatePickerProps & {
  allowedRange: [Date | undefined, Date | undefined]
}

/**
 * An overlay to deal with correct timezones on MonitorUI's DatePicker
 * Their component retransforms dates so we need to re-add the timezone difference
 * @param props DatePickerProps
 */
const DatePicker: FC<DatePickerProps> = ({ allowedRange, ...props }: DatePickerProps) => {
  const [value, setValue] = useState<Date | undefined>(undefined)
  const [error, setError] = useState<string | undefined>(undefined)

  useEffect(() => {
    const newDate = props.defaultValue ? new Date(props.defaultValue) : undefined
    setValue(preprocessDateForPicker(newDate))
    if (allowedRange) {
      const validation = validate(allowedRange, newDate)
      if (!validation?.ok) {
        setError(validation?.message)
      }
    }
  }, [props.defaultValue, allowedRange])

  const handleChange = (date?: Date) => {
    const correctedDate = date ? postprocessDateFromPicker(date) : undefined
    const validation = validate(allowedRange, date)
    if (validation?.ok) {
      setError(undefined)
    } else {
      setError(validation?.message)
    }
    if (props.onChange) {
      props.onChange(correctedDate)
    }
  }

  return <MonitorUIDatePicker {...props} defaultValue={value} onChange={handleChange} error={error || props.error} />
}

export default DatePicker
