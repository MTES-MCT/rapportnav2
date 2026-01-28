import React from 'react'
import { useFormikContext } from 'formik'
import { FlexboxGrid } from 'rsuite'
import { CrewAbsenceForm } from './crew-absence-form.tsx'
import { MissionGeneralInfo2 } from '../../../../../common/types/mission-types.ts'
import { MissionCrewAbsenceType } from '../../../../../common/types/crew-type.ts'
import { Accent, Dialog, DialogProps, Icon, IconButton, IconButtonProps, Size, THEME } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'
import Text from '@common/components/ui/text.tsx'

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

const MissionGeneralInformationCrewPamAbsenceForm: React.FC<Props> = ({
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
        <FlexboxGrid align="middle" justify="space-between" style={{ paddingLeft: 24, paddingRight: 24 }}>
          <FlexboxGrid.Item>
            {absenceType === MissionCrewAbsenceType.FULL_MISSION
              ? 'Déclaration de non participation'
              : 'Ajouter une absence temporaire'}
          </FlexboxGrid.Item>
          <FlexboxGrid.Item>
            <CloseIconButton onClick={handleClose} />
          </FlexboxGrid.Item>
        </FlexboxGrid>
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

export default MissionGeneralInformationCrewPamAbsenceForm
