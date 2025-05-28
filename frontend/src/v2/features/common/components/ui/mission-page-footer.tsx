import { Accent, Button, Icon, Size } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import OnlineToggle from '../elements/online-toggle.tsx'
import useMission from '../../services/use-mission.tsx'
import Text from '@common/components/ui/text.tsx'
import { useOnlineManager } from '../../hooks/use-online-manager.tsx'
import { formatDateTimeForFrenchHumans } from '@common/utils/dates-for-humans.ts'

const StyledFooter = styled.div`
  height: 60px;
  background: var(--white-ffffff-) 0% 0% no-repeat padding-box;
  background: #ffffff 0% 0% no-repeat padding-box;
  border-top: 1px solid var(--lightGray-cccfd6-);
  border-top: 1px solid #cccfd6;
  padding: 0 2rem;
`

interface MissionPageFooterProps {
  missionId: number
  exitMission: () => void
}

const MissionPageFooter: React.FC<MissionPageFooterProps> = ({ missionId, exitMission }) => {
  const { dataUpdatedAt } = useMission(missionId)
  const { isOnline } = useOnlineManager()

  const deleteMission = () => {
    // TODO add delete
    alert('Fonctionnalité pas encore implémentée')
    exitMission()
  }

  return (
    <StyledFooter>
      <Stack direction="row" justifyContent="space-between" alignItems="center" style={{ height: '100%' }}>
        <Stack.Item style={{ paddingLeft: '1rem' }}>
          <Button
            accent={Accent.SECONDARY}
            size={Size.NORMAL}
            Icon={Icon.Delete}
            onClick={deleteMission}
            disabled={true}
            title={"Cette fonctionnalité n'a pas encore été implémentée"}
          >
            Supprimer la mission
          </Button>
        </Stack.Item>
        <Stack.Item>
          <Stack direction="row">
            <Stack.Item>
              <Text as="h4">Connexion {isOnline ? 'disponible' : 'indisponible'} </Text>
            </Stack.Item>
            <Stack.Item>
              <Text as="h4">&nbsp;</Text>
            </Stack.Item>
            <Stack.Item>
              <Text as="h4">
                {dataUpdatedAt ? `- Dernière synchronisation le ${formatDateTimeForFrenchHumans(dataUpdatedAt)}` : ``}
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ paddingLeft: '1rem' }}>
          <OnlineToggle />
        </Stack.Item>
      </Stack>
    </StyledFooter>
  )
}

export default MissionPageFooter
