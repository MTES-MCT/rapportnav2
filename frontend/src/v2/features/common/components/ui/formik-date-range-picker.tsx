import {
  FormikDateRangePicker as MonitorUIFormikDateRangePicker,
  FormikDateRangePickerWithDateDateProps
} from '@mtes-mct/monitor-ui'

export type FormikDateRangePickerProps = Omit<FormikDateRangePickerWithDateDateProps, 'name' | 'label'> & {
  name?: string
  label?: string
}

export const FormikDateRangePicker = ({ name, label, ...props }: FormikDateRangePickerProps) => (
  <MonitorUIFormikDateRangePicker
    name={name ?? 'dates'}
    label={label || 'Date et heure de début et de fin (utc)'}
    hasSingleCalendar={true}
    withTime={true}
    isCompact={true}
    isRequired={true}
    {...props}
  />
)
