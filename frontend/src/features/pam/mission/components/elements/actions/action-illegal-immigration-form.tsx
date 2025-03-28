import DateRangePicker from '@common/components/elements/dates/daterange-picker.tsx'
import { CoordinateInputDMD } from '@common/components/ui/coordonates-input-dmd.tsx'
import { Action, ActionIllegalImmigration } from '@common/types/action-types.ts'
import { useAction } from '@features/pam/mission/hooks/action/use-action.tsx'
import { Coordinates, DateRange, NumberInput, Textarea, THEME } from '@mtes-mct/monitor-ui'
import { isEqual } from 'lodash'
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
  const { getError } = useAction<ActionIllegalImmigration>()

  const { data: navAction, loading, error } = useActionById(actionId, missionId, action.source, action.type)
  const [mutateIllegalImmigration] = useAddOrUpdateIllegalImmigration()
  const [deleteIllegalImmigration] = useDeleteIllegalImmigration()

  const [observationsValue, setObservationsValue] = useState<string | undefined>(undefined)

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
      await onChange(observationsValue)('observations')
    }

    const onChange = (value: any) => async (field: string) => {
      let updatedField: {}
      if (field === 'dates') {
        const startDateTimeUtc = value[0]
        const endDateTimeUtc = value[1]
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
                  selectedRange={[navAction.startDateTimeUtc, navAction.endDateTimeUtc]}
                  error={!navAction.startDateTimeUtc && !navAction.endDateTimeUtc ? 'error' : undefined}
                  label="Date et heure de début et de fin"
                  withTime={true}
                  isCompact={true}
                  isLight={true}
                  onChange={async (nextValue?: DateRange) => {
                    await onChange(nextValue)('dates')
                  }}
                />
              </Stack.Item>
            </Stack>
          </Stack.Item>

          <Stack.Item style={{ width: '100%' }}>
            <CoordinateInputDMD
              label={"Lieu de l'opération"}
              name={'geoCoords'}
              defaultValue={[actionData?.latitude as any, actionData?.longitude as any]}
              isLight={true}
              disabled={false}
              onChange={async (nextCoordinates?: Coordinates, prevCoordinates?: Coordinates) => {
                if (!isEqual(nextCoordinates, prevCoordinates)) {
                  await onChange(nextCoordinates)('geoCoords')
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
                  error={getError(actionData, isMissionFinished, 'nbOfInterceptedVessels')}
                  onChange={(nextValue?: number) => onChange(nextValue)('nbOfInterceptedVessels')}
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
                      error={getError(actionData, isMissionFinished, 'nbOfInterceptedMigrants')}
                      onChange={(nextValue?: number) => onChange(nextValue)('nbOfInterceptedMigrants')}
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
                      error={getError(actionData, isMissionFinished, 'nbOfSuspectedSmugglers')}
                      onChange={(nextValue?: number) => onChange(nextValue)('nbOfSuspectedSmugglers')}
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
