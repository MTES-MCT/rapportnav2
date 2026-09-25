import Text from '@common/components/ui/text.tsx'
import { Accent, Button, Icon } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'

interface MissionListEmptyFilteredProps {
  onReset: () => void
}

/**
 * Empty state shown in place of the list when filters are active but match no mission. Offers a reset action
 * that clears the mission-list filters (see `clearMissionListFilters`).
 */
const MissionListEmptyFiltered: FC<MissionListEmptyFilteredProps> = ({ onReset }) => (
  <Stack direction="column" alignItems="center" spacing="1.5rem" style={{ width: '100%', marginTop: '10rem' }}>
    <Stack.Item>
      <Text as="h2" weight="bold">
        Aucune mission
      </Text>
    </Stack.Item>
    <Stack.Item>
      <Text as="h3">Les filtres sélectionnés ne correspondent à aucune mission de l'unité</Text>
    </Stack.Item>
    <Stack.Item>
      <Button accent={Accent.SECONDARY} Icon={Icon.Reset} onClick={onReset}>
        Réinitialiser les filtres
      </Button>
    </Stack.Item>
  </Stack>
)

export default MissionListEmptyFiltered
