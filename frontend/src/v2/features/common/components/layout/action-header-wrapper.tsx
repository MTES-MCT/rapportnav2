import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent, JSX } from 'react'
import { Stack } from 'rsuite'
import { MissionAction } from '../../types/mission-action.ts'
import { NetworkSyncStatus } from '../../types/network-types.ts'
import { OwnerType } from '../../types/owner-type.ts'
import ActionHeaderSyncStatusBanner from '../ui/action-header-sync-status-banner.tsx'
import ActionHeaderTitleWrapper from './action-header-title-wrapper.tsx'

export type ActionHeaderWrapperProps = {
  title?: string
  actionId?: string
  ownerId: string
  action?: MissionAction
  ownerType: OwnerType
  completeness: JSX.Element
  icon?: FunctionComponent<IconProps>
  missionStatus?: MissionStatusEnum
}

const ActionHeaderWrapper: FC<ActionHeaderWrapperProps> = ({
  icon,
  title,
  action,
  ownerId,
  ownerType,
  completeness,
  missionStatus
}) => {
  return (
    <>
      {action?.networkSyncStatus === NetworkSyncStatus.UNSYNC && <ActionHeaderSyncStatusBanner />}

      <Stack direction="column" spacing={'0.5rem'}>
        <Stack.Item style={{ width: '100%' }}>
          {action?.actionType && (
            <ActionHeaderTitleWrapper
              icon={icon}
              title={title}
              ownerId={ownerId}
              ownerType={ownerType}
              actionId={action.id}
              source={action.source}
              startDateTimeUtc={action?.data.startDateTimeUtc}
            />
          )}
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>{completeness}</Stack.Item>
      </Stack>
    </>
  )
}

export default ActionHeaderWrapper
