import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { useGlobalRoutes } from '@router/use-global-routes'
import { useNavigate } from 'react-router'
import { Stack } from 'rsuite'
import SatiPdfExportButton from '../../../sati/components/ui/sati-pdf-export-button.tsx'
import { useSati } from '../../../sati/hooks/use-sati.tsx'
import useDeleteActionMutation from '../../services/use-delete-action.tsx'
import { MissionAction } from '../../types/mission-action'
import { MissionSourceEnum } from '../../types/mission-types'
import { OwnerType } from '../../types/owner-type'

interface ActionHeaderActionProps {
  action: MissionAction
  actionId: string
  ownerId: string
  ownerType: OwnerType
}

export const ActionHeaderAction: React.FC<ActionHeaderActionProps> = ({ action, actionId, ownerId, ownerType }) => {
  const navigate = useNavigate()
  const { getUrl } = useGlobalRoutes()
  const mutation = useDeleteActionMutation()
  const { canExportSatiPdf } = useSati()

  const handleDelete = async () => {
    mutation.mutate({ ownerId, ownerType, actionId })
    navigate(`${getUrl(ownerType)}/${ownerId}`)
  }

  const isDeleteDisabled = action.source !== MissionSourceEnum.RAPPORT_NAV

  return (
    <Stack direction="row" spacing="0.5rem">
      {canExportSatiPdf(action) && (
        <Stack.Item>
          <SatiPdfExportButton ownerId={ownerId} actionId={actionId} disabled={!action.isCompleteForStats} />
        </Stack.Item>
      )}
      <Stack.Item>
        <IconButton
          accent={Accent.SECONDARY}
          size={Size.SMALL}
          Icon={Icon.Delete}
          onClick={handleDelete}
          data-testid={'deleteButton'}
          disabled={isDeleteDisabled}
          color={isDeleteDisabled ? undefined : THEME.color.maximumRed}
        />
      </Stack.Item>
    </Stack>
  )
}

export default ActionHeaderAction
