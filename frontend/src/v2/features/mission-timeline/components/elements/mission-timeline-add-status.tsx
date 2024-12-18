import { ActionStatusType } from '@common/types/action-types.ts'
import { getColorForStatus, mapStatusToText } from '@common/utils/status-utils'
import useAddOrUpdateStatus from '@features/pam/mission/hooks/use-add-update-status'
import { Dropdown, Icon } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { useMissionTimeline } from '../../../common/hooks/use-mission-timeline'

const ACTION_STATUS: ActionStatusType[] = [
  ActionStatusType.NAVIGATING,
  ActionStatusType.ANCHORED,
  ActionStatusType.DOCKED,
  ActionStatusType.UNAVAILABLE
]

interface MissionTimelineAddStatusProps {
  missionId?: number
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
  const [addStatus, { loading }] = useAddOrUpdateStatus()
  const { getBaseInput } = useMissionTimeline(missionId?.toString())

  const handleAddStatus = async (status: ActionStatusType) => {
    const statusAction = {
      status,
      reason: null,
      observations: null,
      ...getBaseInput()
    }
    const response = await addStatus({ variables: { statusAction } })
    if (onSumbit) onSumbit(response.data?.id)
  }

  return (
    <Dropdown
      Icon={Icon.FleetSegment}
      onSelect={handleAddStatus}
      disabled={loading}
      title="&#x25BC;"
      placement="bottomEnd"
    >
      {ACTION_STATUS.map(status => (
        <Dropdown.Item key={status} eventKey={status} disabled={loading}>
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
