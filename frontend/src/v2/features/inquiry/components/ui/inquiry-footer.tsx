import Text from '@common/components/ui/text.tsx'
import { Accent, Button, Icon, Size } from '@mtes-mct/monitor-ui'
import { useState } from 'react'
import PageFooterWrapper from '../../../common/components/layout/page-footer-wrapper.tsx'
import DialogQuestion from '../../../common/components/ui/dialog-question.tsx'
import { useDate } from '../../../common/hooks/use-date.tsx'
import { useOnlineManager } from '../../../common/hooks/use-online-manager.tsx'
import useDeleteInquiryMutation from '../../services/use-delete-inquiry.tsx'
import useGetInquiryQuery from '../../services/use-inquiry.tsx'

interface InquiryFooterrops {
  inquiryId?: string
  exitMission: () => void
}

const InquiryFooter: React.FC<InquiryFooterrops> = ({ inquiryId, exitMission }) => {
  const { isOnline } = useOnlineManager()
  const { formatDateForFrenchHumans } = useDate()
  const [showDialog, setShowDialog] = useState(false)
  const mutation = useDeleteInquiryMutation(inquiryId)
  const { data: inquiry, dataUpdatedAt } = useGetInquiryQuery(inquiryId)

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
          >
            Supprimer l'enquête
          </Button>
        }
        message={
          <Text as="h4">
            {`Connexion ${isOnline ? 'disponible' : 'indisponible'} `}&nbsp;
            {dataUpdatedAt ? `- Dernière synchronisation le ${formatDateForFrenchHumans(dataUpdatedAt)}` : ``}
          </Text>
        }
        online={
          <Button Icon={Icon.Check} accent={Accent.PRIMARY} disabled={true}>
            Cloturer le contrôle
          </Button>
        }
        exitMission={exitMission}
      />
      {showDialog && (
        <DialogQuestion
          type="danger"
          onSubmit={handleDeleteMission}
          title="Suppression de l'enquête"
          question="Voulez vous vraiment supprimer cette enquête?"
        />
      )}
    </>
  )
}

export default InquiryFooter
