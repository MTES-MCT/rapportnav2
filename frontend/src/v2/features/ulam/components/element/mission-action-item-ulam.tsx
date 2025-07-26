import { useStore } from '@tanstack/react-store'
import { isEqual } from 'lodash'
import { createElement, FC } from 'react'
import { store } from '../../../../store'
import { resetDebounceTime } from '../../../../store/slices/delay-query-reducer'
import { useDelay } from '../../../common/hooks/use-delay'
import useUpdateMissionActionMutation from '../../../common/services/use-update-action'
import { MissionAction } from '../../../common/types/mission-action'
import { OwnerType } from '../../../common/types/owner-type'
import { useUlamActionRegistry } from '../../hooks/use-ulam-action-registry'

interface MissionActionItemUlamProps {
  ownerId: string
  action: MissionAction
}

const MissionActionItemUlam: FC<MissionActionItemUlamProps> = ({ action, ownerId }) => {
  const { handleExecuteOnDelay } = useDelay()
  const { component } = useUlamActionRegistry(action.actionType)
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)
  const isMissionFinished = useStore(store, state => state.mission.isMissionFinished)

  const mutation = useUpdateMissionActionMutation(ownerId, OwnerType.MISSION, action.id)

  const onChange = async (newAction: MissionAction) => {
    handleExecuteOnDelay(async () => {
      if (!isEqual(action, newAction)) {
        await mutation.mutateAsync(newAction)
      }
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
