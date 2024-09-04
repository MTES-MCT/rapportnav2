import { Action, ActionIllegalImmigration } from '@common/types/action-types.ts'
import {
  Coordinates,
  CoordinatesFormat,
  CoordinatesInput,
  DateRangePicker,
  NumberInput,
  Textarea,
  THEME
} from '@mtes-mct/monitor-ui'
import { isEqual, isNil } from 'lodash'
import omit from 'lodash/omit'
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Stack } from 'rsuite'
import useAddOrUpdateIllegalImmigration from '../../../hooks/illegal-immigration/use-add-illegal-immigration.tsx'
import useDeleteIllegalImmigration from '../../../hooks/illegal-immigration/use-delete-illegal-immigration.tsx'
import useActionById from '../../../hooks/use-action-by-id.tsx'
import useIsMissionFinished from '../../../hooks/use-is-mission-finished.tsx'
import ActionHeader from './action-header.tsx'

interface ActionIllegalImmigrationFormProps {
  action: Action
}

const ActionIllegalImmigrationForm: React.FC<ActionIllegalImmigrationFormProps> = ({ action }) => {
  const navigate = useNavigate()
  const { missionId, actionId } = useParams()
  const isMissionFinished = useIsMissionFinished(missionId)

  const { data: navAction, loading, error } = useActionById(actionId, missionId, action.source, action.type)
  const [mutateIllegalImmigration] = useAddOrUpdateIllegalImmigration()
  const [deleteIllegalImmigration] = useDeleteIllegalImmigration()

  const [observationsValue, setObservationsValue] = useState<string | undefined>(undefined)

  const getError = (data: ActionIllegalImmigration, key: keyof ActionIllegalImmigration) => {
    return isNil(data[key]) && isMissionFinished ? 'error' : undefined
  }

  useEffect(() => {
    setObservationsValue(navAction?.data?.observations)
  }, [navAction])

  if (loading) {
    return <div>Chargement...</div>
  }
  if (error) {
    return <div>error</div>
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
      } else if (field === 'geoCoords') {
        updatedField = {
          latitude: value[0],
          longitude: value[1]
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

      await mutateIllegalImmigration({ variables: { illegalImmigrationAction: updatedData } })
    }

    const deleteAction = async () => {
      await deleteIllegalImmigration({
        variables: {
          id: action.id
        }
      })
      navigate(`/pam/missions/${missionId}`)
    }

    return (
      <form style={{ width: '100%' }} data-testid={'action-nautical-event-form'}>
        <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
          {/* TITLE AND BUTTONS */}
          <Stack.Item style={{ width: '100%' }}>
            <ActionHeader
              icon={undefined}
              title={"Lutte contre l'immigration illégale"}
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
              name={'geoCoords'}
              defaultValue={[actionData?.latitude as any, actionData?.longitude as any]}
              coordinatesFormat={CoordinatesFormat.DEGREES_MINUTES_DECIMALS}
              // label="Lieu du contrôle"
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
            <Stack
              direction="column"
              alignItems="flex-start"
              spacing="1rem"
              style={{ width: '100%', backgroundColor: THEME.color.gainsboro, padding: '0.5rem' }}
            >
              <Stack.Item style={{ width: '100%' }}>
                <NumberInput
                  isRequired={true}
                  isErrorMessageHidden={true}
                  label="Nb de navires/embarcations interceptées"
                  name="nbOfInterceptedVessels"
                  role="nbOfInterceptedVessels"
                  placeholder="0"
                  isLight={true}
                  value={actionData?.nbOfInterceptedVessels}
                  error={getError(actionData, 'nbOfInterceptedVessels')}
                  onChange={(nextValue?: number) => onChange('nbOfInterceptedVessels', nextValue)}
                />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction={'row'} spacing={'1rem'}>
                  <Stack.Item style={{ flex: 1 }}>
                    <NumberInput
                      isRequired={true}
                      isErrorMessageHidden={true}
                      label="Nb de migrants interceptés"
                      name="nbOfInterceptedMigrants"
                      role="nbOfInterceptedMigrants"
                      placeholder="0"
                      isLight={true}
                      value={actionData?.nbOfInterceptedMigrants}
                      error={getError(actionData, 'nbOfInterceptedMigrants')}
                      onChange={(nextValue?: number) => onChange('nbOfInterceptedMigrants', nextValue)}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ flex: 1 }}>
                    <NumberInput
                      isRequired={true}
                      isErrorMessageHidden={true}
                      label="Nb de passeurs présumés"
                      name="nbOfSuspectedSmugglers"
                      role="nbOfSuspectedSmugglers"
                      placeholder="0"
                      isLight={true}
                      value={actionData?.nbOfSuspectedSmugglers}
                      error={getError(actionData, 'nbOfSuspectedSmugglers')}
                      onChange={(nextValue?: number) => onChange('nbOfSuspectedSmugglers', nextValue)}
                    />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          </Stack.Item>

          <Stack.Item style={{ width: '100%' }}>
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
          </Stack.Item>
        </Stack>
      </form>
    )
  }
  return null
}

export default ActionIllegalImmigrationForm
