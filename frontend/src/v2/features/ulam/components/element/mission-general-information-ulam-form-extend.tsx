import { FormikCheckbox, FormikMultiRadio } from '@mtes-mct/monitor-ui'
import { useStore } from '@tanstack/react-store'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps } from 'formik'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { store } from '../../../../store/index.ts'
import { FormikTextAreaInput } from '../../../common/components/ui/formik-textarea-input.tsx'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished.tsx'
import { useMissionType } from '../../../common/hooks/use-mission-type.tsx'
import useAdministrationsQuery from '../../../common/services/use-administrations.tsx'
import useAgentsQuery from '../../../common/services/use-agents.tsx'
import useResourceByControlUnitQuery from '../../../common/services/use-resources-control-unit.tsx'
import MissionGeneralInformationControlUnitResource from '../../../mission-general-infos/components/mission-general-information-control-unit-resource.tsx'
import MissionGeneralInformationCrewNoComment from '../../../mission-general-infos/components/mission-general-information-crew-no-comment.tsx'
import MissionGeneralInformationInterService from '../../../mission-general-infos/components/mission-general-information-inter-service.tsx'
import { MissionGeneralInfoInput } from '../../hooks/use-ulam-mission-general-information-form.tsx'
import { MissionReportTypeEnum } from '../../../common/types/mission-types.ts'

export interface MissionGeneralInformationUlamFormExtendProps {
  missionId?: string
  values: MissionGeneralInfoInput
}

const MissionGeneralInformationUlamFormExtend: FC<MissionGeneralInformationUlamFormExtendProps> = ({
  values,
  missionId
}) => {
  const { data: agents } = useAgentsQuery()
  const user = useStore(store, state => state.user)
  const { isEnvMission, jdpTypeOptions } = useMissionType()
  const isMissionFinished = useMissionFinished(missionId)
  const { data: administrations } = useAdministrationsQuery()
  const { resources } = useResourceByControlUnitQuery(user?.controlUnitId)

  return (
    <Stack direction="column" spacing="1em" style={{ width: '100%' }} justifyContent="flex-start">
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" spacing="1em" justifyContent="flex-start" style={{ width: '80%' }}>
          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="column" spacing=".2em" justifyContent="flex-start" style={{ width: '100%' }}>
              {isEnvMission(values.missionReportType) && (
                <Stack.Item style={{ width: '100%' }}>
                  <Field name="resources">
                    {(field: FieldProps) => (
                      <MissionGeneralInformationControlUnitResource
                        name="resources"
                        fieldFormik={field}
                        controlUnitResources={resources}
                        disabled={values.isResourcesNotUsed}
                        isMissionFinished={isMissionFinished}
                      />
                    )}
                  </Field>
                </Stack.Item>
              )}
              {values.missionReportType === MissionReportTypeEnum.FIELD_REPORT && (
                <Stack.Item style={{ width: '100%' }}>
                  <FormikCheckbox
                    isLight
                    name="isResourcesNotUsed"
                    label="Aucun moyen de l'unité n'est utilisé pour cette mission"
                  />
                </Stack.Item>
              )}
            </Stack>
          </Stack.Item>

          <Stack.Item style={{ width: '100%', marginTop: '1rem' }}>
            <FieldArray name="crew">
              {(fieldArray: FieldArrayRenderProps) => (
                <MissionGeneralInformationCrewNoComment
                  name="crew"
                  agents={agents ?? []}
                  missionId={missionId}
                  fieldArray={fieldArray}
                  isMissionFinished={isMissionFinished}
                />
              )}
            </FieldArray>
          </Stack.Item>

          {isEnvMission(values.missionReportType) && (
            <>
              <Stack.Item style={{ width: '100%', marginTop: 32 }}>
                <FormikCheckbox name={'isMissionArmed'} label={'Mission armée'} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FormikCheckbox name="isUnderJdp" label="Mission sous JDP" />
                {values.isUnderJdp && (
                  <Stack direction="column" spacing="1em" style={{ marginTop: 12 }}>
                    <Stack.Item style={{ width: '100%' }}>
                      <FormikMultiRadio label="Type de JDP" name="jdpType" options={jdpTypeOptions} isInline />
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <Divider style={{ marginTop: 6, marginBottom: 6 }} />
                    </Stack.Item>
                  </Stack>
                )}
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FormikCheckbox
                  name={'isWithInterMinisterialService'}
                  label={'Mission conjointe (avec un autre service)'}
                />
              </Stack.Item>
              <Stack.Item style={{ width: '100%', marginBottom: '1.5rem' }}>
                {values.isWithInterMinisterialService && (
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
        <FormikTextAreaInput
          isLight={false}
          name="observations"
          data-testid="mission-general-observation"
          label="Observation générale à l'échelle de la mission (remarques, résumé)"
        />
      </Stack.Item>
    </Stack>
  )
}

export default MissionGeneralInformationUlamFormExtend
