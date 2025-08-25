import { FormikCheckbox, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { StyledFormikToggle } from '../../../common/components/ui/formik-styled-toogle'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const MissionActionItemNavSurveillance: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      onChange={onChange}
      children={<StyledFormikToggle name="isWithinDepartment" label="Surveillance dans le département" />}
      footer={
        <Stack.Item style={{ width: '100%' }}>
          <Divider style={{ backgroundColor: THEME.color.charcoal, marginBottom: 4 }} />
          <FormikCheckbox isLight name="hasDivingDuringOperation" label="Plongée au cours de l'opération" />
        </Stack.Item>
      }
      data-testid={'action-nav-surveillance-form'}
    />
  )
}

export default MissionActionItemNavSurveillance
