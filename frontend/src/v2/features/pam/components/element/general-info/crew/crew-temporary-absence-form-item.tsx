import React from 'react'
import { Accent, FormikEffect, FormikSelect, Icon, IconButton, Size } from '@mtes-mct/monitor-ui'
import { Field, Formik } from 'formik'
import { ABSENCE_REASON_OPTIONS, MissionCrewAbsence } from '../../../../../common/types/crew-type.ts'
import { FormikDateRangePicker } from '../../../../../common/components/ui/formik-date-range-picker.tsx'
import { FieldProps } from 'formik'
import { useMissionCrewAbsenceForm } from '../../../../hooks/use-crew-absence.tsx'
import { Stack } from 'rsuite'

interface Props {
  missionId: string
  name: string
  fieldFormik: FieldProps<MissionCrewAbsence>
  onRemove: () => void
  onCommit: () => void
}

const TemporaryAbsenceItemForm: React.FC<Props> = ({ missionId, name, fieldFormik, onRemove, onCommit }) => {
  const { initValue, validationSchema, handleSubmit } = useMissionCrewAbsenceForm(name, fieldFormik, missionId)

  const onSubmit = async (nextValues: MissionCrewAbsence) => {
    await handleSubmit(nextValues)
    onCommit()
  }

  return (
    <>
      {initValue && (
        <Formik
          initialValues={initValue}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
          validateOnChange={true}
          enableReinitialize={true}
        >
          {formik => (
            <>
              <FormikEffect
                onChange={nextValues => {
                  formik.validateForm(nextValues).then(errors => {
                    if (Object.keys(errors).length === 0) {
                      onSubmit(nextValues)
                    }
                  })
                }}
              />
              <Stack direction="row" spacing={'1rem'} alignItems={'flex-end'}>
                <Stack.Item>
                  <Field name={`dates`}>
                    {(field: FieldProps<Date[]>) => (
                      <FormikDateRangePicker
                        name={`dates`}
                        isLight={true}
                        isRequired={true}
                        fieldFormik={field}
                        validateOnSubmit={false}
                        isCompact={true}
                        withTime={false}
                        allowSameDate={true}
                        isErrorMessageHidden={true}
                        showErrors={false}
                      />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item grow={3}>
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
