import { FC } from 'react'
import { MissionAction } from '../../../common/types/mission-action'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import MissionActionItemGenericControl from './mission-action-item-generic-control.tsx'

const MissionActionItemRoadsideControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { allControlTypes } = useTarget()

  return (
    <MissionActionItemGenericControl
      action={action}
      onChange={onChange}
      withGeoCoords={false}
      controlTypes={allControlTypes}
      data-testid={'action-control-roadside'}
    />
  )
}
export default MissionActionItemRoadsideControl
