import { FC } from 'react'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { ActionType } from '../../../common/types/action-type.ts'

type MissionTimelineItemSyncStatusProps = {
  actionType?: ActionType
}

const OfflineIcon = ({ color }) => (
  <Icon.Offline color={color} size={14} title={"Cette action n'est pas encore enregistrée sur le serveur"} />
)

const ActionStatusSyncStatus = () => (
  <Stack
    data-testid="action-status-sync-status"
    direction="column"
    justifyContent={'center'}
    style={{ height: '100%' }}
  >
    <Stack.Item alignSelf="center" style={{ width: '100%', textAlign: 'center' }}>
      <OfflineIcon color={THEME.color.charcoal} />
    </Stack.Item>
  </Stack>
)

const OtherActionsSyncStatus = () => (
  <Stack
    data-testid="other-actions-sync-status"
    direction="column"
    justifyContent={'center'}
    style={{ height: '100%', backgroundColor: THEME.color.charcoal }}
  >
    <Stack.Item alignSelf="center" style={{ width: '100%', textAlign: 'center' }}>
      <OfflineIcon color={THEME.color.white} />
    </Stack.Item>
  </Stack>
)

const MissionTimelineItemSyncStatus: FC<MissionTimelineItemSyncStatusProps> = ({ actionType }) => {
  return actionType === ActionType.STATUS ? <ActionStatusSyncStatus /> : <OtherActionsSyncStatus />
}

export default MissionTimelineItemSyncStatus
