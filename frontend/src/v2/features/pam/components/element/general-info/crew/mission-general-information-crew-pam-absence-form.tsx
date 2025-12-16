import React, { useEffect } from 'react'
import { useFormikContext } from 'formik'
import { FlexboxGrid } from 'rsuite'
import { FullMissionAbsenceForm } from './crew-full-mission-absence-form.tsx'
import { TemporaryMissionAbsenceForm } from './crew-remporary-absence-form.tsx'
import { MissionGeneralInfo2 } from '../../../../../common/types/mission-types.ts'
import { MissionCrewAbsenceType } from '../../../../../common/types/crew-type.ts'
import { Accent, Dialog, DialogProps, Icon, IconButton, IconButtonProps, Size, THEME } from '@mtes-mct/monitor-ui'
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

const EMPTY_ABSENCE = {
  startDate: undefined,
  endDate: undefined,
  reason: undefined,
  isAbsentFullMission: false
}

interface Props {
  crewIndex?: number
  absenceType?: MissionCrewAbsenceType
  handleClose: (open: boolean) => void
}

const MissionGeneralInformationCrewPamAbsenceForm: React.FC<Props> = ({ crewIndex, absenceType, handleClose }) => {
  const { values } = useFormikContext<MissionGeneralInfo2>()

  const crew = values.crew?.[crewIndex]

  if (!crew) return null

  return (
    <Dialog data-testId={'crew-form'}>
      <Dialog.Title>
        <FlexboxGrid align="middle" justify="space-between" style={{ paddingLeft: 24, paddingRight: 24 }}>
          <FlexboxGrid.Item>
            {absenceType === MissionCrewAbsenceType.FULL_MISSION
              ? 'Déclaration de non participation'
              : 'Ajouter une absence temporaire'}
          </FlexboxGrid.Item>
          <FlexboxGrid.Item>
            <CloseIconButton onClick={() => handleClose(false)} />
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </Dialog.Title>
      <CrewFormDialogBody>
        {absenceType === MissionCrewAbsenceType.FULL_MISSION ? (
          <FullMissionAbsenceForm crewIndex={crewIndex} />
        ) : (
          <TemporaryMissionAbsenceForm crewIndex={crewIndex} />
        )}
      </CrewFormDialogBody>
    </Dialog>
  )
}

export default MissionGeneralInformationCrewPamAbsenceForm
