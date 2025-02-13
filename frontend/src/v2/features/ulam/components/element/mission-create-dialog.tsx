import { Accent, Dialog, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { FC, useEffect, useState } from 'react'
import { FlexboxGrid } from 'rsuite'
import MissionCreateNewUlam from './mission-create-new-ulam.tsx'

interface MissionCreateDialogProps {
  isOpen: boolean
  onClose: () => void
}

const MissionCreateDialog: FC<MissionCreateDialogProps> = ({ isOpen, onClose }) => {
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
        <Dialog.Title>
          <FlexboxGrid align="middle" justify="space-between" style={{ paddingLeft: 14, paddingRight: 24 }}>
            <FlexboxGrid.Item style={{ fontSize: '16px' }}>Création d'un rapport de mission</FlexboxGrid.Item>
            <FlexboxGrid.Item>
              <IconButton
                Icon={Icon.Close}
                size={Size.NORMAL}
                role={'quit-create-mission'}
                accent={Accent.TERTIARY}
                color={THEME.color.gainsboro}
                data-testid="close-create-mission-icon"
                onClick={handleClose}
              />
            </FlexboxGrid.Item>
          </FlexboxGrid>
        </Dialog.Title>
        <Dialog.Body style={{ backgroundColor: THEME.color.gainsboro, paddingLeft: 14, paddingTop: 31, width: '100%' }}>
          <MissionCreateNewUlam onClose={handleClose} />
        </Dialog.Body>
      </Dialog>
    )
  )
}

export default MissionCreateDialog
