import React from 'react'
import { useFormikContext } from 'formik'
import { Stack } from 'rsuite'
import { CrewAbsenceForm } from './crew-absence-form.tsx'
import { MissionGeneralInfo2 } from '../../../../../common/types/mission-types.ts'
import { MissionCrewAbsenceType } from '../../../../../common/types/crew-type.ts'
import { Accent, DialogProps, Icon, IconButton, IconButtonProps, Size, THEME } from '@mtes-mct/monitor-ui'
import { Dialog } from '@common/components/ui/custom-dialog.tsx'
import styled from 'styled-components'

const CrewFormDialogBody = styled((props: DialogProps) => <Dialog.Body {...props} />)(({ theme }) => ({
  padding: 24,
  width: '100%',
  backgroundColor: theme.color.gainsboro
}))

const CloseIconButton = styled((props: Omit<IconButtonProps, 'Icon'>) => (
  <IconButton
    {...props}
    Icon={Icon.Close}
    size={Size.NORMAL}
    role={'quit-modal-crew'}
    accent={Accent.TERTIARY}
    color={THEME.color.gainsboro}
    data-testid="close-crew-form-icon"
  />
))(({ theme }) => ({
  color: theme.color.gainsboro
}))

interface Props {
  missionId: string
  crewIndex?: number
  absenceType?: MissionCrewAbsenceType
  handleClose: () => void
}

const MissionGeneralInformationCrewPamAbsenceModal: React.FC<Props> = ({
  missionId,
  crewIndex,
  absenceType,
  handleClose
}) => {
  const { values } = useFormikContext<MissionGeneralInfo2>()

  const crew = values.crew?.[crewIndex]
  if (!crew) return null

  return (
    <Dialog data-testid="crew-absence-form">
      <Dialog.Title>
        <Stack direction={'row'} justifyContent={'space-between'} alignItems={'flex-start'} style={{ width: '100%' }}>
          <Stack.Item>
            {absenceType === MissionCrewAbsenceType.FULL_MISSION
              ? 'Déclaration de non participation'
              : 'Ajouter une absence temporaire'}
          </Stack.Item>
          <Stack.Item>
            <CloseIconButton onClick={handleClose} />
          </Stack.Item>
        </Stack>
      </Dialog.Title>
      <CrewFormDialogBody>
        <CrewAbsenceForm
          missionId={missionId}
          crew={crew}
          crewIndex={crewIndex}
          absenceType={absenceType}
          handleClose={handleClose}
        />
      </CrewFormDialogBody>
    </Dialog>
  )
}

export default MissionGeneralInformationCrewPamAbsenceModal
