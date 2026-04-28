import { MissionActionType } from '@common/types/fish-mission-types.ts'
import { FormikEffect, TextInput } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker'
import StyledTabs from '../../../common/components/ui/styled-tab.tsx'
import VesselName from '../../../common/components/ui/vessel-name'
import { MissionAction } from '../../../common/types/mission-action'
import { useMissionActionFishControl } from '../../hooks/use-mission-action-fish-control'
import { ActionFishControlInput } from '../../types/action-type'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'

const MissionActionItemFishControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { items, initValue, handleSubmit } = useMissionActionFishControl(action, onChange)
  return (
    <div style={{ width: '100%' }}>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          validateOnChange={true}
          validateOnMount={true}
          enableReinitialize
        >
          {({ values }) => (
            <>
              <FormikEffect onChange={nextValues => handleSubmit(nextValues as ActionFishControlInput)} />
              <Stack
                direction="column"
                spacing="2rem"
                alignItems="flex-start"
                style={{ width: '100%' }}
                data-testid={'action-control-nav'}
              >
                <Stack.Item style={{ width: '100%' }}>
                  <VesselName
                    name={values.vesselName}
                    flagState={values.flagState}
                    externalReferenceNumber={values.externalReferenceNumber}
                  />
                </Stack.Item>
                <Stack.Item grow={1}>
                  <Field name="dates">
                    {(field: FieldProps<Date[]>) => (
                      <FormikDateRangePicker label="" name="dates" isLight={true} fieldFormik={field} />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  {initValue?.fishActionType === MissionActionType.LAND_CONTROL ? (
                    <TextInput
                      label={"Lieu de l'opération"}
                      readOnly={true}
                      value={`${initValue?.portName} ${initValue.portLocode ? `(${initValue.portLocode})` : ''}`}
                      isLight={true}
                      data-testid={'portName'}
                      name="portName"
                    />
                  ) : (
                    <Field name="geoCoords">
                      {(field: FieldProps<number[]>) => (
                        <MissionActionFormikCoordinateInputDMD
                          isLight={true}
                          readOnly={true}
                          name="geoCoords"
                          fieldFormik={field}
                          data-testid="mission-coordinate-dmd-input"
                        />
                      )}
                    </Field>
                  )}
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <StyledTabs
                    items={items}
                    defaultActiveKey={items[0].key}
                    params={{ controlsToComplete: action.controlsToComplete, ...values }}
                  />
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </div>
  )
}

export default MissionActionItemFishControl
