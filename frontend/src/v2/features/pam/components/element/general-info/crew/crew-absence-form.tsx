import React, { useState, useCallback } from 'react'
import { FieldArray, useFormikContext, getIn } from 'formik'
import { MissionCrew, MissionCrewAbsence, MissionCrewAbsenceType } from '../../../../../common/types/crew-type'
import { MissionGeneralInfo2 } from '../../../../../common/types/mission-types'
import TemporaryAbsenceItemForm from './crew-temporary-absence-form-item'
import { Accent, Button, Icon, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import { FullMissionAbsenceForm } from './crew-full-mission-absence-form.tsx'
import { Divider, Stack } from 'rsuite'
import { MissionCrewAbsenceInitialInput } from '../../../../hooks/use-crew-absence-form.tsx'
import { ArrayHelpers } from 'formik/dist/FieldArray'
import Text from '@common/components/ui/text.tsx'

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

interface FormState {
  isValid: boolean
  values: MissionCrewAbsenceInitialInput | null
}

export const CrewAbsenceForm: React.FC<Props> = ({ missionId, crew, crewIndex, absenceType, handleClose }) => {
  const formikContext = useFormikContext<MissionGeneralInfo2>()
  const absencesPath = `crew.${crewIndex}.absences`
  const absences: MissionCrewAbsence[] = getIn(formikContext.values, absencesPath) || []

  // Local state for "new/unsaved" temporary absences
  // Initialize with one empty absence so the user sees a form immediately
  const [localAbsences, setLocalAbsences] = useState<MissionCrewAbsence[]>(absences.length === 0 ? [EMPTY_ABSENCE] : [])

  // Track validity and values for each form row
  const formStatesRef = React.useRef<Record<number, FormState>>({})
  const [, forceUpdate] = useState(0)

  // Display absences: committed ones first, then local ones
  const displayAbsences = [...absences, ...localAbsences]

  const handleValidityChange = useCallback(
    (index: number, isValid: boolean, values: MissionCrewAbsenceInitialInput) => {
      const prev = formStatesRef.current[index]
      if (prev?.isValid !== isValid) {
        formStatesRef.current[index] = { isValid, values }
        forceUpdate(n => n + 1)
      } else {
        formStatesRef.current[index] = { isValid, values }
      }
    },
    []
  )

  // Check if all forms are valid
  const allFormsValid =
    displayAbsences.length > 0 &&
    displayAbsences.every((_, index) => {
      const state = formStatesRef.current[index]
      return state?.isValid === true
    })

  const handleValidate = async () => {
    // Submit all form values to the parent formik context
    const allValues: MissionCrewAbsence[] = []

    for (let i = 0; i < displayAbsences.length; i++) {
      const state = formStatesRef.current[i]
      if (state?.values) {
        const { dates, ...rest } = state.values
        allValues.push({
          ...rest,
          startDate: dates?.[0],
          endDate: dates?.[1]
        } as MissionCrewAbsence)
      }
    }

    await formikContext.setFieldValue(absencesPath, allValues)

    // Clear local absences and close the modal
    setLocalAbsences([])
    handleClose()
  }

  const handleRemoveAbsence = (index: number, isLocal: boolean, arrayHelpers: ArrayHelpers<any>) => {
    delete formStatesRef.current[index]
    if (isLocal) {
      setLocalAbsences(prev => prev.filter((_, i) => i !== index - absences.length))
    } else {
      arrayHelpers.remove(index)
    }

    // Close the form if no more absences
    if (displayAbsences.length === 1) {
      handleClose()
    }
  }

  return (
    <FieldArray name={absencesPath}>
      {arrayHelpers => (
        <Stack direction="column" spacing="0.5rem">
          <Stack.Item style={{ width: '100%', marginBottom: '1rem' }}>
            <Stack direction="row" spacing={'2rem'}>
              <Stack.Item>
                <Label style={{ textAlign: 'left' }}>Prénom Nom</Label>
                <Text as={'h3'} color={THEME.color.charcoal} weight={'medium'}>
                  {crew.agent ? `${crew.agent.firstName} ${crew.agent.lastName}` : crew.fullName}
                </Text>
              </Stack.Item>
              <Stack.Item>
                <Label style={{ textAlign: 'left' }}>Fonction</Label>
                <Text as={'h3'} color={THEME.color.charcoal} weight={'medium'}>
                  {crew.role?.title ?? '-'}
                </Text>
              </Stack.Item>
            </Stack>
          </Stack.Item>
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

              // full mission absence
              if (absenceType === MissionCrewAbsenceType.FULL_MISSION) {
                return (
                  <FullMissionAbsenceForm
                    missionId={missionId}
                    name={`${absencesPath}.0`}
                    key={`${absencesPath}.0`}
                    crew={crew}
                    fieldFormik={fieldFormik}
                    handleClose={handleClose}
                  />
                )
              }

              // temporary absence
              return (
                <Stack direction="column" key={`${absencesPath}.${index}`}>
                  <Stack.Item style={{ width: '100%' }}>
                    <TemporaryAbsenceItemForm
                      missionId={missionId}
                      fieldFormik={fieldFormik}
                      name={`${absencesPath}.${index}`}
                      onValidityChange={(isValid, values) => handleValidityChange(index, isValid, values)}
                      onRemove={() => handleRemoveAbsence(index, isLocal, arrayHelpers)}
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
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" spacing="1rem" alignItems="center" style={{ marginTop: '2rem' }}>
                <Stack.Item grow={2}></Stack.Item>
                <Stack.Item grow={3}>
                  {displayAbsences.length < 3 && (
                    <Button
                      Icon={Icon.Plus}
                      size={Size.NORMAL}
                      accent={Accent.SECONDARY}
                      disabled={!allFormsValid}
                      isFullWidth={true}
                      onClick={() => {
                        setLocalAbsences(prev => [...prev, EMPTY_ABSENCE])
                      }}
                    >
                      Ajouter une absence temporaire
                    </Button>
                  )}
                </Stack.Item>
                <Stack.Item grow={1}>
                  <Button
                    Icon={Icon.Check}
                    accent={Accent.PRIMARY}
                    size={Size.NORMAL}
                    onClick={handleValidate}
                    disabled={!allFormsValid}
                    isFullWidth={true}
                  >
                    Valider
                  </Button>
                </Stack.Item>
              </Stack>
            </Stack.Item>
          )}
        </Stack>
      )}
    </FieldArray>
  )
}
