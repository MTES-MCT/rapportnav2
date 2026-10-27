import Text from '@common/components/ui/text'
import { formatDateTimeForFrenchHumans } from '@common/utils/dates-for-humans'
import { IconProps, THEME } from '@mtes-mct/monitor-ui'
import { createElement, FunctionComponent } from 'react'
import { Stack } from 'rsuite'
import { MissionAction } from '../../types/mission-action'
import { OwnerType } from '../../types/owner-type'
import ActionHeaderAction from '../elements/action-header-action'

interface ActionHeaderTitleWrapperProps {
  title?: string
  ownerId: string
  ownerType: OwnerType
  action: MissionAction
  icon?: FunctionComponent<IconProps>
}

export const ActionHeaderTitleWrapper: React.FC<ActionHeaderTitleWrapperProps> = ({
  icon,
  title,
  ownerId,
  ownerType,
  action
}) => {
  return (
    <Stack direction="row" spacing="0.5rem" style={{ width: '100%', alignItems: 'initial' }}>
      <Stack.Item alignSelf="baseline">
        {icon && createElement<IconProps>(icon, { color: THEME.color.charcoal, size: 20 })}
      </Stack.Item>
      <Stack.Item grow={2}>
        <Stack direction="row" spacing={'0.5rem'} wrap={true}>
          <Stack.Item>
            <Text as="h2">{title}</Text>
          </Stack.Item>
          <Stack.Item>
            <Text as="h2" weight={'normal'}>
              ({formatDateTimeForFrenchHumans(action.data.startDateTimeUtc)})
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item>
        {action.id && (
          <ActionHeaderAction action={action} actionId={action.id} ownerId={ownerId} ownerType={ownerType} />
        )}
      </Stack.Item>
    </Stack>
  )
}

export default ActionHeaderTitleWrapper
