import { FormikDatePicker, FormikDatePickerWithDateDateProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

export const MissionActionFormikDatePicker = styled((props: Omit<FormikDatePickerWithDateDateProps, 'label'>) => (
  <FormikDatePicker
    isLight={true}
    withTime={true}
    isCompact={true}
    isRequired={true}
    label="Date et heure de début"
    {...props}
  />
))({})
