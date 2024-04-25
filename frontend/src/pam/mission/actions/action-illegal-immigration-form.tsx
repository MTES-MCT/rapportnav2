import React, { useEffect, useState } from 'react'
import {
  Accent,
  Button, Coordinates, CoordinatesFormat, CoordinatesInput,
  DatePicker, DateRangePicker,
  Icon, Label, NumberInput,
  Size,
  Textarea, TextInput, THEME
} from '@mtes-mct/monitor-ui'
import { Action, ActionIllegalImmigration } from '../../../types/action-types'
import { Stack } from 'rsuite'
import Text from '../../../ui/text'
import { formatDateTimeForFrenchHumans } from '../../../utils/dates.ts'
import { useNavigate, useParams } from 'react-router-dom'
import omit from 'lodash/omit'
import useActionById from "./use-action-by-id.tsx";
import useAddOrUpdateIllegalImmigration from '../others/illegal-immigration/use-add-illegal-immigration.tsx'
import { isEqual } from 'lodash'
import useDeleteIllegalImmigration from '../others/illegal-immigration/use-delete-illegal-immigration.tsx'

interface ActionIllegalImmigrationFormProps {
  action: Action
}

const ActionIllegalImmigrationForm: React.FC<ActionIllegalImmigrationFormProps> = ({action}) => {
  const navigate = useNavigate()
  const {missionId, actionId} = useParams()

  const {data: navAction, loading, error} = useActionById(actionId, missionId, action.source, action.type)
  const [mutateIllegalImmigration] = useAddOrUpdateIllegalImmigration()
  const [deleteIllegalImmigration] = useDeleteIllegalImmigration()

  const [observationsValue, setObservationsValue] = useState<string | undefined>(undefined)

  useEffect(() => {
    setObservationsValue(navAction?.data?.observations)
  }, [navAction])

  if (loading) {
    return (
      <div>Chargement...</div>
    )
  }
  if (error) {
    return (
      <div>error</div>
    )
  }
  if (navAction) {
    const actionData = navAction?.data as unknown as ActionIllegalImmigration

    const handleObservationsChange = (nextValue?: string) => {
      setObservationsValue(nextValue)
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
      }
      else if (field === 'geoCoords') {
        updatedField = {
          latitude: value[0],
          longitude: value[1]
        }
      }
      else {
        updatedField = {
          [field]: value
        }
      }

      const updatedData = {
        missionId: missionId,
        ...omit(actionData, [
          '__typename',
        ]),
        startDateTimeUtc: navAction.startDateTimeUtc,
        endDateTimeUtc: navAction.endDateTimeUtc,
        ...updatedField
      }


      await mutateIllegalImmigration({ variables: { illegalImmigrationAction: updatedData } })
    }

    const deleteAction = async () => {
      await deleteIllegalImmigration({
        variables: {
          id: action.id!
        }
      })
      navigate(`/pam/missions/${missionId}`)
    }

    return (
      <form style={{width: '100%'}} data-testid={"action-nautical-event-form"}>
        <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{width: '100%'}}>
          {/* TITLE AND BUTTONS */}
          <Stack.Item style={{width: '100%'}}>
            <Stack direction="row" spacing="0.5rem" style={{width: '100%'}}>
              <Stack.Item grow={2}>
                <Stack direction="column" alignItems="flex-start">
                  <Stack.Item>
                    <Text as="h2" weight="bold">
                      Lutte contre l'immigration irrégulière {actionData.startDateTimeUtc && `(${formatDateTimeForFrenchHumans(actionData.startDateTimeUtc)})`}
                    </Text>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item>
                <Stack direction="row" spacing="0.5rem">
                  <Stack.Item>
                    <Button accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Duplicate}
                            disabled>
                      Dupliquer
                    </Button>
                  </Stack.Item>
                  <Stack.Item>
                    <Button
                      accent={Accent.PRIMARY}
                      size={Size.SMALL}
                      Icon={Icon.Delete}
                      onClick={deleteAction}
                      data-testid={'deleteButton'}
                    >
                      Supprimer
                    </Button>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          </Stack.Item>

          <Stack.Item style={{width: '100%'}}>
            <Stack direction="row" spacing="0.5rem" style={{width: '100%'}}>
              <Stack.Item grow={1}>
                <DateRangePicker
                  name="dates"
                  // defaultValue={[navAction.startDateTimeUtc ?? formatDateForServers(toLocalISOString()), navAction.endDateTimeUtc ?? formatDateForServers(new Date() as any)]}
                  defaultValue={navAction.startDateTimeUtc && navAction.endDateTimeUtc ? [navAction.startDateTimeUtc, navAction.endDateTimeUtc] : undefined}
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

          <Stack.Item style={{width: '100%'}}>
            <CoordinatesInput
              label={"Lieu de l'opération"}
              name={"geoCoords"}
              defaultValue={[
                actionData?.latitude  as any,
                actionData?.longitude  as any
              ]}
              coordinatesFormat={CoordinatesFormat.DEGREES_MINUTES_DECIMALS}
              // label="Lieu du contrôle"
              isLight={true}
              disabled={false}
              onChange={ async (nextCoordinates?: Coordinates, prevCoordinates?: Coordinates) => {
                if (!isEqual(nextCoordinates, prevCoordinates)) {
                  await onChange('geoCoords', nextCoordinates)
                }
              }}
            />
          </Stack.Item>
        </Stack>

        <Stack
          direction="row"
          alignItems="flex-start"
          spacing="1rem"
          style={{width: '100%', backgroundColor: THEME.color.gainsboro, padding: '0.5rem'}}
        >
          <Stack.Item style={{flex: 1}}>
            <NumberInput
              label="Nb de navires/embarcations interceptées"
              name="nbOfInterceptedVessels"
              role="nbOfInterceptedVessels"
              placeholder="0"
              isLight={true}
              value={actionData?.nbOfInterceptedVessels}
              onChange={(nextValue?: number) => onChange('nbOfInterceptedVessels', nextValue)}
            />
          </Stack.Item>
          <Stack.Item style={{flex: 1}}>
            <NumberInput
              label="Nb de migrants interceptés"
              name="nbOfInterceptedMigrants"
              role="nbOfInterceptedMigrants"
              placeholder="0"
              isLight={true}
              value={actionData?.nbOfInterceptedMigrants}
              onChange={(nextValue?: number) => onChange('nbOfInterceptedMigrants', nextValue)}
            />
          </Stack.Item>
          <Stack.Item style={{flex: 1}}>
            <NumberInput
              label="Nb de passeurs présumés"
              name="nbOfSuspectedSmugglers"
              role="nbOfSuspectedSmugglers"
              placeholder="0"
              isLight={true}
              value={actionData?.nbOfSuspectedSmugglers}
              onChange={(nextValue?: number) => onChange('nbOfSuspectedSmugglers', nextValue)}
            />
          </Stack.Item>
        </Stack>

        <Stack>
          <Stack.Item style={{width: '100%'}}>
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
      </form>
    )
  }
  return null
}

export default ActionIllegalImmigrationForm
