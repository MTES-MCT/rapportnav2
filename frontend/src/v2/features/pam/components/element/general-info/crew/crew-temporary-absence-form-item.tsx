import React from 'react'
import Text from '@common/components/ui/text.tsx'
import { Accent, FormikEffect, FormikSelect, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { Field, Formik } from 'formik'
import { MissionCrewAbsence } from '../../../../../common/types/crew-type.ts'
import { FormikDateRangePicker } from '../../../../../common/components/ui/formik-date-range-picker.tsx'
import { FieldProps } from 'formik'
import { useMissionCrewAbsenceForm, MissionCrewAbsenceInitialInput } from '../../../../hooks/use-crew-absence.tsx'
import { Stack } from 'rsuite'
import { useMissionCrewAbsenceReason } from '../../../../hooks/use-crew-absence-reason.tsx'
import { isEmpty } from 'lodash'

interface Props {
  missionId: string
  name: string
  fieldFormik: FieldProps<MissionCrewAbsence>
  onRemove: () => void
  onValidityChange: (isValid: boolean, values: MissionCrewAbsenceInitialInput) => void
}

const TemporaryAbsenceItemForm: React.FC<Props> = ({ missionId, name, fieldFormik, onRemove, onValidityChange }) => {
  const { initValue, validationSchema } = useMissionCrewAbsenceForm(name, fieldFormik, missionId)
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
              <Stack direction="row" spacing={'1rem'} alignItems={'flex-end'}>
                <Stack.Item>
                  <Field name={`dates`}>
                    {(field: FieldProps<Date[]>) => (
                      <FormikDateRangePicker
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
              <Stack direction="row" spacing={'1rem'} alignItems={'flex-end'} style={{ marginTop: '0.2rem' }}>
                <Text as={'h3'} color={THEME.color.maximumRed}>
                  {formik.touched && formik.errors.dates}
                </Text>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </>
  )
}

export default TemporaryAbsenceItemForm
