import Text from '@common/components/ui/text'
import { ToggleLabel } from '@common/components/ui/toogle-label'
import { Action, ActionRescue } from '@common/types/action-types'
import {
  FormikCheckbox,
  FormikEffect,
  FormikMultiRadio,
  FormikTextarea,
  FormikTextInput,
  FormikToggle,
  THEME
} from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { isEqual, mapValues, pick } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Divider, Stack } from 'rsuite'
import * as Yup from 'yup'
import { useDate } from '../../../common/hooks/use-date'
import { RescueType } from '../../../common/types/rescue-type'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'
import { MissionActionFormikNumberInput } from '../ui/mission-action-formik-number-input'

const RESCUE_TYPE_OPTIONS = [
  {
    label: 'Assistance de navire en difficulté',
    value: RescueType.VESSEL
  },
  {
    label: 'Sauvegarde de la vie humaine',
    value: RescueType.PERSON
  }
]

type ActionDataInput = {
  dates: Date[]
  rescueType: RescueType
  geoCoords: (number | undefined)[]
} & ActionRescue

const MissionActionItemRescue: FC<{
  action: Action
  isMissionFinished?: boolean
  onChange: (newAction: Action) => void
}> = ({ action, onChange, isMissionFinished }) => {
  const data = action?.data as unknown as ActionRescue
  const [initValue, setInitValue] = useState<ActionDataInput>()
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  useEffect(() => {
    if (!data) return
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    const rescueType = data.isVesselRescue ? RescueType.VESSEL : RescueType.PERSON

    const booleans = mapValues(
      pick(
        data,
        'isVesselRescue',
        'isVesselTowed',
        'isPersonRescue',
        'isVesselNoticed',
        'isMigrationRescue',
        'operationFollowsDEFREP',
        'isInSRRorFollowedByCROSSMRCC'
      ),
      o => !!o
    )
    const value = {
      ...data,
      rescueType,
      ...booleans,
      dates: [startDate, endDate],
      geoCoords: [data.latitude ?? 0, data.longitude ?? 0]
    }
    setInitValue(value)
  }, [data])

  const formSchema = Yup.object().shape({
    isPersonRescue: Yup.boolean(),
    isMigrationRescue: Yup.boolean(),
    dates: Yup.array().of(Yup.date()).length(2),
    geoCoords: Yup.array().of(Yup.number()).length(2),
    numberOfPersonRescued: Yup.number()
      .nullable()
      .when('isPersonRescue', {
        is: true && isMissionFinished,
        then: schema => schema.nonNullable().required(),
        otherwise: schema => schema.nullable()
      }),
    numberOfDeaths: Yup.number()
      .nullable()
      .when('isPersonRescue', {
        is: true && isMissionFinished,
        then: schema => schema.nonNullable().required(),
        otherwise: schema => schema.nullable()
      }),
    nbOfVesselsTrackedWithoutIntervention: Yup.number()
      .nullable()
      .when('isMigrationRescue', {
        is: true && isMissionFinished,
        then: schema => schema.nonNullable().required(),
        otherwise: schema => schema.nullable()
      }),
    nbAssistedVesselsReturningToShore: Yup.number()

      .nullable()
      .when('isMigrationRescue', {
        is: true && isMissionFinished,
        then: schema => schema.nonNullable().required(),
        otherwise: schema => schema.nullable()
      })
  })

  const handleSubmit = async (value: ActionDataInput): Promise<void> => {
    if (isEqual(value, initValue)) return

    const { dates, geoCoords, ...newData } = value
    const latitude = geoCoords[0]
    const longitude = geoCoords[1]

    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    const isVesselRescue = value.rescueType === RescueType.VESSEL
    const isPersonRescue = value.rescueType === RescueType.PERSON

    const data: ActionRescue = {
      ...newData,
      startDateTimeUtc,
      endDateTimeUtc,
      longitude,
      latitude,
      isVesselRescue,
      isPersonRescue
    }
    setInitValue(value)
    onChange({ ...action, data: [data] })
  }
  return (
    <form style={{ width: '100%' }} data-testid={'action-nautical-event-form'}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true} validationSchema={formSchema}>
          <>
            <FormikEffect onChange={nextValues => handleSubmit(nextValues as ActionDataInput)} />
            <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                  <Stack.Item grow={1}>
                    <MissionActionFormikDateRangePicker name="dates" />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <MissionActionFormikCoordinateInputDMD name={'geoCoords'} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FormikTextInput
                  isLight={true}
                  name="locationDescription"
                  label="Précision concernant la localisation"
                />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FormikMultiRadio label="" name="rescueType" options={RESCUE_TYPE_OPTIONS} />
              </Stack.Item>
            </Stack>

            {initValue.rescueType === RescueType.VESSEL && (
              <Stack
                direction="column"
                spacing="2rem"
                alignItems="flex-start"
                style={{ width: '100%', marginTop: '35px' }}
              >
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="row" alignItems="center" spacing={'0.5rem'}>
                    <Stack.Item>
                      <FormikToggle size="sm" label="" isLight={true} name="operationFollowsDEFREP" />
                    </Stack.Item>
                    <Stack.Item alignSelf="flex-end">
                      <ToggleLabel>Opération suivie (DEFREP)</ToggleLabel>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>

                <Stack.Item style={{ marginBottom: 15 }}>
                  <FormikCheckbox
                    isLight
                    readOnly={false}
                    name="isVesselNoticed"
                    style={{ marginBottom: 8 }}
                    label="Le navire a été mis en demeure avant intervention"
                  />
                  <FormikCheckbox isLight readOnly={false} name="isVesselTowed" label="Le navire a été remorqué" />
                </Stack.Item>
              </Stack>
            )}

            {initValue.rescueType === RescueType.PERSON && (
              <Stack>
                <Stack.Item>
                  <Stack style={{ width: '100%', marginBottom: 25, marginTop: 25 }}>
                    <Stack.Item>
                      <MissionActionFormikNumberInput
                        style={{ marginRight: 10 }}
                        name="numberOfPersonRescued"
                        label="Nb de personnes secourues"
                      />
                    </Stack.Item>
                    <Stack.Item>
                      <MissionActionFormikNumberInput
                        name="numberOfDeaths"
                        label="Nb de personnes disparues / décédées"
                      />
                    </Stack.Item>
                  </Stack>
                  <Stack.Item>
                    <FormikCheckbox
                      isLight
                      readOnly={false}
                      style={{ marginBottom: 25 }}
                      name="isInSRRorFollowedByCROSSMRCC"
                      label="Opération en zone SRR ou suivie par un CROSS / MRCC"
                    />
                  </Stack.Item>
                </Stack.Item>
              </Stack>
            )}

            <Stack>
              <Stack.Item style={{ width: '100%' }}>
                <FormikTextarea label="Observations" isLight={true} name="observations" data-testid="observations" />
              </Stack.Item>
            </Stack>

            {initValue.rescueType === RescueType.PERSON && (
              <Stack direction={'column'} spacing={'1rem'} alignItems={'flex-start'}>
                <Stack.Item style={{ width: '100%' }}>
                  <Divider style={{ backgroundColor: THEME.color.charcoal }} />
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="row" alignItems="center" spacing={'0.5rem'}>
                    <Stack.Item>
                      <FormikToggle label="" size="sm" name="isMigrationRescue" />
                    </Stack.Item>
                    <Stack.Item alignSelf="flex-end">
                      <Text as={'h3'} weight={'medium'}>
                        Sauvetage dans le cadre d'un phénomène migratoire
                      </Text>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>

                <Stack.Item style={{ width: '50%', maxWidth: '50%' }}>
                  <MissionActionFormikNumberInput
                    disabled={!initValue?.isMigrationRescue}
                    isRequired={!!initValue?.isMigrationRescue}
                    name="nbOfVesselsTrackedWithoutIntervention"
                    label="Nb d'embarcations suivies sans nécessité d'intervention"
                  />
                </Stack.Item>
                <Stack.Item style={{ width: '50%', maxWidth: '50%' }}>
                  <MissionActionFormikNumberInput
                    disabled={!initValue?.isMigrationRescue}
                    isRequired={!!initValue?.isMigrationRescue}
                    name="nbAssistedVesselsReturningToShore"
                    label="Nb d'embarcations assistées pour un retour à terre"
                  />
                </Stack.Item>
              </Stack>
            )}
          </>
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemRescue
