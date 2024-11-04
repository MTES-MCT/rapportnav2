import { Accent, Button, Icon, Size } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'

interface MissionInfractionAddButtonProps {
  onClick: () => void
  disabled: boolean
}

const MissionInfractionAddButton: React.FC<MissionInfractionAddButtonProps> = ({ onClick, disabled }) => {
  return (
    <Stack style={{ marginBottom: '.7em' }} justifyContent="flex-end">
      <Stack.Item>
        <Button
          Icon={Icon.Plus}
          onClick={onClick}
          role={'add-target'}
          size={Size.NORMAL}
          disabled={disabled}
          accent={Accent.SECONDARY}
        >
          Ajouter une infraction
        </Button>
      </Stack.Item>
    </Stack>
  )
}

export default MissionInfractionAddButton
