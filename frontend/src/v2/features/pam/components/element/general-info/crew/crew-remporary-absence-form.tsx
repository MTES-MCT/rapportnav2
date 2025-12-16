import React from 'react'
import { FieldArray, useFormikContext, getIn } from 'formik'
import { MissionCrewAbsence } from '../../../../../common/types/crew-type'
import { MissionGeneralInfo2 } from '../../../../../common/types/mission-types'
import TemporaryAbsenceItemForm from './crew-temporary-absence-form-item'
import { Accent, Button, Icon, IconButton } from '@mtes-mct/monitor-ui'

interface Props {
  crewIndex: number
}

const EMPTY_ABSENCE: MissionCrewAbsence = {
  id: undefined,
  startDate: undefined,
  endDate: undefined,
  reason: undefined,
  isAbsentFullMission: false
}

export const TemporaryMissionAbsenceForm: React.FC<Props> = ({ crewIndex }) => {
  const formikContext = useFormikContext<MissionGeneralInfo2>()
  const absencesPath = `crew.${crewIndex}.absences`
  const absences: MissionCrewAbsence[] = getIn(formikContext.values, absencesPath) || []
  const displayAbsences = absences.length > 0 ? absences : [EMPTY_ABSENCE]
  const isShowingEmptyForm = absences.length === 0

  return (
    <FieldArray name={absencesPath}>
      {arrayHelpers => (
        <>
          {/*{absences.map((absence, index) => {*/}
          {/*  const fieldFormik = {*/}
          {/*    field: {*/}
          {/*      value: absence,*/}
          {/*      name: `${absencesPath}.${index}`,*/}
          {/*      onChange: () => {},*/}
          {/*      onBlur: () => {}*/}
          {/*    },*/}
          {/*    form: formikContext,*/}
          {/*    meta: {}*/}
          {/*  } as any*/}

          {/*  return (*/}
          {/*    <TemporaryAbsenceItemForm*/}
          {/*      key={index}*/}
          {/*      name={`${absencesPath}.${index}`}*/}
          {/*      fieldFormik={fieldFormik}*/}
          {/*      onRemove={() => arrayHelpers.remove(index)}*/}
          {/*    />*/}
          {/*  )*/}
          {/*})}*/}

          {displayAbsences.map((absence: MissionCrewAbsence, index) => {
            const fieldFormik = {
              field: {
                value: absence,
                name: `${absencesPath}.${index}`,
                onChange: () => {},
                onBlur: () => {}
              },

              form: formikContext,
              meta: {}
            } as any
            return (
              <TemporaryAbsenceItemForm
                key={index}
                fieldFormik={fieldFormik}
                name={isShowingEmptyForm ? `${absencesPath}.0` : `${absencesPath}.${index}`}
                onRemove={() => {
                  if (isShowingEmptyForm) {
                    // Don't actually remove if it's the empty form, just reset it
                    // Or you could do nothing here
                  } else {
                    arrayHelpers.remove(index)
                  }
                }}
              />
            )
          })}

          {absences.length === 0 && (
            <div style={{ color: '#666', fontStyle: 'italic', marginBottom: 16 }}>Aucune absence temporaire</div>
          )}

          {absences.length > 0 && absences.length < 3 && (
            <Button Icon={Icon.Plus} accent={Accent.SECONDARY} onClick={() => arrayHelpers.push(EMPTY_ABSENCE)}>
              Ajouter une absence temporaire
            </Button>
          )}

          {absences.length > 0 && (
            <Button
              accent="secondary"
              onClick={() => formikContext.setFieldValue(absencesPath, [])}
              style={{ marginTop: 16, marginLeft: 8 }}
            >
              Réinitialiser les absences
            </Button>
          )}
        </>
      )}
    </FieldArray>
  )
}
