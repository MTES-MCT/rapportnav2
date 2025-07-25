import { FormikEffect, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import {
  MissionGeneralInfo2,
  MissionGeneralInfoExtended,
  MissionULAMGeneralInfoInitial
} from '../../../common/types/mission-types.ts'
import {
  MissionGeneralInfoInput,
  useUlamMissionGeneralInfoForm
} from '../../hooks/use-ulam-mission-general-information-form.tsx'
import MissionGeneralInformationExtendedFormUlam from './mission-general-information-ulam-extended-form.tsx'
import MissionGeneralInformationInitialFormUlam from './mission-general-information-ulam-initial-form.tsx'

const MissionGeneralInformationUlamForm: FC<{
  generalInfo2: MissionGeneralInfo2
  onChange: (newGeneralInfo: MissionGeneralInfo2) => Promise<unknown>
}> = ({ generalInfo2, onChange }) => {
  const { handleSubmit, initValue } = useUlamMissionGeneralInfoForm(onChange, generalInfo2)
  return (
    <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%' }}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} enableReinitialize={true}>
          <>
            <FormikEffect onChange={newValues => handleSubmit(newValues as MissionGeneralInfoInput)} />
            <Stack direction="column" style={{ width: '100%' }} alignItems={'flex-start'}>
              <Stack.Item style={{ width: '100%' }}>
                <Field name="initial">
                  {(field: FieldProps<MissionULAMGeneralInfoInitial>) => (
                    <MissionGeneralInformationInitialFormUlam name="initial" fieldFormik={field} isCreation={false} />
                  )}
                </Field>
              </Stack.Item>

              <Stack.Item style={{ width: '100%' }}>
                <Field name="extended">
                  {(field: FieldProps<MissionGeneralInfoExtended>) => (
                    <MissionGeneralInformationExtendedFormUlam
                      name="extended"
                      fieldFormik={field}
                      missionId={generalInfo2?.missionId}
                      generalInfos2={generalInfo2}
                    />
                  )}
                </Field>
              </Stack.Item>
            </Stack>
          </>
        </Formik>
      )}
    </Stack.Item>
  )
}

export default MissionGeneralInformationUlamForm
