import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { Accent, Button, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import Text from '../../../ui/text.tsx'
import { formatDateTimeForFrenchHumans } from '../../../utils/dates.ts'
import ActionReportStatus, { ActionReportStatusProps } from './action-report-status.tsx'

export type ActionHeaderProps = ActionReportStatusProps & {
  icon: Element
  title: string
  date?: string
  showStatus: boolean
  showButtons: boolean
  onDelete?: () => void
  isMissionFinished?: boolean
}

const ActionHeader: FC<ActionHeaderProps> = ({
  icon,
  title,
  date,
  showButtons,
  showStatus,
  onDelete,
  actionSource,
  isCompleteForStats,
  isMissionFinished
}) => {
  const ActionIcon = icon
  return (
    <Stack direction="column" spacing={'0.5rem'}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="0.5rem" style={{ width: '100%', alignItems: 'initial' }}>
          <Stack.Item alignSelf="baseline">
            <ActionIcon color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item grow={2}>
            <Stack direction="row" spacing={'0.5rem'}>
              <Stack.Item>
                <Text as="h2">{title}</Text>
              </Stack.Item>
              <Stack.Item>
                <Text as="h2" weight={'normal'}>
                  ({formatDateTimeForFrenchHumans(date)})
                </Text>
              </Stack.Item>
            </Stack>
          </Stack.Item>
          <Stack.Item>
            {showButtons ? (
              <Stack direction="row" spacing="0.5rem">
                <Stack.Item>
                  <Button accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Duplicate} disabled={true}>
                    Dupliquer
                  </Button>
                </Stack.Item>
                <Stack.Item>
                  <IconButton
                    accent={Accent.SECONDARY}
                    size={Size.SMALL}
                    Icon={Icon.Delete}
                    color={THEME.color.maximumRed}
                    onClick={onDelete}
                    data-testid={'deleteButton'}
                  ></IconButton>
                </Stack.Item>
              </Stack>
            ) : (
              <div style={{ height: '24px' }}></div> // small button height
            )}
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        {showStatus ? (
          <ActionReportStatus
            actionSource={actionSource}
            isCompleteForStats={isCompleteForStats}
            isMissionFinished={isMissionFinished}
          />
        ) : (
          <div style={{ height: '20px' }}></div> // status icon height
        )}
      </Stack.Item>
      <Stack.Item style={{ width: '100%', marginTop: '1rem' }}>
        <Divider style={{ backgroundColor: THEME.color.charcoal, margin: 0 }} />
      </Stack.Item>
    </Stack>
  )
}

export default ActionHeader
