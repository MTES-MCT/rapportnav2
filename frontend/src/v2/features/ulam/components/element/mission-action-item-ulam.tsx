import { Action } from '@common/types/action-types'
import { createElement, FC } from 'react'
import { useUlamActionRegistry } from '../../hooks/use-ulam-action-registry'

interface MissionActionItemUlamProps {
  action: Action
  missionId?: number
  isMissionFinished?: boolean
}

const MissionActionItemUlam: FC<MissionActionItemUlamProps> = ({ action, missionId, isMissionFinished }) => {
  const { actionComponent } = useUlamActionRegistry(action.type)
  const onChange = (newAction: Action) => {
    console.log(newAction)
  }

  return (
    <div style={{ width: '100%' }}>
      {actionComponent && createElement(actionComponent, { action, onChange, isMissionFinished })}
    </div>
  )
}

export default MissionActionItemUlam
