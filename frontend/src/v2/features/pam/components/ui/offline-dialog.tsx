import { FC, useEffect, useState } from 'react'
import { Accent, Button, Dialog, Icon, THEME } from '@mtes-mct/monitor-ui'
import Text from '@common/components/ui/text.tsx'
import { useOnlineManager } from '../../../common/hooks/use-online-manager.tsx'
import { Stack } from 'rsuite'
import { useOfflineSince } from '../../../common/hooks/use-offline-since.tsx'

// export const OFFLINE_CHECK_INTERVAL = 300000 // every 5 minutes
// export const OFFLINE_CHECK_INTERVAL = 60000 // every 1 minute
export const OFFLINE_CHECK_INTERVAL = 6000 // every 1 minute

type OfflineDialogProps = {}

const OfflineDialog: FC<OfflineDialogProps> = () => {
  const { isOffline, hasNetwork, toggleOnline, manualOffline } = useOnlineManager()
  const { offlineSince, isOfflineSinceTooLong } = useOfflineSince()

  const [showModal, setShowModal] = useState(false)

  useEffect(() => {
    const checkOfflineDuration = () => {
      setShowModal(isOfflineSinceTooLong())
    }

    const interval = setInterval(checkOfflineDuration, OFFLINE_CHECK_INTERVAL)

    return () => clearInterval(interval)
  }, [isOffline, offlineSince, manualOffline, isOfflineSinceTooLong])

  const stayOffline = () => {
    setShowModal(false)
  }

  const goOnline = () => {
    if (hasNetwork) {
      toggleOnline(true)
      setShowModal(false)
    }
  }

  return (
    showModal && (
      <Dialog data-testid={'offline-dialog'}>
        <Dialog.Title>Temps passé hors connexion</Dialog.Title>
        <Dialog.Body>
          <Stack direction={'column'} justifyContent={'center'} spacing={'1rem'}>
            <Stack.Item>
              <Icon.Clock color={THEME.color.maximumRed} size={40} />
            </Stack.Item>
            <Stack.Item>
              <Text as={'h3'} color={THEME.color.maximumRed} weight={'bold'}>
                Cela fait 24 heures que vous êtes hors ligne sur RapportNav.
              </Text>
            </Stack.Item>
            <Stack.Item>
              <Text as={'h3'}>
                Par conséquent, vos actions ajoutées hors ligne ne sont pas encore sauvegardées sur le serveur et vous
                n'êtes pas synchronisé avec les centres CNSP et CACEM.
              </Text>
            </Stack.Item>
          </Stack>
        </Dialog.Body>
        <Dialog.Action style={{ justifyContent: 'flex-end', paddingRight: '1.5rem' }}>
          <Button accent={Accent.SECONDARY} onClick={stayOffline}>
            Rester hors ligne
          </Button>
          <Button
            accent={Accent.PRIMARY}
            onClick={goOnline}
            disabled={!hasNetwork}
            title={
              hasNetwork
                ? ''
                : "Vous ne pouvez pas repasser en ligne car vous n'avez pas de réseau. Continuez en mode hors ligne."
            }
          >
            Se reconnecter
          </Button>
        </Dialog.Action>
      </Dialog>
    )
  )
}

export default OfflineDialog
