import { createElement, FC } from 'react'
import { MissionActionOutput } from '../../../common/types/mission-action-output'
import { useUlamActionRegistry } from '../../hooks/use-ulam-action-registry'

interface MissionActionItemUlamProps {
  missionId?: number
  isMissionFinished?: boolean
  action: MissionActionOutput
}

const MissionActionItemUlam: FC<MissionActionItemUlamProps> = ({ action, missionId, isMissionFinished }) => {
  const { actionComponent } = useUlamActionRegistry(action.actionType)
  const onChange = async (newAction: MissionActionOutput, debounceTime?: number) => {
    console.log(newAction)
  }

  return (
    <div style={{ width: '100%' }}>
      {actionComponent && createElement(actionComponent, { action, onChange, isMissionFinished })}
    </div>
  )
}

export default MissionActionItemUlam
