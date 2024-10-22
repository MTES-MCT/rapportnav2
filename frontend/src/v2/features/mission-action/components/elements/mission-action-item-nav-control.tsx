import { Action, ActionControl } from '@common/types/action-types'
import { VesselSizeEnum } from '@common/types/env-mission-types'
import { VesselTypeEnum } from '@common/types/mission-types'
import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { isEqual } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import * as Yup from 'yup'
import { FormikSelectVesselSize } from '../../../common/components/ui/formik-select-vessel-size'
import { useDate } from '../../../common/hooks/use-date'
import MissionControlNavForm from '../../../mission-control/components/elements/mission-control-nav-form'
import MissionControlNavSummary from '../../../mission-control/components/ui/mission-control-nav-summary'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'
import { MissionActionFormikTextInput } from '../ui/mission-action-formik-text-input'
import MissionActionNavControlWarning from '../ui/mission-action-nav-control-warning'

type ActionDataInput = {
  dates: Date[]
  isMissionFinished: boolean
  geoCoords: (number | undefined)[]
} & ActionControl

const MissionActionItemNavControl: FC<{
  action: Action
  onChange: (newAction: Action) => void
  isMissionFinished?: boolean
}> = ({ action, onChange, isMissionFinished }) => {
  const data = action?.data as unknown as ActionControl
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
    const { dates, isMissionFinished, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    const data: ActionControl = { ...newData }
    setInitValue(value)
    onChange({ ...action, startDateTimeUtc, endDateTimeUtc, data: [data] })
  }

  const formSchema = Yup.object().shape({
    isMissionFinished: Yup.boolean(),
    vesselSize: Yup.mixed<VesselSizeEnum>()
      .nullable()
      .oneOf(Object.values(VesselSizeEnum))
      .when('isMissionFinished', {
        is: true,
        then: schema => schema.nonNullable().required()
      }),
    vesselIdentifier: Yup.string()
      .nullable()
      .when('isMissionFinished', {
        is: true,
        then: schema => schema.nonNullable().required()
      }),
    identityControlledPersonValue: Yup.string().when('isMissionFinished', {
      is: true,
      then: schema => schema.nonNullable().required()
    })
  })

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true} validationSchema={formSchema}>
          <>
            <FormikEffect onChange={nextValues => handleSubmit(nextValues as ActionDataInput)} />
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
                <MissionControlNavSummary vesselType={data?.vesselType} controlMethod={data?.controlMethod} />
              </Stack.Item>
              <Stack.Item grow={1}>
                <MissionActionFormikDateRangePicker name="dates" />
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
                  label={`Contrôle(s) effectué(s) par l’unité sur le navire`}
                  hideGensDeMer={data.vesselType === VesselTypeEnum.SAILING_LEISURE}
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
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemNavControl
