import { createElement, FC } from 'react'
import { MissionActionOutput } from '../../common/types/mission-action-output'
import { usePamActionRegistry } from '../hooks/use-pam-action-registry'

interface MissionActionItemPamProps {
  missionId?: number
  isMissionFinished?: boolean
  action: MissionActionOutput
}

const MissionActionItemPam: FC<MissionActionItemPamProps> = ({ action, missionId, isMissionFinished }) => {
  const { actionComponent } = usePamActionRegistry(action.actionType)
  const onChange = async (newAction: MissionActionOutput, debounceTime?: number): Promise<void> => {
    console.log(newAction)
  }

  return (
    <div style={{ width: '100%' }}>
      {actionComponent && createElement(actionComponent, { action, onChange, isMissionFinished })}
    </div>
  )
}

export default MissionActionItemPam
