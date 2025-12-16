import React from 'react'
import { Button, FormikEffect, FormikSelect } from '@mtes-mct/monitor-ui'
import { Field, Formik } from 'formik'
import { ABSENCE_REASON_OPTIONS, MissionCrewAbsence } from '../../../../../common/types/crew-type.ts'
import { FormikDateRangePicker } from '../../../../../common/components/ui/formik-date-range-picker.tsx'
import { FieldProps } from 'formik'
import { useMissionCrewAbsenceForm } from '../../../../hooks/use-crew-absence.tsx'
import { Stack } from 'rsuite'

interface Props {
  name: string
  fieldFormik: FieldProps<MissionCrewAbsence>
  onRemove: () => void
}

const TemporaryAbsenceItemForm: React.FC<Props> = ({ name, fieldFormik, onRemove }) => {
  const { initValue, validationSchema, handleSubmit } = useMissionCrewAbsenceForm(name, fieldFormik)
  debugger
  return (
    <>
      {initValue && (
        <Formik
          initialValues={initValue}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
          validateOnChange={false}
          isInitialValid={true}
        >
          {formik => (
            <>
              <FormikEffect onChange={nextValues => handleSubmit(nextValues)} />
              <Stack direction="row" spacing={'1rem'} alignItems={'flex-end'}>
                <Stack.Item grow={2}>
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
                      />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item grow={3}>
                  <FormikSelect name="reason" options={ABSENCE_REASON_OPTIONS} label="Motif" isRequired isLight />
                </Stack.Item>
                <Stack.Item>
                  <Button type="submit" color="red" size="xs" onClick={onRemove} style={{ marginTop: 8 }}>
                    x
                  </Button>
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
