import React, { useState } from 'react'
import { FieldArray, useFormikContext, getIn } from 'formik'
import { MissionCrewAbsence, MissionCrewAbsenceType } from '../../../../../common/types/crew-type'
import { MissionGeneralInfo2 } from '../../../../../common/types/mission-types'
import TemporaryAbsenceItemForm from './crew-temporary-absence-form-item'
import { Accent, Button, Icon } from '@mtes-mct/monitor-ui'
import { FullMissionAbsenceForm } from './crew-full-mission-absence-form.tsx'
import { Stack } from 'rsuite'

interface Props {
  crewIndex?: number
  absenceType?: MissionCrewAbsenceType
  handleClose: () => void
}

const EMPTY_ABSENCE: MissionCrewAbsence = {
  id: undefined,
  startDate: undefined,
  endDate: undefined,
  reason: undefined,
  isAbsentFullMission: false
}

export const CrewAbsenceForm: React.FC<Props> = ({ crewIndex, absenceType, handleClose }) => {
  const formikContext = useFormikContext<MissionGeneralInfo2>()
  const absencesPath = `crew.${crewIndex}.absences`
  const absences: MissionCrewAbsence[] = getIn(formikContext.values, absencesPath) || []

  // Local state for "new/unsaved" temporary absences
  const [localAbsences, setLocalAbsences] = useState<MissionCrewAbsence[]>([])

  // Display absences: committed ones first, then local placeholders
  const displayAbsences =
    absences.length > 0 || localAbsences.length > 0 ? [...absences, ...localAbsences] : [EMPTY_ABSENCE]

  const isShowingEmptyForm = absences.length === 0 && localAbsences.length === 0

  return (
    <FieldArray name={absencesPath}>
      {arrayHelpers => (
        <Stack direction="column" spacing="2rem">
          <Stack.Item>
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
                    name={`${absencesPath}.0`}
                    crewIndex={crewIndex}
                    key={index}
                    fieldFormik={fieldFormik}
                    handleClose={handleClose}
                  />
                )
              }

              // Temporary absence
              return (
                <TemporaryAbsenceItemForm
                  key={index}
                  fieldFormik={fieldFormik}
                  name={`${absencesPath}.${index}`}
                  showCloseButton={!isShowingEmptyForm}
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
                  onCommit={committedValue => {
                    if (isLocal) {
                      // Update this placeholder with the committedValue
                      const newLocalAbsences = [...localAbsences]
                      newLocalAbsences[index - absences.length] = committedValue

                      // Push all non-empty local absences to parent Formik
                      const toPush = newLocalAbsences.filter(a => a.reason || a.startDate || a.endDate)
                      debugger
                      toPush.forEach(a => arrayHelpers.push(a))

                      // Clear local placeholders
                      setLocalAbsences([])
                    }
                  }}
                />
              )
            })}
          </Stack.Item>

          {absenceType === MissionCrewAbsenceType.TEMPORARY && (
            <Stack.Item>
              <Stack direction="row" spacing="1rem" justifyContent="flex-end" alignItems="center">
                {(absences.length ?? 0) + (localAbsences.length ?? 0) < 3 && (
                  <Button
                    Icon={Icon.Plus}
                    accent={Accent.PRIMARY}
                    onClick={() => {
                      // Add a new local placeholder, no parent mutation
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
