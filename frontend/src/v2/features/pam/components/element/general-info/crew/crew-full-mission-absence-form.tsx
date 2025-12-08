import React from 'react'
import { Formik, FieldProps } from 'formik'
import { MissionCrew, MissionCrewAbsence } from '../../../../../common/types/crew-type'
import { Accent, Button, FormikEffect, FormikSelect, Size } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { useCrewAbsenceForm } from '../../../../hooks/use-crew-absence-form.tsx'
import { useMissionCrewAbsenceReason } from '../../../../hooks/use-crew-absence-reason.tsx'

interface FullMissionAbsenceFormProps {
  missionId: string
  crew: MissionCrew
  name: string
  fieldFormik: FieldProps<MissionCrewAbsence>
  handleClose: () => void
}

export const FullMissionAbsenceForm: React.FC<FullMissionAbsenceFormProps> = ({
  missionId,
  crew,
  handleClose,
  name,
  fieldFormik
}: FullMissionAbsenceFormProps) => {
  const { initValue, validationSchema, handleSubmit } = useCrewAbsenceForm(name, fieldFormik, missionId)
  const { ABSENCE_REASON_OPTIONS } = useMissionCrewAbsenceReason()

  const onSubmit = async (nextValues: MissionCrewAbsence) => {
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
              <Stack direction="column" spacing={'2rem'}>
                <Stack.Item style={{ width: '100%' }}>
                  <FormikSelect name={'reason'} options={ABSENCE_REASON_OPTIONS} label="Motif" isRequired isLight />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="row" spacing={'1rem'}>
                    <Stack.Item grow={1}></Stack.Item>
                    <Stack.Item grow={1}>
                      <Button accent={Accent.SECONDARY} size={Size.NORMAL} onClick={handleClose} isFullWidth={true}>
                        Annuler
                      </Button>
                    </Stack.Item>
                    <Stack.Item grow={3}>
                      <Button
                        type="submit"
                        disabled={!formik.values.reason}
                        size={Size.NORMAL}
                        onClick={async () => {
                          onSubmit(formik.values)
                        }}
                        isFullWidth={true}
                      >
                        Valider l’absence sur toute la mission
                      </Button>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </>
  )
}
