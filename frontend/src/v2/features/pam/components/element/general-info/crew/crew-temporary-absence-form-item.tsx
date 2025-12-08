import React from 'react'
import { Accent, FormikEffect, FormikSelect, Icon, IconButton, Size } from '@mtes-mct/monitor-ui'
import { Field, Formik, FieldProps } from 'formik'
import { MissionCrewAbsence } from '../../../../../common/types/crew-type.ts'
import { useCrewAbsenceForm, MissionCrewAbsenceInitialInput } from '../../../../hooks/use-crew-absence-form.tsx'
import { Stack } from 'rsuite'
import { useMissionCrewAbsenceReason } from '../../../../hooks/use-crew-absence-reason.tsx'
import MissionBoundFormikDateRangePicker from '../../../../../common/components/elements/mission-bound-formik-date-range-picker.tsx'

interface Props {
  missionId: string
  name: string
  fieldFormik: FieldProps<MissionCrewAbsence>
  onRemove: () => void
  onValidityChange: (isValid: boolean, values: MissionCrewAbsenceInitialInput) => void
}

const TemporaryAbsenceItemForm: React.FC<Props> = ({ missionId, name, fieldFormik, onRemove, onValidityChange }) => {
  const { initValue, validationSchema } = useCrewAbsenceForm(name, fieldFormik, missionId)
  const { ABSENCE_REASON_OPTIONS } = useMissionCrewAbsenceReason()

  return (
    <>
      {initValue && (
        <Formik
          initialValues={initValue}
          validationSchema={validationSchema}
          onSubmit={() => {}}
          validateOnChange={true}
          enableReinitialize={true}
        >
          {formik => (
            <>
              <FormikEffect
                onChange={nextValues => {
                  formik.validateForm(nextValues).then(errors => {
                    const isValid = Object.keys(errors).length === 0
                    onValidityChange(isValid, nextValues as MissionCrewAbsenceInitialInput)
                  })
                }}
              />
              <Stack direction="row" spacing={'1rem'} alignItems={'flex-start'}>
                <Stack.Item style={{ width: '60%' }}>
                  <Field name={`dates`}>
                    {(field: FieldProps<Date[]>) => (
                      <MissionBoundFormikDateRangePicker
                        name={`dates`}
                        label={'Dates de début et de fin'}
                        isLight={true}
                        isRequired={true}
                        fieldFormik={field}
                        validateOnSubmit={false}
                        isCompact={true}
                        withTime={false}
                        allowSameDate={true}
                        isErrorMessageHidden={false}
                        missionId={missionId}
                      />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item style={{ width: '40%' }}>
                  <FormikSelect
                    name="reason"
                    options={ABSENCE_REASON_OPTIONS}
                    label="Motif"
                    isRequired
                    isLight
                    isErrorMessageHidden={true}
                  />
                </Stack.Item>
                <Stack.Item>
                  <IconButton
                    role="delete-absence"
                    size={Size.NORMAL}
                    accent={Accent.TERTIARY}
                    Icon={Icon.Delete}
                    title={'Supprimer une absence'}
                    data-testid="delete-absence"
                    onClick={onRemove}
                    style={{ marginTop: '24px' }}
                  />
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </>
  )
}

export default TemporaryAbsenceItemForm
