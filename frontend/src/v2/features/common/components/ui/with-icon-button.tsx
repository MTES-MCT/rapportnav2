import { Accent, Button, IconProps, Size } from '@mtes-mct/monitor-ui'
import React, { FunctionComponent } from 'react'
import { Stack } from 'rsuite'

interface WithIconButtonProps {
  onClick: () => void
  disabled?: boolean
  label?: string
  role?: string
  icon: FunctionComponent<IconProps>
}

const WithIconButton: React.FC<WithIconButtonProps> = ({ icon, role, label, onClick, disabled }) => {
  return (
    <Stack style={{ marginBottom: '.7em' }} justifyContent="flex-end">
      <Stack.Item>
        <Button
          Icon={icon}
          onClick={onClick}
          role={role ?? 'with-icon-button'}
          size={Size.NORMAL}
          disabled={disabled}
          accent={Accent.SECONDARY}
        >
          {label ?? 'Ajouter une infraction'}
        </Button>
      </Stack.Item>
    </Stack>
  )
}

export default WithIconButton
