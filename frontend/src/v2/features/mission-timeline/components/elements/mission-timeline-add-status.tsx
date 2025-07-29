import { ActionStatusType } from '@common/types/action-types.ts'
import { getColorForStatus, mapStatusToText } from '@common/utils/status-utils'
import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { useTimelineAction } from '../../../common/hooks/use-timeline-action'
import useCreateMissionActionMutation from '../../../common/services/use-create-action'
import { ActionType } from '../../../common/types/action-type'

const ACTION_STATUS: ActionStatusType[] = [
  ActionStatusType.NAVIGATING,
  ActionStatusType.ANCHORED,
  ActionStatusType.DOCKED,
  ActionStatusType.UNAVAILABLE
]

interface MissionTimelineAddStatusProps {
  missionId: string
  onSumbit?: (id?: string) => void
}

export const MissionStatusColorTag: FC<{ status: ActionStatusType }> = ({ status }) => (
  <div
    style={{
      backgroundColor: getColorForStatus(status),
      width: '16px',
      height: '16px',
      borderRadius: '16px'
    }}
  ></div>
)

const MissionTimelineAddStatus: FC<MissionTimelineAddStatusProps> = ({ missionId, onSumbit }) => {
  const { getActionInput } = useTimelineAction(missionId)
  const mutation = useCreateMissionActionMutation(missionId)

  const handleAddStatus = async (status: ActionStatusType) => {
    const action = getActionInput(ActionType.STATUS, { status })
    const response = await mutation.mutateAsync(action)
    if (onSumbit) onSumbit(response?.id)
  }

  return (
    <Dropdown
      Icon={Icon.FleetSegment}
      onSelect={handleAddStatus}
      disabled={mutation.isPending}
      title="&#x25BC;"
      placement="bottomEnd"
    >
      {ACTION_STATUS.map(status => (
        <Dropdown.Item key={status} eventKey={status} disabled={mutation.isPending}>
          <Stack spacing="0.5rem" alignItems="center">
            <Stack.Item>
              <MissionStatusColorTag status={status} />
            </Stack.Item>
            <Stack.Item>{mapStatusToText(status)}</Stack.Item>
          </Stack>
        </Dropdown.Item>
      ))}
    </Dropdown>
  )
}

export default MissionTimelineAddStatus
