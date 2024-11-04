import { Action } from '@common/types/action-types'
import { createElement, FC } from 'react'
import { usePamActionRegistry } from '../hooks/use-pam-action-registry'

interface MissionActionItemPamProps {
  action: Action
  missionId?: number
  isMissionFinished?: boolean
}

const MissionActionItemPam: FC<MissionActionItemPamProps> = ({ action, missionId, isMissionFinished }) => {
  const { actionComponent } = usePamActionRegistry(action.type)
  const onChange = async (newAction: Action, debounceTime?: number): Promise<void> => {
    console.log(newAction)
  }

  return (
    <div style={{ width: '100%' }}>
      {actionComponent && createElement(actionComponent, { action, onChange, isMissionFinished })}
    </div>
  )
}

export default MissionActionItemPam
