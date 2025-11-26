import { FC } from 'react'
import { MissionAction } from '../../../common/types/mission-action'
import { CONTROL_NAUTICAL_LEISURE_SCHEMA } from '../../validation-schema/conrtol-nautical-leisure'
import MissionActionNauticalLeisureControlForm from '../ui/mission-action-control-nautical-leisure-form'
import MissionActionItemGenericControl from './mission-action-item-generic-control'

const MissionActionItemNauticalLeisureControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  return (
    <MissionActionItemGenericControl
      action={action}
      onChange={onChange}
      withGeoCoords={true}
      isGeoCoordRequired={false}
      schema={CONTROL_NAUTICAL_LEISURE_SCHEMA}
      data-testid={'action-control-nautical-leisure'}
      component={MissionActionNauticalLeisureControlForm}
    />
  )
}
export default MissionActionItemNauticalLeisureControl
