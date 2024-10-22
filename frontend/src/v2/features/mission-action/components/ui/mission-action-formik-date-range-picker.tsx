import { FormikDateRangePicker, FormikDateRangePickerWithDateDateProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

export const MissionActionFormikDateRangePicker = styled(
  (props: Omit<FormikDateRangePickerWithDateDateProps, 'label'>) => (
    <FormikDateRangePicker
      isLight={true}
      withTime={true}
      isCompact={true}
      isRequired={true}
      label="Date et heure de début et de fin"
      {...props}
    />
  )
)({})
