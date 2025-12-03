import React from 'react'
import { Accent, Button, ButtonProps, Icon, THEME } from '@mtes-mct/monitor-ui'
import GearIcon from '@rsuite/icons/Gear'

type ExportFileButtonProps = ButtonProps & {
  isLoading: boolean
}

const LoadingIcon = (accent: Accent) => (
  <GearIcon
    spin
    width={16}
    height={16}
    color={accent === Accent.PRIMARY ? THEME.color.white : THEME.color.charcoal}
    style={{ fontSize: '2em', marginRight: '0.5rem' }}
    data-testid={'loading-icon'}
  />
)

const ExportFileButton: React.FC<ExportFileButtonProps> = ({ onClick, isLoading, children, ...props }) => {
  const triggerExport = () => {
    onClick()
  }

  return (
    <Button
      role={'button'}
      accent={Accent.PRIMARY}
      Icon={isLoading ? LoadingIcon : Icon.Download}
      onClick={() => triggerExport()}
      data-testid={'export-btn'}
      aria-busy={isLoading}
      {...props}
    >
      {children ?? 'Exporter'}
    </Button>
  )
}

export default ExportFileButton
