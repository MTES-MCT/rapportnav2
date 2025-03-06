import { Accent, Button } from '@mtes-mct/monitor-ui'
import { formatISO } from 'date-fns'
import { Field, FieldProps, Formik } from 'formik'
import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Stack } from 'rsuite'
import { MissionULAMGeneralInfoInitial } from '../../../common/types/mission-types.ts'
import useCreateMissionMutation from '../../services/use-create-mission.tsx'
import MissionGeneralInformationInitialFormUlam from './mission-general-information-ulam-initial-form.tsx'

type NewMissionUlam = { missionGeneralInfo: MissionULAMGeneralInfoInitial }

export interface MissionCreateNewUlamProps {
  onClose?: () => void
}

const MissionCreateNewUlam: React.FC<MissionCreateNewUlamProps> = ({ onClose }) => {
  const navigate = useNavigate()
  const mutation = useCreateMissionMutation()

  const initialValues: NewMissionUlam = {
    missionGeneralInfo: {
      startDateTimeUtc: formatISO(new Date()),
      endDateTimeUtc: formatISO(new Date())
    } as MissionULAMGeneralInfoInitial
  }

  const handleSubmit = ({ missionGeneralInfo }: NewMissionUlam, errors: any) => {
    mutation.mutateAsync(missionGeneralInfo).then(r => navigate(`/v2/ulam/missions/${r.id}`))
    if (onClose) onClose()
  }

  return (
    <>
      <Formik initialValues={initialValues} onSubmit={handleSubmit} validateOnChange enableReinitialize>
        {formik => (
          <Stack direction="column" style={{ width: '100%', paddingBottom: '2rem' }} spacing="1.5rem">
            <Stack.Item style={{ width: '100%' }}>
              <Field name={'missionGeneralInfo'}>
                {(field: FieldProps<MissionULAMGeneralInfoInitial>) => (
                  <MissionGeneralInformationInitialFormUlam
                    name="missionGeneralInfo"
                    fieldFormik={field}
                    isCreation={true}
                  />
                )}
              </Field>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Button
                type="submit"
                accent={Accent.PRIMARY}
                disabled={!formik.isValid || formik.isSubmitting}
                onClick={async () => formik.validateForm().then(errors => handleSubmit(formik.values, errors))}
              >
                Créer le rapport
              </Button>

              <Button accent={Accent.SECONDARY} style={{ marginLeft: '10px' }} onClick={onClose}>
                Annuler
              </Button>
            </Stack.Item>
          </Stack>
        )}
      </Formik>
    </>
  )
}

export default MissionCreateNewUlam
