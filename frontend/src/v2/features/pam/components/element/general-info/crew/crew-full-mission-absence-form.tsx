import React from 'react'
import { Formik, FieldProps } from 'formik'
import { ABSENCE_REASON_OPTIONS, MissionCrew, MissionCrewAbsence } from '../../../../../common/types/crew-type'
import { Button, FormikEffect, FormikSelect, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { useMissionCrewAbsenceForm } from '../../../../hooks/use-crew-absence.tsx'
import Text from '@common/components/ui/text.tsx'

interface FullMissionAbsenceFormProps {
  missionId: string
  crew: MissionCrew
  name: string
  fieldFormik: FieldProps<MissionCrewAbsence>
  handleClose: () => void
}

export const FullMissionAbsenceForm: React.FC<Props> = ({
  missionId,
  crew,
  handleClose,
  name,
  fieldFormik
}: FullMissionAbsenceFormProps) => {
  const { initValue, validationSchema, handleSubmit } = useMissionCrewAbsenceForm(name, fieldFormik, missionId)

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
                  <FormikSelect name={'reason'} options={ABSENCE_REASON_OPTIONS} label="Motif" isRequired isLight />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Button
                    type="submit"
                    disabled={!formik.values.reason}
                    size={Size.LARGE}
                    onClick={async () => {
                      onSubmit(formik.values)
                    }}
                    isFullWidth={true}
                  >
                    Valider l’absence sur toute la mission
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
