import { ActionStatusType } from '@common/types/action-types.ts'
import { getColorForStatus, mapStatusToText } from '@common/utils/status-utils'
import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { useTimelineAction } from '../../../common/hooks/use-timeline-action'
import useCreateActionMutation from '../../../common/services/use-create-action'
import { ActionType } from '../../../common/types/action-type'
import { navigateToActionId } from '@router/routes.tsx'
import { useNavigate } from 'react-router-dom'
import { v4 as uuidv4 } from 'uuid'
import { OwnerType } from '../../../common/types/owner-type.ts'

const ACTION_STATUS: ActionStatusType[] = [
  ActionStatusType.NAVIGATING,
  ActionStatusType.ANCHORED,
  ActionStatusType.DOCKED,
  ActionStatusType.UNAVAILABLE
]

interface MissionTimelineAddStatusProps {
  missionId: string
  onSubmit?: (id?: string) => void
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

const MissionTimelineAddStatus: FC<MissionTimelineAddStatusProps> = ({ missionId, onSubmit }) => {
  const navigate = useNavigate()
  const { getActionInput } = useTimelineAction(missionId)
  const mutation = useCreateActionMutation()

  const handleAddStatus = async (status: ActionStatusType) => {
    const action = {
      id: uuidv4(), // Generate a UUID locally
      ...getActionInput(ActionType.STATUS, { status })
    }
    navigateToActionId(action.id, navigate)
    const response = await mutation.mutateAsync({ ownerId: missionId, ownerType: OwnerType.MISSION, action })
    if (onSubmit && response && response.id) onSubmit(response.id)
  }

  return (
    <Dropdown Icon={Icon.FleetSegment} onSelect={handleAddStatus} title="&#x25BC;" placement="bottomEnd">
      {ACTION_STATUS.map(status => (
        <Dropdown.Item key={status} eventKey={status}>
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
