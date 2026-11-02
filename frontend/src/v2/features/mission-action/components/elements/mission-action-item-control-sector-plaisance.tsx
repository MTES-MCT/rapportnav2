import { FC } from 'react'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished.tsx'
import { MissionAction } from '../../../common/types/mission-action'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import { getControlSectorPlaisanceSchema } from '../../validation-schema/control-sector-plaisance.ts'
import MissionActionItemSectorPlaisanceControlForm from '../ui/mission-action-control-sector-plaisance-form.tsx'
import MissionActionItemGenericControl from './mission-action-item-generic-control.tsx'

const MissionActionItemSectorPlaisanceControl: FC<{
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
      data-testid={'action-control-sector-plaisance'}
      schema={getControlSectorPlaisanceSchema(isMissionFinished)}
      component={MissionActionItemSectorPlaisanceControlForm}
    />
  )
}
export default MissionActionItemSectorPlaisanceControl
