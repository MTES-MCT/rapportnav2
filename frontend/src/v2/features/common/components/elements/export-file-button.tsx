import { Mission } from '@common/types/mission-types.ts'
import React from 'react'
import { Accent, Button, ButtonProps, Icon, THEME } from '@mtes-mct/monitor-ui'
import GearIcon from '@rsuite/icons/Gear'
import { useMissionAEMExport } from '../../hooks/use-mission-aem-export.tsx'

type ExportFileButtonProps = ButtonProps & {
  isLoading: boolean
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

const ExportFileButton: React.FC<ExportFileButtonProps> = ({ onClick, isLoading, label, ...props }) => {
  const triggerExport = () => {
    onClick()
  }

  return (
    <Button
      {...props}
      accent={Accent.PRIMARY}
      Icon={isLoading ? LoadingIcon : Icon.Download}
      style={{ position: 'absolute', right: '20px' }}
      onClick={() => triggerExport()}
      data-testid={'export-btn'}
    >
      {label ? label : 'Exporter'}
    </Button>
  )
}

export default ExportFileButton
