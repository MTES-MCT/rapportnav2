import React from 'react'
import { Formik, FieldProps } from 'formik'
import { ABSENCE_REASON_OPTIONS, MissionCrewAbsence } from '../../../../../common/types/crew-type'
import { Button, FormikEffect, FormikSelect } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { useMissionCrewAbsenceForm } from '../../../../hooks/use-crew-absence.tsx'

interface FullMissionAbsenceFormProps {
  name: string
  fieldFormik: FieldProps<MissionCrewAbsence>
  handleClose: () => void
}

export const FullMissionAbsenceForm: React.FC<Props> = ({
  handleClose,
  name,
  fieldFormik
}: FullMissionAbsenceFormProps) => {
  const { initValue, validationSchema, handleSubmit } = useMissionCrewAbsenceForm(name, fieldFormik)

  const onSubmit = async (nextValues: MissionCrewAbsence) => {
    debugger
    await handleSubmit({ ...nextValues, isAbsentFullMission: true })
    handleClose()
  }

  return (
    <>
      {initValue && (
        <Formik
          initialValues={initValue}
          validationSchema={validationSchema}
          onSubmit={onSubmit}
          validateOnChange={false}
          isInitialValid={true}
        >
          {formik => (
            <>
              <FormikEffect onChange={nextValues => {}} />
              <Stack direction="row" spacing={'1rem'} alignItems={'flex-end'}>
                <Stack.Item grow={3}>
                  <FormikSelect name={'reason'} options={ABSENCE_REASON_OPTIONS} label="Motif" isRequired isLight />
                </Stack.Item>
              </Stack>
              <Stack>
                <Button
                  type="submit"
                  disabled={!formik.values.reason}
                  onClick={async () => onSubmit(formik.values)}
                  isFullWidth={true}
                >
                  Valider l’absence sur toute la mission
                </Button>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </>
  )
}
