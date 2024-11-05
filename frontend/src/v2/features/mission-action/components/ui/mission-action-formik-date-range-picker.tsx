import { FormikDatePickerWithDateDateProps, FormikDateRangePicker, Label } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'
import { Stack } from 'rsuite'

export const MissionActionFormikDateRangePicker = styled((props: Omit<FormikDatePickerWithDateDateProps, 'label'>) => (
  <Stack direction={'column'}>
    <Stack.Item>
      <Label>Date et heure de début et de fin</Label>
    </Stack.Item>
    <Stack.Item>
      <Stack direction={'row'}>
        <Stack.Item>
          <FormikDateRangePicker isLight={true} withTime={true} isCompact={true} isRequired={true} {...props} />
        </Stack.Item>
        <Stack.Item>au</Stack.Item>
        <Stack.Item>
          <FormikDateRangePicker isLight={true} withTime={true} isCompact={true} isRequired={true} {...props} />
        </Stack.Item>
      </Stack>
    </Stack.Item>
    <Stack.Item></Stack.Item>
  </Stack>
))({})
