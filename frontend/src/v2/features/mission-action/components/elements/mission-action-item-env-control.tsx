import Text from '@common/components/ui/text'
import { Action } from '@common/types/action-types.ts'
import {
  ControlAdministrative,
  ControlGensDeMer,
  ControlNavigation,
  ControlSecurity,
  ControlType
} from '@common/types/control-types'
import { EnvActionControl } from '@common/types/env-mission-types'
import { FormikEffect, FormikTextarea, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { isEqual } from 'lodash'
import React, { useEffect, useState } from 'react'
import { Divider, Stack } from 'rsuite'
import MissionIncompleteControlTag from '../../../common/components/ui/mission-incomplete-control-tag'
import { useCoordinate } from '../../../common/hooks/use-coordinate'
import { useDate } from '../../../common/hooks/use-date'
import MissionControlEnvForm from '../../../mission-control/components/elements/mission-control-env-form'
import MissionActionEnvControlPlan from '../ui/mission-action-env-control-plan'
import MissionActionEnvControlSummary from '../ui/mission-action-env-control-summary'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'

type ActionDataInput = {
  dates: Date[]
  isMissionFinished: boolean
  geoCoords: [number?, number?]
} & EnvActionControl

type MissionActionItemEnvControlProps = {
  action: Action
  isMissionFinished?: boolean
  onChange: (newAction: Action) => void
}

const MissionActionItemEnvControl: React.FC<MissionActionItemEnvControlProps> = ({
  action,
  onChange,
  isMissionFinished
}) => {
  const data = action?.data as unknown as EnvActionControl
  const { extractLatLngFromMultiPoint } = useCoordinate()
  const [initValue, setInitValue] = useState<ActionDataInput>()
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  useEffect(() => {
    if (!data) return
    const endDate = preprocessDateForPicker(action.endDateTimeUtc)
    const startDate = preprocessDateForPicker(action.startDateTimeUtc)
    const value = {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
      geoCoords: extractLatLngFromMultiPoint(data.geom)
    }
    setInitValue(value)
  }, [data, isMissionFinished])

  const handleSubmit = async (value: ActionDataInput): Promise<void> => {
    if (isEqual(value, initValue)) return
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    const data: EnvActionControl = { ...newData }
    setInitValue(value)
    onChange({ ...action, startDateTimeUtc, endDateTimeUtc, data: [data] })
  }

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true}>
          <>
            <FormikEffect onChange={nextValues => handleSubmit(nextValues as ActionDataInput)} />
            <Stack
              direction="column"
              spacing="2rem"
              alignItems="flex-start"
              style={{ width: '100%' }}
              data-testid={'action-control-nav'}
            >
              <Stack.Item style={{ width: '100%' }}>
                <MissionActionEnvControlPlan controlPlans={data?.formattedControlPlans} />
              </Stack.Item>
              <Stack.Item grow={1}>
                <MissionActionFormikDateRangePicker name="dates" isLight={true} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <MissionActionFormikCoordinateInputDMD name={'geoCoords'} readOnly={true} isLight={true} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Divider style={{ backgroundColor: THEME.color.charcoal }} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" alignItems="flex-start" spacing={'2rem'} style={{ width: '100%' }}>
                  <Stack.Item style={{ width: '33%' }}>
                    <MissionActionEnvControlSummary data={data} />
                  </Stack.Item>

                  <Stack.Item style={{ width: '67%' }}>
                    <Stack direction="column" alignItems="flex-start" spacing={'1.5rem'} style={{ width: '100%' }}>
                      {(data?.controlsToComplete?.length ?? 0) > 0 && (
                        <Stack.Item alignSelf="flex-end">
                          <MissionIncompleteControlTag
                            isLight={true}
                            nbrIncompleteControl={data?.controlsToComplete?.length}
                          />
                        </Stack.Item>
                      )}

                      <Stack.Item>
                        <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                          dont...
                        </Text>
                      </Stack.Item>
                      <Stack.Item style={{ width: '100%' }}>
                        <Field name="controlAdministrative">
                          {(field: FieldProps<ControlAdministrative>) => (
                            <MissionControlEnvForm
                              name="controlAdministrative"
                              fieldFormik={field}
                              controlType={ControlType.ADMINISTRATIVE}
                              maxAmountOfControls={data.actionNumberOfControls}
                              shouldCompleteControl={!!data?.controlsToComplete?.includes(ControlType.ADMINISTRATIVE)}
                            />
                          )}
                        </Field>
                      </Stack.Item>
                      <Stack.Item style={{ width: '100%' }}>
                        <Field name="controlNavigation">
                          {(field: FieldProps<ControlNavigation>) => (
                            <MissionControlEnvForm
                              name="controlNavigation"
                              fieldFormik={field}
                              controlType={ControlType.NAVIGATION}
                              maxAmountOfControls={data.actionNumberOfControls}
                              shouldCompleteControl={!!data?.controlsToComplete?.includes(ControlType.NAVIGATION)}
                            />
                          )}
                        </Field>
                      </Stack.Item>
                      <Stack.Item style={{ width: '100%' }}>
                        <Field name="controlGensDeMer">
                          {(field: FieldProps<ControlGensDeMer>) => (
                            <MissionControlEnvForm
                              name="controlGensDeMer"
                              fieldFormik={field}
                              controlType={ControlType.NAVIGATION}
                              maxAmountOfControls={data.actionNumberOfControls}
                              shouldCompleteControl={!!data?.controlsToComplete?.includes(ControlType.GENS_DE_MER)}
                            />
                          )}
                        </Field>
                      </Stack.Item>
                      <Stack.Item style={{ width: '100%' }}>
                        <Field name="controlSecurity">
                          {(field: FieldProps<ControlSecurity>) => (
                            <MissionControlEnvForm
                              name="controlSecurity"
                              fieldFormik={field}
                              controlType={ControlType.NAVIGATION}
                              maxAmountOfControls={data.actionNumberOfControls}
                              shouldCompleteControl={!!data?.controlsToComplete?.includes(ControlType.SECURITY)}
                            />
                          )}
                        </Field>
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FormikTextarea
                  isLight={true}
                  name="observations"
                  data-testid="observations"
                  label="Observations générales sur le contrôle"
                />
              </Stack.Item>
            </Stack>
          </>
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemEnvControl

/**
 * 
 * 
 *  <Stack.Item style={{ width: '100%' }}>
                    <EnvInfractionAddNewTarget
                      actionTargetType={actionData?.actionTargetType}
                      availableControlTypesForInfraction={actionData?.availableControlTypesForInfraction}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <EnvInfractionExistingTargets
                      actionTargetType={actionData?.actionTargetType}
                      infractionsByTarget={actionData?.infractions}
                      availableControlTypesForInfraction={actionData?.availableControlTypesForInfraction}
                    />
                  </Stack.Item>
 */
