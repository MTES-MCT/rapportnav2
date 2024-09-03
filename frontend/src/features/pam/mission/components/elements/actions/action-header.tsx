import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { Accent, Button, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import Text from '../../../../../common/components/ui/text.tsx'
import { formatDateTimeForFrenchHumans } from '@common/utils/dates-for-humans.ts'
import ActionCompletenessForStatsMessage, {
  ActionCompletenessForStatsMessageProps
} from './action-completeness-for-stats-message.tsx'
import { CompletenessForStats } from '@common/types/mission-types.ts'

export type ActionHeaderProps = ActionCompletenessForStatsMessageProps & {
  icon?: Element
  title: string
  date?: string
  showStatus: boolean
  showButtons: boolean
  onDelete?: () => void
  isMissionFinished?: boolean
  completenessForStats?: CompletenessForStats
}

const ActionHeader: FC<ActionHeaderProps> = ({
  icon,
  title,
  date,
  showButtons,
  showStatus,
  onDelete,
  isMissionFinished,
  completenessForStats
}) => {
  const ActionIcon = icon
  return (
    <Stack direction="column" spacing={'0.5rem'}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="0.5rem" style={{ width: '100%', alignItems: 'initial' }}>
          {icon && (
            <Stack.Item alignSelf="baseline">
              <ActionIcon color={THEME.color.charcoal} size={20} />
            </Stack.Item>
          )}
          <Stack.Item grow={2}>
            <Stack direction="row" spacing={'0.5rem'} wrap={true}>
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
          <ActionCompletenessForStatsMessage
            completenessForStats={completenessForStats}
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
