import { FormikEffect, THEME } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { MissionGeneralInfo2 } from '../../../common/types/mission-types.ts'
import {
  MissionGeneralInfoInput,
  useUlamMissionGeneralInfoForm
} from '../../hooks/use-ulam-mission-general-information-form.tsx'
import MissionGeneralInformationUlamFormExtend from './mission-general-information-ulam-form-extend.tsx'
import MissionGeneralInformationUlamFormMainForm from './mission-general-information-ulam-form-main.tsx'

const MissionGeneralInformationUlamForm: FC<{
  generalInfo2: MissionGeneralInfo2
  onChange: (newGeneralInfo: MissionGeneralInfo2) => Promise<unknown>
}> = ({ generalInfo2, onChange }) => {
  const { handleSubmit, initValue, validationSchema } = useUlamMissionGeneralInfoForm(onChange, generalInfo2)
  return (
    <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%' }}>
      {initValue && (
        <Formik
          onSubmit={handleSubmit}
          initialValues={initValue}
          enableReinitialize={true}
          validationSchema={validationSchema}
        >
          {({ values, validateForm }) => (
            <>
              <FormikEffect
                onChange={async nextValue =>
                  //validateForm(nextValue).then(errors => handleSubmit(nextValue as MissionGeneralInfoInput, errors))
                  handleSubmit(nextValue as MissionGeneralInfoInput)
                }
              />
              <Stack direction="column" style={{ width: '100%' }} alignItems={'flex-start'}>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionGeneralInformationUlamFormMainForm values={values} isCreation={false} />
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <MissionGeneralInformationUlamFormExtend values={values} missionId={generalInfo2?.missionId} />
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </Stack.Item>
  )
}

export default MissionGeneralInformationUlamForm
