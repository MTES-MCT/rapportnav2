import React, { FC, useEffect, useState } from 'react'
import { Dialog, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { MissionTypeEnum } from '@common/types/env-mission-types.ts'
import MissionGeneralInformationUlamFormNew from './mission-general-information-ulam-form-new.tsx'

interface MissionCreateDialogProps {
  isOpen: boolean
  onClose: () => void
}

const MissionCreateDialog: FC<MissionCreateDialogProps> = ({isOpen, onClose}) => {

  const [isDialogOpen, setIsDialogOpen] = useState(isOpen)

  useEffect(() => {
    setIsDialogOpen(isOpen)
  }, [isOpen])


  const handleClose = () => {
    setIsDialogOpen(false)
    onClose()
  }


  return (
    isDialogOpen && (
      <Dialog>
        <Dialog.Title>Création d'un rapport de mission</Dialog.Title>
        <Dialog.Body style={{ backgroundColor: THEME.color.gainsboro  }}>
          <Stack direction="column" spacing="1.5rem" >
            <MissionGeneralInformationUlamFormNew
              onClose={handleClose}
            />
          </Stack>
        </Dialog.Body>
      </Dialog>
    )
  )
}

export default MissionCreateDialog
