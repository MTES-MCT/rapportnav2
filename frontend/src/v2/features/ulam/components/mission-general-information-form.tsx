import { FC } from 'react'
import {
  MissionGeneralInfo2,
  MissionGeneralInfoExtended,
  MissionULAMGeneralInfoInitial
} from '../../common/types/mission-types.ts'
import useHandleSubmitMissionGeneralInfoHook, {
  MissionGeneralInfoInput
} from '../hooks/use-submit-mission-general-information.tsx'
import { FormikEffect, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { Field, FieldProps, Formik } from 'formik'
import MissionGeneralInformationInitialForm from './element/mission-general-information-initial-form.tsx'
import MissionGeneralInformationExtendedForm from './element/mission-general-information-extended-form.tsx'

const MissionGeneralInformationForm: FC<{
  generalInfo2?: MissionGeneralInfo2
  onChange: (newGeneralInfo: MissionGeneralInfo2) => Promise<unknown>
}> = ({ generalInfo2, onChange }) => {
  const {handleSubmit, initValue} = useHandleSubmitMissionGeneralInfoHook(onChange, generalInfo2)


  return (
    <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%'}}>
      <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
        {initValue && (
          <Formik initialValues={initValue} onSubmit={handleSubmit} enableReinitialize={true}>
            <>
              <FormikEffect onChange={newValues => handleSubmit(newValues as MissionGeneralInfoInput)} />
              <Stack direction="column" style={{ width: '100%' }} alignItems={'flex-start'}>
                <Stack.Item>
                  <Field name="initial">
                    {(field: FieldProps<MissionULAMGeneralInfoInitial>) => (
                      <MissionGeneralInformationInitialForm
                        name="initial"
                        fieldFormik={field}
                        isCreation={false}
                      />
                    )}
                  </Field>
                </Stack.Item>

                <Stack.Item style={{ width: '60%' }}>
                  <Field name="extended">
                    {(field: FieldProps<MissionGeneralInfoExtended>) => (
                      <MissionGeneralInformationExtendedForm
                        name="extended"
                        fieldFormik={field}
                      />
                    )}
                  </Field>
                </Stack.Item>
              </Stack>
            </>
          </Formik>
        )}
      </Stack>
    </Stack.Item>
  )
}

export default MissionGeneralInformationForm
