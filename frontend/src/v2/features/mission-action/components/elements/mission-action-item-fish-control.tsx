import { Action } from '@common/types/action-types'
import { FishAction } from '@common/types/fish-mission-types'
import { FormikEffect, FormikTextarea, Label, THEME } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { isEqual } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Divider, Stack } from 'rsuite'
import MissionIncompleteControlTag from '../../../common/components/ui/mission-incomplete-control-tag'
import VesselName from '../../../common/components/ui/vessel-name'
import { useDate } from '../../../common/hooks/use-date'
import MissionControlNavForm from '../../../mission-control/components/elements/mission-control-nav-form'
import MissionControlFishAdministrativeSection from '../../../mission-control/components/ui/mission-control-fish-administrative-section'
import FishControlEnginesSection from '../../../mission-control/components/ui/mission-control-fish-engines-section'
import FishControlFleetSegmentSection from '../../../mission-control/components/ui/mission-control-fish-fleet-segment-section'
import FishControlOtherInfractionsSection from '../../../mission-control/components/ui/mission-control-fish-other-infractions-section'
import FishControlOtherObservationsSection from '../../../mission-control/components/ui/mission-control-fish-other-observation-section'
import FishControlSeizureSection from '../../../mission-control/components/ui/mission-control-fish-seizure-section'
import FishControlSpeciesSection from '../../../mission-control/components/ui/mission-control-fish-species-section'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'

type ActionDataInput = {
  dates: Date[]
  isMissionFinished: boolean
  geoCoords: (number | undefined)[]
} & FishAction

const MissionActionItemFishControl: FC<{
  action: Action
  onChange: (newAction: Action) => void
  isMissionFinished: boolean
}> = ({ action, onChange, isMissionFinished }) => {
  const data = action?.data as unknown as FishAction
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
      geoCoords: [data.latitude, data.longitude]
    }
    setInitValue(value)
  }, [data, isMissionFinished])

  const handleSubmit = async (value: ActionDataInput): Promise<void> => {
    if (isEqual(value, initValue)) return
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    const data: FishAction = { ...newData }
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
                <VesselName name={data.vesselName} />
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
                <MissionControlFishAdministrativeSection action={data} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FishControlEnginesSection action={data} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FishControlSpeciesSection action={data} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FishControlSeizureSection action={data} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FishControlOtherInfractionsSection action={data} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FishControlOtherObservationsSection action={data} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Divider style={{ backgroundColor: THEME.color.charcoal }} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FishControlFleetSegmentSection action={data} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="column" alignItems="flex-start">
                  <Stack.Item>
                    <Label>Saisi par</Label>
                  </Stack.Item>
                  <Stack.Item>{data.userTrigram ?? '--'}</Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Divider style={{ backgroundColor: THEME.color.charcoal }} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                {(data?.controlsToComplete?.length ?? 0) > 0 && (
                  <Stack.Item alignSelf="flex-end">
                    <MissionIncompleteControlTag
                      isLight={true}
                      nbrIncompleteControl={data?.controlsToComplete?.length}
                    />
                  </Stack.Item>
                )}
              </Stack.Item>
              <MissionControlNavForm
                controlsToComplete={data.controlsToComplete}
                label={`Autre(s) contrôle(s) effectué(s) par l’unité sur le navire`}
              />
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
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemFishControl
