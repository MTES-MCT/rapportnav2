import { FormikToggle, FormikToggleProps, Label } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import styled from 'styled-components'

export const MissionActionFormikToogleUnitShouldConfirm = styled((props: Omit<FormikToggleProps, 'label'>) => (
  <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
    <Stack.Item>
      <FormikToggle label="" size="sm" {...props} />
    </Stack.Item>
    <Stack.Item alignSelf="flex-end">
      <Label style={{ marginBottom: 0 }}>
        <b>Contrôle confirmé par l’unité</b>
      </Label>
    </Stack.Item>
  </Stack>
))({})
