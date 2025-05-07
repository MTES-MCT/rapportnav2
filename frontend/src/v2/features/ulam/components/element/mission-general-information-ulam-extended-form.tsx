import { FormikCheckbox, FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { useStore } from '@tanstack/react-store'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { store } from '../../../../store/index.ts'
import useAdministrationsQuery from '../../../common/services/use-administrations.tsx'
import useAgentsQuery from '../../../common/services/use-agents.tsx'
import useControlUnitResourcesQuery from '../../../common/services/use-control-unit-resources.tsx'
import { MissionGeneralInfo2, MissionGeneralInfoExtended } from '../../../common/types/mission-types.ts'
import MissionGeneralInformationControlUnitResource from '../../../mission-general-infos/components/mission-general-information-control-unit-resource.tsx'
import MissionGeneralInformationCrewNoComment from '../../../mission-general-infos/components/mission-general-information-crew-no-comment.tsx'
import MissionGeneralInformationInterService from '../../../mission-general-infos/components/mission-general-information-inter-service.tsx'
import { useUlamMissionGeneralInformationsExtendedForm } from '../../hooks/use-ulam-mission-general-informations-extended-form.tsx'
import { useMissionType } from '../../../common/hooks/use-mission-type.tsx'

export interface MissionGeneralInformationUlamExtendedFormProps {
  name: string
  missionId?: number
  fieldFormik: FieldProps<MissionGeneralInfoExtended>
  generalInfos2: MissionGeneralInfo2
}

const MissionGeneralInformationUlamExtendedForm: FC<MissionGeneralInformationUlamExtendedFormProps> = ({
  name,
  missionId,
  fieldFormik,
  generalInfos2
}) => {
  const { data: agents } = useAgentsQuery()
  const user = useStore(store, state => state.user)
  const { data: administrations } = useAdministrationsQuery()
  const { data: resources } = useControlUnitResourcesQuery(user?.controlUnitId)
  const { initValue, handleSubmit } = useUlamMissionGeneralInformationsExtendedForm(name, fieldFormik)
  const { isEnvMission } = useMissionType()


  return (
    <>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} enableReinitialize={true}>
          {formik => (
            <Stack direction="column" spacing="1em" style={{ width: '100%' }} justifyContent="flex-start">
              <FormikEffect onChange={newValues => handleSubmit(newValues)} />

              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="column" spacing="1em" justifyContent="flex-start" style={{ width: '70%' }}>
                  {isEnvMission(generalInfos2.missionReportType) && (
                    <Stack.Item style={{ width: '100%' }}>
                      <Field name="resources">
                        {(field: FieldProps) => (
                          <MissionGeneralInformationControlUnitResource
                            name="resources"
                            fieldFormik={field}
                            controlUnitResources={resources}
                          />
                        )}
                      </Field>
                    </Stack.Item>
                  )}

                  <Stack.Item style={{ width: '100%' }}>
                    <FieldArray name="crew">
                      {(fieldArray: FieldArrayRenderProps) => (
                        <MissionGeneralInformationCrewNoComment
                          name="crew"
                          agents={agents ?? []}
                          missionId={missionId}
                          fieldArray={fieldArray}
                        />
                      )}
                    </FieldArray>
                  </Stack.Item>

                  {isEnvMission(generalInfos2.missionReportType) && (
                    <>
                      <Stack.Item style={{ width: '100%', marginTop: 32 }}>
                        <FormikCheckbox name={'isMissionArmed'} label={'Mission armée'} />
                      </Stack.Item>
                      <Stack.Item style={{ width: '100%' }}>
                        <FormikCheckbox
                          name={'isWithInterMinisterialService'}
                          label={'Mission conjointe (avec un autre service)'}
                        />
                      </Stack.Item>
                      <Stack.Item style={{ width: '100%', marginBottom: '1.5rem' }}>
                        {formik.values.isWithInterMinisterialService && (
                          <Field name="interMinisterialServices">
                            {(field: FieldProps) => (
                              <MissionGeneralInformationInterService
                                fieldFormik={field}
                                name="interMinisterialServices"
                                administrations={administrations ?? []}
                              />
                            )}
                          </Field>
                        )}
                      </Stack.Item>
                    </>
                  )}

                </Stack>
              </Stack.Item>

              <Stack.Item style={{ width: '100%' }}>
                <FormikTextarea
                  isRequired={true}
                  isLight={false}
                  name="observations"
                  data-testid="mission-general-observation"
                  label="Observation générale à l'échelle de la mission (remarques, résumé)"
                  isErrorMessageHidden={true}
                />
              </Stack.Item>
            </Stack>
          )}
        </Formik>
      )}
    </>
  )
}

export default MissionGeneralInformationUlamExtendedForm
