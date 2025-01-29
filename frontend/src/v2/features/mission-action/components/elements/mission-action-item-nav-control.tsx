import { VesselTypeEnum } from '@common/types/mission-types'
import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikSelectVesselSize } from '../../../common/components/ui/formik-select-vessel-size'
import { MissionAction } from '../../../common/types/mission-action'
import MissionControlNavForm from '../../../mission-control/components/elements/mission-control-nav-form'
import MissionControlNavSummary from '../../../mission-control/components/ui/mission-control-nav-summary'
import { useMissionActionNavControl } from '../../hooks/use-mission-action-nav-control'
import { ActionNavControlInput } from '../../types/action-type'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'
import { MissionActionFormikTextInput } from '../ui/mission-action-formik-text-input'
import MissionActionNavControlWarning from '../ui/mission-action-nav-control-warning'

const MissionActionItemNavControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
  isMissionFinished?: boolean
}> = ({ action, onChange, isMissionFinished }) => {
  const { initValue, handleSubmit, validationSchema } = useMissionActionNavControl(action, onChange, isMissionFinished)

  return (
    <div style={{ width: '100%' }}>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          validateOnChange={true}
          validationSchema={validationSchema}
          enableReinitialize
        >
          {({ values }) => (
            <>
              <FormikEffect onChange={nextValue => handleSubmit(nextValue as ActionNavControlInput)} />
              <Stack
                direction="column"
                spacing="2rem"
                alignItems="flex-start"
                style={{ width: '100%' }}
                data-testid={'action-control-nav'}
              >
                <Stack.Item>
                  <MissionActionNavControlWarning />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionControlNavSummary vesselType={values?.vesselType} controlMethod={values?.controlMethod} />
                </Stack.Item>
                <Stack.Item grow={1}>
                  <Field name="dates">
                    {(field: FieldProps<Date[]>) => (
                      <MissionActionFormikDateRangePicker label="" name="dates" isLight={true} fieldFormik={field} />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionActionFormikCoordinateInputDMD name={'geoCoords'} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack spacing="0.5rem" style={{ width: '100%' }}>
                    <Stack.Item grow={1} basis={'25%'}>
                      <FormikSelectVesselSize name="vesselSize" label="Taille du navire" data-testid={'vesselSize'} />
                    </Stack.Item>
                    <Stack.Item grow={1} basis={'25%'}>
                      <MissionActionFormikTextInput
                        name="vesselIdentifier"
                        label="Immatriculation"
                        data-testid={'vesselIdentifier'}
                      />
                    </Stack.Item>
                    <Stack.Item grow={2} basis={'50%'}>
                      <MissionActionFormikTextInput
                        label="Identité de la personne contrôlée"
                        name="identityControlledPerson"
                        data-testid={'identityControlledPerson'}
                      />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionControlNavForm
                    controlsToComplete={action.controlsToComplete}
                    label={`Contrôle(s) effectué(s) par l’unité sur le navire`}
                    hideGensDeMer={values.vesselType === VesselTypeEnum.SAILING_LEISURE}
                  />
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
          )}
        </Formik>
      )}
    </div>
  )
}

export default MissionActionItemNavControl
