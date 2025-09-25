import { FormikTextInput } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericControl from './mission-action-item-generic-control'

const MissionActionItemOtherControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  return (
    <MissionActionItemGenericControl
      action={action}
      onChange={onChange}
      withGeoCoords={true}
      data-testid={'action-control-other'}
    >
      <Stack.Item style={{ width: '100%' }}>
        <FormikTextInput
          isLight={true}
          isRequired={true}
          name="controlType"
          isErrorMessageHidden={true}
          label="Nature du contrôle / Type de contrôle"
        />
      </Stack.Item>
    </MissionActionItemGenericControl>
  )
}
export default MissionActionItemOtherControl
