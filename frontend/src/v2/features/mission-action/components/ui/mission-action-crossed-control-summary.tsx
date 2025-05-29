import Text from '@common/components/ui/text'
import { Label, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { useAgent } from '../../../common/hooks/use-agent'
import { useCrossControl } from '../../../common/hooks/use-crossed-control'
import { useDate } from '../../../common/hooks/use-date'
import { CrossControl } from '../../../common/types/crossed-control-type'

type MissionActionErrorProps = {
  data?: CrossControl
  children: JSX.Element
}

const MissionActionCrossControlSummary: React.FC<MissionActionErrorProps> = ({ data, children }) => {
  const { getAgentById } = useAgent()
  const { formatTime, formatShortDate } = useDate()

  const { getCrossControlOriginType, getCrossControlTargetType } = useCrossControl()

  return (
    <Stack direction="column" spacing="16px" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item>
        <Label color={THEME.color.slateGray}>Date et heure de début</Label>
        <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
          {`Le ${data?.startDateTimeUtc ? formatShortDate(data.startDateTimeUtc) : '--'} à ${data?.startDateTimeUtc ? formatTime(data.startDateTimeUtc) : '--:--'}`}
        </Text>
      </Stack.Item>
      <Stack.Item style={{ width: '50%' }}>
        <Stack direction="column" spacing={'16px'} style={{ width: '100%' }}>
          <Stack.Item style={{ width: '100%' }}>
            <Label color={THEME.color.slateGray}>Origine du contrôle</Label>
            <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
              {getCrossControlOriginType(data?.origin) ?? '--'}
            </Text>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Label color={THEME.color.slateGray}>Agent en charge du contrôle</Label>
            <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
              <>{getAgentById(data?.agentId) ?? '--'}</>
            </Text>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Label color={THEME.color.slateGray}>"Type de cible</Label>
            <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
              {getCrossControlTargetType(data?.type) ?? '--'}
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>{children}</Stack.Item>
    </Stack>
  )
}

export default MissionActionCrossControlSummary
