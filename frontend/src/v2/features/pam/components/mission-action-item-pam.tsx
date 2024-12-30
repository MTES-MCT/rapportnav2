import { useStore } from '@tanstack/react-store'
import { createElement, FC } from 'react'
import { store } from '../../../store'
import { resetDebounceTime } from '../../../store/slices/delay-query-reducer'
import { useDelay } from '../../common/hooks/use-delay'
import useUpdateMissionActionMutation from '../../common/services/use-update-mission-action'
import { MissionAction } from '../../common/types/mission-action'
import { usePamActionRegistry } from '../hooks/use-pam-action-registry'

interface MissionActionItemPamProps {
  missionId: number
  action: MissionAction
  isMissionFinished?: boolean
}

const MissionActionItemPam: FC<MissionActionItemPamProps> = ({ action, missionId, isMissionFinished }) => {
  const { handleExecuteOnDelay } = useDelay()
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)
  const { actionComponent } = usePamActionRegistry(action.actionType)

  const mutation = useUpdateMissionActionMutation(missionId, action?.id)

  const onChange = async (newAction: MissionAction): Promise<void> => {
    handleExecuteOnDelay(async () => {
      await mutation.mutateAsync(newAction)
      if (debounceTime !== undefined) resetDebounceTime()
    }, debounceTime)
  }

  return (
    <div style={{ width: '100%' }}>
      {actionComponent && createElement(actionComponent, { action, onChange, isMissionFinished })}
    </div>
  )
}

export default MissionActionItemPam
