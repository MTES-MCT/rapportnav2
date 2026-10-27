import { Accent, Icon, IconButton, Size } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import useSatiPdfExport from '../../services/use-sati-pdf-export.tsx'

interface SatiPdfExportButtonProps {
  ownerId: string
  actionId: string
  disabled?: boolean
}

const SatiPdfExportButton: FC<SatiPdfExportButtonProps> = ({ ownerId, actionId, disabled = false }) => {
  const mutation = useSatiPdfExport()

  return (
    <IconButton
      accent={Accent.SECONDARY}
      size={Size.SMALL}
      Icon={Icon.Download}
      title={
        disabled
          ? "Le PDF SATI est disponible une fois l'action complète pour les statistiques"
          : 'Télécharger le PDF SATI'
      }
      onClick={() => mutation.mutate({ ownerId, actionId })}
      disabled={disabled || mutation.isPending}
      data-testid="sati-pdf-export-button"
    />
  )
}

export default SatiPdfExportButton
