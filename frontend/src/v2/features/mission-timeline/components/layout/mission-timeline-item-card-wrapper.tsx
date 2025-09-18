import { IconProps, THEME } from '@mtes-mct/monitor-ui'
import React, { createElement, FunctionComponent } from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import { NetworkSyncStatus } from '../../../common/types/network-types.ts'
import MissionTimelineItemSyncStatus from './mission-timeline-item-sync-status.tsx'
import { ActionType } from '../../../common/types/action-type.ts'

type MissionTimelineCardWrapperProps = {
  tags?: JSX.Element
  title?: JSX.Element
  statusTag?: JSX.Element
  footer?: JSX.Element
  subTitle?: JSX.Element
  noPadding?: boolean
  icon?: FunctionComponent<IconProps>
  networkSyncStatus?: NetworkSyncStatus
  actionType?: ActionType
}

const MissionTimelineCardWrapper: React.FC<MissionTimelineCardWrapperProps> = ({
  icon,
  tags,
  title,
  footer,
  subTitle,
  statusTag,
  noPadding,
  networkSyncStatus,
  actionType
}) => {
  return (
    <FlexboxGrid
      style={{
        width: '100%',
        cursor: 'pointer',
        alignItems: 'stretch'
      }}
      justify="start"
      align="middle"
    >
      <FlexboxGrid.Item
        colspan={networkSyncStatus === NetworkSyncStatus.UNSYNC ? 22 : 24}
        style={{
          padding: noPadding ? '0em' : '1rem'
        }}
      >
        <Stack direction="column">
          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" spacing="0.5rem">
              <Stack.Item alignSelf="flex-start">
                {icon ? createElement<IconProps>(icon, { color: THEME.color.charcoal, size: 20 }) : statusTag}
              </Stack.Item>
              <Stack.Item alignSelf="flex-start" style={{ width: '100%' }}>
                <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
                  <Stack.Item style={{ width: '100%' }}>
                    <Stack direction="row" spacing="0.25rem">
                      <Stack.Item style={{ width: '100%' }}>{title}</Stack.Item>
                    </Stack>
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <Stack direction="column" spacing="0.25rem" alignItems="flex-start" style={{ width: '100%' }}>
                      {subTitle && <Stack.Item>{subTitle}</Stack.Item>}
                      {tags && (
                        <Stack.Item alignSelf="flex-start" style={{ width: '100%' }} grow={3}>
                          {tags}
                        </Stack.Item>
                      )}
                    </Stack>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            {footer && (
              <Stack.Item style={{ width: '100%', display: 'flex', justifyContent: statusTag ? 'start' : 'end' }}>
                {footer}
              </Stack.Item>
            )}
          </Stack.Item>
        </Stack>
      </FlexboxGrid.Item>
      {networkSyncStatus === NetworkSyncStatus.UNSYNC && (
        <FlexboxGrid.Item colspan={2} data-testid="network-sync-status">
          <MissionTimelineItemSyncStatus actionType={actionType} />
        </FlexboxGrid.Item>
      )}
    </FlexboxGrid>
  )
}

export default MissionTimelineCardWrapper
