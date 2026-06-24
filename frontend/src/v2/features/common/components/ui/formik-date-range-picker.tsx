import {
  FormikDateRangePicker as MonitorUIFormikDateRangePicker,
  FormikDateRangePickerWithDateDateProps
} from '@mtes-mct/monitor-ui'

export type FormikDateRangePickerProps = Omit<FormikDateRangePickerWithDateDateProps, 'name' | 'label'> & {
  name?: string
  label?: string
  withTime?: boolean
  allowSameDate?: boolean
}

export const FormikDateRangePicker = ({
  name,
  label,
  withTime = true,
  allowSameDate = false,
  ...props
}: FormikDateRangePickerProps) => (
  <MonitorUIFormikDateRangePicker
    name={name ?? 'dates'}
    label={label || 'Date et heure de début et de fin (utc)'}
    hasSingleCalendar={true}
    withTime={withTime}
    isCompact={true}
    isRequired={true}
    {...props}
  />
)
