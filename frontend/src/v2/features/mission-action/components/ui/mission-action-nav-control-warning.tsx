import Text from '@common/components/ui/text'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'

const MissionActionNaControlWarning: React.FC = () => {
  return (
    <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
      <Stack.Item alignSelf="baseline">
        <Icon.Info color={THEME.color.charcoal} size={20} />
      </Stack.Item>
      <Stack.Item>
        <Text as="h4" weight="normal" fontStyle="italic">
          Pour la saisie des contrôles de la pêche et de l’environnement marin, veuillez appeler les centres concernés.
          <br />
          Pêche : CNSP / Environnement Marin : CACEM
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionNaControlWarning
