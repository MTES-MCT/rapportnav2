import { useStore } from '@tanstack/react-store'
import { createElement, FC } from 'react'
import { store } from '../../../store'
import { resetDebounceTime } from '../../../store/slices/delay-query-reducer'
import { useDelay } from '../../common/hooks/use-delay'
import { useMissionActionInput } from '../../common/hooks/use-mission-action-input'
import useUpdateMissionActionMutation from '../../common/services/use-update-mission-action'
import { MissionActionOutput } from '../../common/types/mission-action-output'
import { usePamActionRegistry } from '../hooks/use-pam-action-registry'

interface MissionActionItemPamProps {
  missionId?: number
  isMissionFinished?: boolean
  action: MissionActionOutput
}

const MissionActionItemPam: FC<MissionActionItemPamProps> = ({ action, missionId, isMissionFinished }) => {
  const { handleExecuteOnDelay } = useDelay()
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)
  const { getMissionActionInput } = useMissionActionInput()
  const { actionComponent } = usePamActionRegistry(action.actionType)

  const [updateAction] = useUpdateMissionActionMutation(action.id, missionId)

  const onChange = async (newAction: MissionActionOutput): Promise<void> => {
    const input = getMissionActionInput(newAction)

    handleExecuteOnDelay(async () => {
      await updateAction({ variables: { input } })
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
