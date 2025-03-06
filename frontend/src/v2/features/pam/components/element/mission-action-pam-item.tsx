import { useStore } from '@tanstack/react-store'
import { createElement, FC } from 'react'
import { store } from '../../../../store'
import { resetDebounceTime } from '../../../../store/slices/delay-query-reducer'
import { useDelay } from '../../../common/hooks/use-delay'
import useUpdateMissionActionMutation from '../../../common/services/use-update-mission-action'
import { MissionAction } from '../../../common/types/mission-action'
import { usePamActionRegistry } from '../../hooks/use-pam-action-registry'

interface MissionActionPamItemProps {
  missionId: number
  action: MissionAction
}

const MissionActionPamItem: FC<MissionActionPamItemProps> = ({ action, missionId }) => {
  const { handleExecuteOnDelay } = useDelay()
  const { component } = usePamActionRegistry(action.actionType)
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)
  const isMissionFinished = useStore(store, state => state.mission.isMissionFinished)

  const mutation = useUpdateMissionActionMutation(missionId, action?.id)

  const onChange = async (newAction: MissionAction): Promise<void> => {
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

export default MissionActionPamItem
