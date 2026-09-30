import { FC } from 'react'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished.tsx'
import { MissionAction } from '../../../common/types/mission-action'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import { getControlSectorFishingSchema } from '../../validation-schema/control-sector-fishing.ts'
import MissionActionItemSectorFishingControlForm from '../ui/mission-action-control-sector-fishing-form.tsx'
import MissionActionItemGenericControl from './mission-action-item-generic-control.tsx'

const MissionActionItemSectorFishingControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { allControlTypes } = useTarget()
  const isMissionFinished = useMissionFinished(action.ownerId)

  return (
    <MissionActionItemGenericControl
      action={action}
      onChange={onChange}
      withGeoCoords={false}
      controlTypes={allControlTypes}
      data-testid={'action-control-sector-fishing'}
      schema={getControlSectorFishingSchema(isMissionFinished)}
      component={MissionActionItemSectorFishingControlForm}
    />
  )
}
export default MissionActionItemSectorFishingControl
