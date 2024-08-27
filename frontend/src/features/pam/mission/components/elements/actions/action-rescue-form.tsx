import {
  Checkbox,
  Coordinates,
  CoordinatesFormat,
  CoordinatesInput,
  DateRangePicker,
  Icon,
  MultiRadio,
  NumberInput,
  Textarea,
  TextInput,
  THEME
} from '@mtes-mct/monitor-ui'
import { isEqual } from 'lodash'
import omit from 'lodash/omit'
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Divider, Stack, Toggle } from 'rsuite'
import { Action, ActionRescue } from '@common/types/action-types.ts'
import Text from '../../../../../common/components/ui/text.tsx'
import { RESCUE_TYPE_OPTIONS } from '../../../utils/control-utils.ts'
import useAddUpdateRescue from '../../../hooks/rescues/use-add-update-rescue.tsx'
import useDeleteRescue from '../../../hooks/rescues/use-delete-rescue.tsx'
import useIsMissionFinished from '../../../hooks/use-is-mission-finished.tsx'
import ActionHeader from './action-header.tsx'
import useActionById from '../../../hooks/use-action-by-id.tsx'

interface ActionRescueFormProps {
  action: Action
}

const ActionRescueForm: React.FC<ActionRescueFormProps> = ({ action }) => {
  const navigate = useNavigate()
  const { missionId, actionId } = useParams()
  const isMissionFinished = useIsMissionFinished(missionId)

  const [showVesselStack, setShowVesselStack] = useState(false)
  const [showPersonStack, setShowPersonStack] = useState(true)

  const { data: navAction, loading, error } = useActionById(actionId, missionId, action.source, action.type)
  const [mutateRescue] = useAddUpdateRescue()
  const [deleteRescue] = useDeleteRescue()

  const [observationsValue, setObservationsValue] = useState<string | undefined>(undefined)
  const [locationObservationValue, setLocationObservationValue] = useState<string | undefined>(undefined)

  useEffect(() => {
    setObservationsValue(navAction?.data?.observations)
    setLocationObservationValue(navAction?.data?.locationDescription)
  }, [navAction])

  if (loading) {
    return <div>Chargement...</div>
  }
  if (error) {
    return <div>error</div>
  }
  if (navAction) {
    const actionData = navAction?.data as ActionRescue

    const handleObservationsChange = (nextValue?: string) => {
      setObservationsValue(nextValue)
    }

    const handleLocationObservationChange = (nextValue?: string) => {
      setLocationObservationValue(nextValue)
    }

    const handleLocationDescriptionBlur = async () => {
      await onChange('locationDescription', locationObservationValue)
    }

    const handleObservationsBlur = async () => {
      await onChange('observations', observationsValue)
    }

    const onChange = async (field: string, value: any) => {
      let updatedField: {}
      if (field === 'dates') {
        const startDateTimeUtc = value[0].toISOString()
        const endDateTimeUtc = value[1].toISOString()
        updatedField = {
          startDateTimeUtc,
          endDateTimeUtc
        }
      } else if (field === 'geoCoords') {
        updatedField = {
          latitude: value[0],
          longitude: value[1]
        }
      } else if (field === 'isVesselRescue') {
        updatedField = {
          isVesselRescue: value,
          isPersonRescue: !value
        }
      } else if (field === 'isPersonRescue') {
        updatedField = {
          isVesselRescue: !value,
          isPersonRescue: value
        }
      } else if (field === 'isMigrationRescue' && value === false) {
        updatedField = {
          [field]: value,
          nbAssistedVesselsReturningToShore: undefined,
          nbOfVesselsTrackedWithoutIntervention: undefined
        }
      } else {
        updatedField = {
          [field]: value
        }
      }

      const updatedData = {
        missionId: missionId,
        ...omit(actionData, ['__typename']),
        startDateTimeUtc: navAction.startDateTimeUtc,
        endDateTimeUtc: navAction.endDateTimeUtc,
        ...updatedField
      }

      await mutateRescue({ variables: { rescueAction: updatedData } })
    }

    const toggleRescue = async (isChecked?: boolean) => {
      if (isChecked) {
        setShowVesselStack(true)
        setShowPersonStack(false)
        await onChange('isVesselRescue', true)
      } else {
        setShowVesselStack(false)
        setShowPersonStack(true)
        await onChange('isPersonRescue', true)
      }
    }

    const deleteAction = async () => {
      await deleteRescue({
        variables: {
          id: action.id
        }
      })
      navigate(`/pam/missions/${missionId}`)
    }

    return (
      <form style={{ width: '100%' }} data-testid={'action-rescue-form'}>
        <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
          {/* TITLE AND BUTTONS */}
          <Stack.Item style={{ width: '100%' }}>
            <ActionHeader
              icon={Icon.Rescue}
              title={'Assistance et sauvetage'}
              date={actionData.startDateTimeUtc}
              onDelete={deleteAction}
              showButtons={true}
              showStatus={true}
              isMissionFinished={isMissionFinished}
              completenessForStats={navAction.completenessForStats}
            />
          </Stack.Item>

          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
              <Stack.Item grow={1}>
                <DateRangePicker
                  name="dates"
                  error={!navAction.startDateTimeUtc && !navAction.endDateTimeUtc ? 'error' : undefined}
                  isErrorMessageHidden={true}
                  isRequired={true}
                  defaultValue={
                    navAction.startDateTimeUtc && navAction.endDateTimeUtc
                      ? [navAction.startDateTimeUtc, navAction.endDateTimeUtc]
                      : undefined
                  }
                  label="Date et heure de début et de fin"
                  withTime={true}
                  isCompact={true}
                  isLight={true}
                  onChange={async (nextValue?: [Date, Date] | [string, string]) => {
                    await onChange('dates', nextValue)
                  }}
                />
              </Stack.Item>
            </Stack>
          </Stack.Item>

          <Stack.Item style={{ width: '100%' }}>
            <CoordinatesInput
              label={"Lieu de l'opération"}
              isRequired={true}
              error={!actionData.latitude && !actionData.longitude ? 'error' : undefined}
              isErrorMessageHidden={true}
              name={'geoCoords'}
              defaultValue={[actionData?.latitude as any, actionData?.longitude as any]}
              coordinatesFormat={CoordinatesFormat.DEGREES_MINUTES_DECIMALS}
              isLight={true}
              disabled={false}
              onChange={async (nextCoordinates?: Coordinates, prevCoordinates?: Coordinates) => {
                if (!isEqual(nextCoordinates, prevCoordinates)) {
                  await onChange('geoCoords', nextCoordinates)
                }
              }}
            />
          </Stack.Item>

          <Stack.Item style={{ width: '100%' }}>
            <TextInput
              label={'Précision concernant la localisation'}
              name={'locationDescription'}
              isLight={true}
              value={locationObservationValue}
              onChange={handleLocationObservationChange}
              onBlur={handleLocationDescriptionBlur}
            />
          </Stack.Item>

          <Stack.Item style={{ width: '100%' }}>
            <MultiRadio
              value={actionData?.isVesselRescue}
              label=""
              name="rescue-type"
              onChange={nextOptionValue => toggleRescue(nextOptionValue)}
              options={RESCUE_TYPE_OPTIONS}
            />
          </Stack.Item>
        </Stack>

        {showVesselStack && (
          <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%', marginTop: '35px' }}>
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" alignItems="center" spacing={'0.5rem'}>
                <Stack.Item>
                  <Toggle
                    checked={actionData?.operationFollowsDEFREP}
                    size="sm"
                    onChange={(checked: boolean) => onChange('operationFollowsDEFREP', checked)}
                  />
                </Stack.Item>
                <Stack.Item alignSelf="flex-end">
                  <Text as={'h3'} weight={'medium'}>
                    Opération suivie (DEFREP)
                  </Text>
                </Stack.Item>
              </Stack>
            </Stack.Item>

            <Stack.Item style={{ marginBottom: 15 }}>
              <Checkbox
                readOnly={false}
                isLight
                name="isVesselNoticed"
                label="Le navire a été mis en demeure avant intervention"
                checked={actionData?.isVesselNoticed}
                style={{ marginBottom: 8 }}
                onChange={async nextValue => {
                  await onChange('isVesselNoticed', nextValue)
                }}
              />
              <Checkbox
                readOnly={false}
                isLight
                name="isVesselTowed"
                label="Le navire a été remorqué"
                checked={actionData?.isVesselTowed}
                onChange={async nextValue => {
                  await onChange('isVesselTowed', nextValue)
                }}
              />
            </Stack.Item>
          </Stack>
        )}

        {showPersonStack && (
          <Stack>
            <Stack.Item>
              <Stack style={{ width: '100%', marginBottom: 25, marginTop: 25 }}>
                <Stack.Item>
                  <NumberInput
                    isRequired={true}
                    error={
                      actionData.isPersonRescue &&
                      (actionData.numberPersonsRescued === undefined || actionData.numberPersonsRescued === null)
                        ? 'error'
                        : undefined
                    }
                    isErrorMessageHidden={true}
                    style={{ marginRight: 10 }}
                    label="Nb de personnes secourues"
                    name="numberOfPersonRescued"
                    placeholder="0"
                    isLight={true}
                    value={actionData?.numberPersonsRescued}
                    onChange={async nextValue => {
                      await onChange('numberPersonsRescued', nextValue)
                    }}
                  />
                </Stack.Item>
                <Stack.Item>
                  <NumberInput
                    isRequired={true}
                    error={
                      actionData.isPersonRescue &&
                      (actionData.numberOfDeaths === undefined || actionData.numberOfDeaths === null)
                        ? 'error'
                        : undefined
                    }
                    isErrorMessageHidden={true}
                    label="Nb de personnes disparues / décédées"
                    name="numberOfDeaths"
                    placeholder="0"
                    isLight={true}
                    value={actionData?.numberOfDeaths}
                    onChange={async nextValue => {
                      await onChange('numberOfDeaths', nextValue)
                    }}
                  />
                </Stack.Item>
              </Stack>

              <Stack.Item>
                <Checkbox
                  readOnly={false}
                  isLight
                  name="isInSRRorFollowedByCROSSMRCC"
                  label="Opération en zone SRR ou suivie par un CROSS / MRCC"
                  checked={actionData?.isInSRRorFollowedByCROSSMRCC}
                  style={{ marginBottom: 25 }}
                  onChange={async nextValue => {
                    await onChange('isInSRRorFollowedByCROSSMRCC', nextValue)
                  }}
                />
              </Stack.Item>
            </Stack.Item>
          </Stack>
        )}

        <Stack>
          <Stack.Item style={{ width: '100%' }}>
            <Textarea
              label="Observations"
              value={observationsValue}
              isLight={true}
              name="observations"
              data-testid="observations"
              onChange={handleObservationsChange}
              onBlur={handleObservationsBlur}
            />
          </Stack.Item>
        </Stack>

        {showPersonStack && (
          <Stack direction={'column'} spacing={'1rem'} alignItems={'flex-start'}>
            <Stack.Item style={{ width: '100%' }}>
              <Divider style={{ backgroundColor: THEME.color.charcoal }} />
            </Stack.Item>

            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" alignItems="center" spacing={'0.5rem'}>
                <Stack.Item>
                  {/* TODO add Toggle component to monitor-ui */}
                  <Toggle
                    checked={!!actionData?.isMigrationRescue}
                    size="sm"
                    onChange={(checked: boolean) => onChange('isMigrationRescue', checked)}
                  />
                </Stack.Item>
                <Stack.Item alignSelf="flex-end">
                  <Text as={'h3'} weight={'medium'}>
                    Sauvetage dans le cadre d'un phénomène migratoire
                  </Text>
                </Stack.Item>
              </Stack>
            </Stack.Item>

            <Stack.Item style={{ width: '50%', maxWidth: '50%' }}>
              <NumberInput
                label="Nb d'embarcations suivies sans nécessité d'intervention"
                isRequired={!!actionData?.isMigrationRescue}
                error={
                  !actionData.nbOfVesselsTrackedWithoutIntervention && actionData?.isMigrationRescue
                    ? 'error'
                    : undefined
                }
                isErrorMessageHidden={true}
                name="nbOfVesselsTrackedWithoutIntervention"
                placeholder="0"
                isLight={true}
                value={actionData?.nbOfVesselsTrackedWithoutIntervention}
                onChange={async nextValue => {
                  await onChange('nbOfVesselsTrackedWithoutIntervention', nextValue)
                }}
                disabled={!actionData?.isMigrationRescue}
              />
            </Stack.Item>

            <Stack.Item style={{ width: '50%', maxWidth: '50%' }}>
              <NumberInput
                label="Nb d'embarcations assistées pour un retour à terre"
                isRequired={!!actionData?.isMigrationRescue}
                error={
                  !actionData.nbAssistedVesselsReturningToShore && actionData?.isMigrationRescue ? 'error' : undefined
                }
                isErrorMessageHidden={true}
                name="nbAssistedVesselsReturningToShore"
                placeholder="0"
                isLight={true}
                value={actionData?.nbAssistedVesselsReturningToShore}
                onChange={async nextValue => {
                  await onChange('nbAssistedVesselsReturningToShore', nextValue)
                }}
                disabled={!actionData?.isMigrationRescue}
              />
            </Stack.Item>
          </Stack>
        )}
      </form>
    )
  }
  return null
}

export default ActionRescueForm
