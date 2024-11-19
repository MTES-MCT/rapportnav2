import { FormikEffect, FormikTextarea, Label, THEME } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import MissionIncompleteControlTag from '../../../common/components/ui/mission-incomplete-control-tag'
import VesselName from '../../../common/components/ui/vessel-name'
import { MissionActionOutput } from '../../../common/types/mission-action-output'
import MissionControlNavForm from '../../../mission-control/components/elements/mission-control-nav-form'
import MissionControlFishAdministrativeSection from '../../../mission-control/components/ui/mission-control-fish-administrative-section'
import FishControlEnginesSection from '../../../mission-control/components/ui/mission-control-fish-engines-section'
import FishControlFleetSegmentSection from '../../../mission-control/components/ui/mission-control-fish-fleet-segment-section'
import FishControlOtherInfractionsSection from '../../../mission-control/components/ui/mission-control-fish-other-infractions-section'
import FishControlOtherObservationsSection from '../../../mission-control/components/ui/mission-control-fish-other-observation-section'
import FishControlSeizureSection from '../../../mission-control/components/ui/mission-control-fish-seizure-section'
import FishControlSpeciesSection from '../../../mission-control/components/ui/mission-control-fish-species-section'
import { useMissionActionFishControl } from '../../hooks/use-mission-action-fish-control'
import { ActionFishControlInput } from '../../types/action-type'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'

const MissionActionItemFishControl: FC<{
  action: MissionActionOutput
  onChange: (newAction: MissionActionOutput, debounceTime?: number) => Promise<unknown>
  isMissionFinished?: boolean
}> = ({ action, onChange, isMissionFinished }) => {
  const { initValue, handleSubmit } = useMissionActionFishControl(action, onChange, isMissionFinished)
  return (
    <div style={{ width: '100%' }}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true}>
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
                  <VesselName name={values.vesselName} />
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
                  <MissionControlFishAdministrativeSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FishControlEnginesSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FishControlSpeciesSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FishControlSeizureSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FishControlOtherInfractionsSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FishControlOtherObservationsSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Divider style={{ backgroundColor: THEME.color.charcoal }} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FishControlFleetSegmentSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="column" alignItems="flex-start">
                    <Stack.Item>
                      <Label>Saisi par</Label>
                    </Stack.Item>
                    <Stack.Item>{values.userTrigram ?? '--'}</Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Divider style={{ backgroundColor: THEME.color.charcoal }} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  {(action?.controlsToComplete?.length ?? 0) > 0 && (
                    <Stack.Item alignSelf="flex-end">
                      <MissionIncompleteControlTag
                        isLight={true}
                        nbrIncompleteControl={action?.controlsToComplete?.length}
                      />
                    </Stack.Item>
                  )}
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionControlNavForm
                    controlsToComplete={action.controlsToComplete}
                    label={`Autre(s) contrôle(s) effectué(s) par l’unité sur le navire`}
                  />
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextarea
                    isLight={true}
                    name="observations"
                    data-testid="observations-by-unit"
                    label="Observations de l’unité sur le contrôle de l’environnement marin"
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
