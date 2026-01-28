import React, { useState } from 'react'
import { FieldArray, useFormikContext, getIn } from 'formik'
import { MissionCrew, MissionCrewAbsence, MissionCrewAbsenceType } from '../../../../../common/types/crew-type'
import { MissionGeneralInfo2 } from '../../../../../common/types/mission-types'
import TemporaryAbsenceItemForm from './crew-temporary-absence-form-item'
import { Accent, Button, Icon, THEME } from '@mtes-mct/monitor-ui'
import { FullMissionAbsenceForm } from './crew-full-mission-absence-form.tsx'
import { Divider, Stack } from 'rsuite'

interface Props {
  missionId: string
  crewIndex?: number
  absenceType?: MissionCrewAbsenceType
  handleClose: () => void
  crew: MissionCrew
}

const EMPTY_ABSENCE: MissionCrewAbsence = {
  id: undefined,
  startDate: undefined,
  endDate: undefined,
  reason: undefined,
  isAbsentFullMission: false
}

export const CrewAbsenceForm: React.FC<Props> = ({ missionId, crew, crewIndex, absenceType, handleClose }) => {
  const formikContext = useFormikContext<MissionGeneralInfo2>()
  const absencesPath = `crew.${crewIndex}.absences`
  const absences: MissionCrewAbsence[] = getIn(formikContext.values, absencesPath) || []

  // Local state for "new/unsaved" temporary absences
  const [localAbsences, setLocalAbsences] = useState<MissionCrewAbsence[]>([])

  // Display absences: committed ones first, then local placeholders
  const displayAbsences =
    absences.length > 0 || localAbsences.length > 0 ? [...absences, ...localAbsences] : [EMPTY_ABSENCE]

  return (
    <FieldArray name={absencesPath}>
      {arrayHelpers => (
        <Stack direction="column" spacing="0.5rem">
          <Stack.Item style={{ width: '100%' }}>
            {displayAbsences.map((absence: MissionCrewAbsence, index) => {
              const isLocal = index >= absences.length // local unsaved row
              const fieldFormik = {
                field: {
                  value: absence,
                  name: `${absencesPath}.${index}`
                },
                form: formikContext
              } as any

              // Full mission
              if (absenceType === MissionCrewAbsenceType.FULL_MISSION) {
                return (
                  <FullMissionAbsenceForm
                    missionId={missionId}
                    name={`${absencesPath}.0`}
                    key={`${absencesPath}.0`}
                    crewIndex={crewIndex}
                    crew={crew}
                    fieldFormik={fieldFormik}
                    handleClose={handleClose}
                  />
                )
              }

              // Temporary absence
              return (
                <Stack direction="column" key={`${absencesPath}.${index}`}>
                  <Stack.Item style={{ width: '100%' }}>
                    <TemporaryAbsenceItemForm
                      missionId={missionId}
                      fieldFormik={fieldFormik}
                      name={`${absencesPath}.${index}`}
                      onRemove={() => {
                        if (isLocal) {
                          setLocalAbsences(prev => prev.filter((_, i) => i !== index - absences.length))
                        } else {
                          arrayHelpers.remove(index)
                        }

                        // Close the form if no more absences
                        if (absences.length + localAbsences.length === 1) {
                          handleClose()
                        }
                      }}
                      onCommit={() => setLocalAbsences([])}
                    />
                  </Stack.Item>
                  {index < displayAbsences.length - 1 && (
                    <Stack.Item style={{ width: '100%' }}>
                      <Divider style={{ backgroundColor: THEME.color.lightGray }} />
                    </Stack.Item>
                  )}
                </Stack>
              )
            })}
          </Stack.Item>

          {absenceType === MissionCrewAbsenceType.TEMPORARY && (
            <Stack.Item>
              <Stack
                direction="row"
                spacing="1rem"
                justifyContent="flex-end"
                alignItems="center"
                style={{ marginTop: '2rem' }}
              >
                {(absences.length ?? 0) + (localAbsences.length ?? 0) < 3 && (
                  <Button
                    Icon={Icon.Plus}
                    accent={Accent.PRIMARY}
                    onClick={() => {
                      setLocalAbsences(prev => [...prev, EMPTY_ABSENCE])
                    }}
                  >
                    Ajouter une absence temporaire
                  </Button>
                )}
              </Stack>
            </Stack.Item>
          )}
        </Stack>
      )}
    </FieldArray>
  )
}
