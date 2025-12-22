import React from 'react'
import { Accent, Button, FormikEffect, FormikSelect, Icon, IconButton, Size } from '@mtes-mct/monitor-ui'
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
  showCloseButton?: boolean
  onCommit: (abs: MissionCrewAbsence) => void
}

const TemporaryAbsenceItemForm: React.FC<Props> = ({ name, fieldFormik, onRemove, showCloseButton, onCommit }) => {
  const { initValue, validationSchema, handleSubmit } = useMissionCrewAbsenceForm(name, fieldFormik)

  const onSubmit = async (nextValues: MissionCrewAbsence) => {
    if (nextValues.reason) {
      debugger
      // onCommit(nextValues)
      await handleSubmit(nextValues)
    }
  }

  return (
    <>
      {initValue && (
        <Formik
          initialValues={initValue}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
          validateOnChange={false}
          isInitialValid={true}
          enableReinitialize={true}
        >
          {formik => (
            <>
              <FormikEffect onChange={nextValues => onSubmit(nextValues)} />
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
                {showCloseButton && (
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
                )}
              </Stack>
            </>
          )}
        </Formik>
      )}
    </>
  )
}

export default TemporaryAbsenceItemForm
