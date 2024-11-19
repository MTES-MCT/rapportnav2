import { Mission } from '@common/types/mission-types.ts'
import React from 'react'
import { Accent, Button, Icon, THEME } from '@mtes-mct/monitor-ui'
import GearIcon from '@rsuite/icons/Gear'
import { useMissionAEMExport } from '../../hooks/use-mission-aem-export.tsx'

interface ExportAEMButtonProps {
  label?: string
  isLoading: boolean
  onClick: () => void
}

const LoadingIcon = () => (
  <GearIcon
    spin
    width={16}
    height={16}
    color={THEME.color.white}
    style={{ fontSize: '2em', marginRight: '0.5rem' }}
    data-testid={'loading-icon'}
  />
)

const ExportFileButton: React.FC<ExportAEMButtonProps> = ({ onClick, isLoading, label }) => {
  const triggerExport = () => {
    onClick()
  }

  return (
    <Button
      accent={Accent.PRIMARY}
      Icon={isLoading ? LoadingIcon : Icon.Download}
      style={{ position: 'absolute', right: '20px' }}
      onClick={() => triggerExport()}
      data-testid={'aem-export-btn'}
    >
      {label ? label : 'Exporter'}
    </Button>
  )
}

export default ExportFileButton
