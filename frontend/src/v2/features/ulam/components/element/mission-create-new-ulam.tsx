import { Accent, Button } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import React from 'react'
import { Stack } from 'rsuite'
import { MissionGeneralInfo2 } from '../../../common/types/mission-types.ts'
import { useUlamMissionGeneralInfoForm } from '../../hooks/use-ulam-mission-general-information-form.tsx'
import MissionGeneralInformationUlamFormMain from './mission-general-information-ulam-form-main.tsx'

export interface MissionCreateNewUlamProps {
  onClose?: () => void
  value: MissionGeneralInfo2
  onChange: (newGeneralInfo: MissionGeneralInfo2) => Promise<unknown>
}

const MissionCreateNewUlam: React.FC<MissionCreateNewUlamProps> = ({ value, onClose, onChange }) => {
  const { handleSubmit, initValue, validationSchema } = useUlamMissionGeneralInfoForm(onChange, value)

  return (
    <>
      {initValue && (
        <Formik
          validateOnChange
          enableReinitialize
          onSubmit={handleSubmit}
          initialValues={initValue}
          validationSchema={validationSchema}
        >
          {formik => (
            <Stack direction="column" style={{ width: '100%', paddingBottom: '2rem' }} spacing="1.5rem">
              <Stack.Item style={{ width: '100%' }}>
                <MissionGeneralInformationUlamFormMain isCreation={true} values={formik.values} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Button
                  type="submit"
                  accent={Accent.PRIMARY}
                  disabled={!formik.isValid || formik.isSubmitting || !formik.dirty}
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
      )}
    </>
  )
}

export default MissionCreateNewUlam
