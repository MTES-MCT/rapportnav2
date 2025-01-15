import { useStore } from '@tanstack/react-store'
import { createElement, FC } from 'react'
import { store } from '../../../../store'
import { resetDebounceTime } from '../../../../store/slices/delay-query-reducer'
import { useDelay } from '../../../common/hooks/use-delay'
import useUpdateMissionActionMutation from '../../../common/services/use-update-mission-action'
import { MissionAction } from '../../../common/types/mission-action'
import { useUlamActionRegistry } from '../../hooks/use-ulam-action-registry'

interface MissionActionItemUlamProps {
  missionId: number
  isMissionFinished?: boolean
  action: MissionAction
}

const MissionActionItemUlam: FC<MissionActionItemUlamProps> = ({ action, missionId, isMissionFinished }) => {
  const { handleExecuteOnDelay } = useDelay()
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)
  const { component } = useUlamActionRegistry(action.actionType)

  const mutation = useUpdateMissionActionMutation(missionId, action.id)

  const onChange = async (newAction: MissionAction) => {
    handleExecuteOnDelay(async () => {
      await mutation.mutateAsync(newAction)
      if (debounceTime !== undefined) resetDebounceTime()
    }, debounceTime)
  }

  return (
    <div style={{ width: '100%' }}>
      {component && createElement(component, { action, onChange, isMissionFinished })}
    </div>
  )
}

export default MissionActionItemUlam
