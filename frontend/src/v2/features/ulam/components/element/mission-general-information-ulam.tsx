import { FormikEffect, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { useDelay } from '../../../../features/common/hooks/use-delay'
import { Field, FieldProps, Formik } from 'formik'
import {
  Mission2,
  MissionGeneralInfoExtended,
  MissionULAMGeneralInfoInitial
} from '../../../common/types/mission-types.ts'
import MissionGeneralInformationInitialForm from './mission-general-information-initial-form.tsx'
import { Stack } from 'rsuite'
import MissionGeneralInformationExtendedForm from './mission-general-information-extended-form.tsx'
import { useParams } from 'react-router-dom'
import MissionObservationsUnit from '../../../common/components/elements/mission-observations-unit.tsx'
import useHandleSubmitMissionGeneralInfoHook, { useHandleSubmitMissionGeneralInformation } from '../../hooks/use-submit-mission-general-information.tsx'

type MissionGeneralInformationUlamProps = {
  initial?: MissionULAMGeneralInfoInitial,
  extended?: MissionGeneralInfoExtended,
  mission: Mission2
}

type NewMissionGeneralInfo = { initial: MissionULAMGeneralInfoInitial, extended: MissionGeneralInfoExtended }

const MissionGeneralInformationUlam: React.FC<MissionGeneralInformationUlamProps> = ({initial, extended, mission}) => {
  let { missionId } = useParams()

  const { handleSubmit } = useHandleSubmitMissionGeneralInfoHook(missionId)


  const initialValues: NewMissionGeneralInfo = {
    initial,
    extended
  }

  return (
    <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%'}}>
      <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
        <Formik initialValues={initialValues} onSubmit={handleSubmit} enableReinitialize={true}>
          <>
          <FormikEffect onChange={newValues => handleSubmit(newValues as NewMissionGeneralInfo, mission)} />
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

      </Stack>
      <Stack style={{marginTop: '1.5rem'}}>
        <Stack.Item style={{width:'100%', paddingRight: 20}}>
          <MissionObservationsUnit missionId={mission?.id}/>
        </Stack.Item>
      </Stack>
    </Stack.Item>
  )
}

export default MissionGeneralInformationUlam
