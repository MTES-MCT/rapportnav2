import Text from '@common/components/ui/text.tsx'
import { Accent, Button, Icon, Size } from '@mtes-mct/monitor-ui'
import { useState } from 'react'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { useDate } from '../../hooks/use-date.tsx'
import { useOnlineManager } from '../../hooks/use-online-manager.tsx'
import useDeleteMissionMutation from '../../services/use-delete-mission.tsx'
import useMission from '../../services/use-mission.tsx'
import { MissionSourceEnum } from '../../types/mission-types.ts'
import OnlineToggle from '../elements/online-toggle.tsx'
import DialogQuestion from './dialog-question.tsx'

const StyledFooter = styled.div`
  height: 60px;
  background: var(--white-ffffff-) 0% 0% no-repeat padding-box;
  background: #ffffff 0% 0% no-repeat padding-box;
  border-top: 1px solid var(--lightGray-cccfd6-);
  border-top: 1px solid #cccfd6;
  padding: 0 2rem;
`

interface MissionPageFooterProps {
  missionId?: string
  exitMission: () => void
}

const MissionPageFooter: React.FC<MissionPageFooterProps> = ({ missionId, exitMission }) => {
  const { isOnline } = useOnlineManager()
  const { formatDateForFrenchHumans } = useDate()
  const [showDialog, setShowDialog] = useState(false)
  const mutation = useDeleteMissionMutation(missionId)
  const { data: mission, dataUpdatedAt } = useMission(missionId)

  const handleDeleteMission = async (response: boolean) => {
    setShowDialog(false)
    if (!response) return
    await mutation.mutateAsync()
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
            onClick={() => setShowDialog(true)}
            title={"Cette fonctionnalité n'a pas encore été implémentée"}
            disabled={
              !(
                mission &&
                [MissionSourceEnum.RAPPORTNAV, MissionSourceEnum.RAPPORT_NAV].includes(mission.data?.missionSource)
              )
            }
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
                {dataUpdatedAt ? `- Dernière synchronisation le ${formatDateForFrenchHumans(dataUpdatedAt)}` : ``}
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ paddingLeft: '1rem' }}>
          <OnlineToggle />
        </Stack.Item>
      </Stack>
      {showDialog && (
        <DialogQuestion
          type="danger"
          title="Suppression de la mission"
          question="Voulez vous vraiment supprimer cette mission?"
          onSubmit={handleDeleteMission}
        />
      )}
    </StyledFooter>
  )
}

export default MissionPageFooter
