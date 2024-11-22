import Text from '@common/components/ui/text'
import { Label, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { useActionTarget } from '../../../common/hooks/use-action-target'
import { useVehicule } from '../../../common/hooks/use-vehicule'
import { MissionEnvActionDataOutput } from '../../../common/types/mission-env-action-output'

type MissionActionErrorProps = {
  data: MissionEnvActionDataOutput
}
const MissionActionEnvControlSummary: React.FC<MissionActionErrorProps> = ({ data }) => {
  const { getVehiculeType } = useVehicule()
  const { getActionTargetType } = useActionTarget()
  return (
    <Stack direction="column" spacing={'1rem'} alignItems="flex-start">
      <Stack.Item>
        <Label>Nbre total de contrôles</Label>
        <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
          {data?.actionNumberOfControls ?? 'inconnu'}
        </Text>
      </Stack.Item>
      <Stack.Item>
        <Label>Type de cible</Label>
        <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
          {getActionTargetType(data?.actionTargetType) ?? 'inconnu'}
        </Text>
      </Stack.Item>
      <Stack.Item>
        <Label>Type de véhicule</Label>
        <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
          {getVehiculeType(data?.vehicleType) ?? 'inconnu'}
        </Text>
      </Stack.Item>
      <Stack.Item>
        <Label>Observations</Label>
        <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
          {data?.observations ?? 'aucunes'}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionEnvControlSummary
