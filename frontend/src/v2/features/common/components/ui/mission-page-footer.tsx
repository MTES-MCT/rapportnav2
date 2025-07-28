import Text from '@common/components/ui/text.tsx'
import { Accent, Button, Icon, Size } from '@mtes-mct/monitor-ui'
import { useState } from 'react'
import { useDate } from '../../hooks/use-date.tsx'
import { useOnlineManager } from '../../hooks/use-online-manager.tsx'
import useDeleteMissionMutation from '../../services/use-delete-mission.tsx'
import useMission from '../../services/use-mission.tsx'
import { MissionSourceEnum } from '../../types/mission-types.ts'
import OnlineToggle from '../elements/online-toggle.tsx'
import PageFooterWrapper from '../layout/page-footer-wrapper.tsx'
import DialogQuestion from './dialog-question.tsx'

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
    <>
      <PageFooterWrapper
        action={
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
        }
        message={
          <Text as="h4">
            {`Connexion ${isOnline ? 'disponible' : 'indisponible'} `}&nbsp;
            {dataUpdatedAt ? `- Dernière synchronisation le ${formatDateForFrenchHumans(dataUpdatedAt)}` : ``}
          </Text>
        }
        online={<OnlineToggle />}
        exitMission={exitMission}
      />
      {showDialog && (
        <DialogQuestion
          type="danger"
          title="Suppression de la mission"
          question="Voulez vous vraiment supprimer cette mission?"
          onSubmit={handleDeleteMission}
        />
      )}
    </>
  )
}

export default MissionPageFooter
