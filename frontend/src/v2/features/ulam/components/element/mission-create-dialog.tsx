import { UTCDate } from '@date-fns/utc'
import { Accent, Dialog, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { FC, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FlexboxGrid, Stack } from 'rsuite'
import { MissionGeneralInfo2 } from 'src/v2/features/common/types/mission-types.ts'
import TimeConversion from '../../../common/components/ui/time-conversion.tsx'
import useCreateMissionMutation from '../../services/use-create-mission.tsx'
import MissionCreateNewUlam from './mission-create-new-ulam.tsx'

interface MissionCreateDialogProps {
  isOpen: boolean
  onClose: () => void
}

const MissionCreateDialog: FC<MissionCreateDialogProps> = ({ isOpen, onClose }) => {
  const navigate = useNavigate()
  const mutation = useCreateMissionMutation()
  const [isDialogOpen, setIsDialogOpen] = useState(isOpen)
  const value = {
    startDateTimeUtc: new UTCDate().toISOString(),
    endDateTimeUtc: new UTCDate().toISOString()
  } as MissionGeneralInfo2

  const onChange = async (value: MissionGeneralInfo2) => {
    mutation.mutateAsync(value).then(response => {
      const id = response.id ?? response.idUUID
      if (id) navigate(`/ulam/missions/${id}`)
    })
    if (onClose) onClose()
  }

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
        <Dialog.Title style={{ border: '1px solid black' }}>
          <FlexboxGrid align="middle" justify="space-between" style={{ paddingLeft: 14, paddingRight: 24 }}>
            <FlexboxGrid.Item style={{ fontSize: '16px' }}>
              <Stack direction="row" alignItems="flex-end">
                <Stack.Item>{`Création d'un rapport (`}</Stack.Item>
                <Stack.Item>
                  <div style={{ marginLeft: 2, marginRight: 2 }}>
                    <TimeConversion />
                  </div>
                </Stack.Item>
                <Stack.Item>{`)`}</Stack.Item>
              </Stack>
            </FlexboxGrid.Item>
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
        <Dialog.Body
          style={{
            width: '100%',
            paddingTop: 31,
            paddingLeft: 14,
            border: '1px solid black',
            backgroundColor: THEME.color.gainsboro
          }}
        >
          <MissionCreateNewUlam onClose={handleClose} value={value} onChange={onChange} />
        </Dialog.Body>
      </Dialog>
    )
  )
}

export default MissionCreateDialog
