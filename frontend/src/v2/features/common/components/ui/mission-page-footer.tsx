import Text from '@common/components/ui/text.tsx'
import { Accent, Button, Icon, Size } from '@mtes-mct/monitor-ui'
import { useState } from 'react'
import { useDate } from '../../hooks/use-date.tsx'
import OnlineToggle from '../elements/online-toggle.tsx'
import { useOnlineManager } from '../../hooks/use-online-manager.tsx'
import { useOfflineSince } from '../../hooks/use-offline-since.tsx'
import useDeleteMissionMutation from '../../services/use-delete-mission.tsx'
import useMission from '../../services/use-mission.tsx'
import { MissionSourceEnum } from '../../types/mission-types.ts'
import PageFooterWrapper from '../layout/page-footer-wrapper.tsx'
import DialogQuestion from './dialog-question.tsx'
import { useOfflineMode } from '../../hooks/use-offline-mode.tsx'

interface MissionPageFooterProps {
  missionId?: string
  exitMission: () => void
  type: 'ULAM' | 'PAM'
}

const MissionPageFooter: React.FC<MissionPageFooterProps> = ({ missionId, type, exitMission }) => {
  const { isOnline, hasNetwork } = useOnlineManager()
  const { offlineSince } = useOfflineSince()
  const isOfflineModeEnabled = useOfflineMode()
  const { formatDateForFrenchHumans } = useDate()
  const [showDialog, setShowDialog] = useState(false)
  const mutation = useDeleteMissionMutation(missionId)
  const { data: mission } = useMission(missionId)

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
            {isOnline ? '' : hasNetwork ? 'Connexion disponible' : 'Connexion indisponible'}
            {offlineSince ? `- Dernière synchronisation le ${formatDateForFrenchHumans(offlineSince)}` : ``}
          </Text>
        }
        online={type === 'PAM' && isOfflineModeEnabled ? <OnlineToggle /> : <div />}
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
