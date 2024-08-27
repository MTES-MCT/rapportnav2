import {
  Checkbox,
  Coordinates,
  CoordinatesFormat,
  CoordinatesInput,
  DateRangePicker,
  Icon,
  Textarea,
  THEME,
  Toggle,
  ToggleProps
} from '@mtes-mct/monitor-ui'
import { isEqual } from 'lodash'
import omit from 'lodash/omit'
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { Action, ActionAntiPollution } from '@common/types/action-types.ts'
import Text, { TextProps } from '../../../../../common/components/ui/text.tsx'
import useAddOrUpdateAntiPollution from '../../../hooks/anti-pollution/use-add-anti-pollution.tsx'
import useDeleteAntiPollution from '../../../hooks/anti-pollution/use-delete-anti-pollution.tsx'
import useIsMissionFinished from '../../../hooks/use-is-mission-finished.tsx'
import ActionHeader from './action-header.tsx'
import useActionById from '../../../hooks/use-action-by-id.tsx'

const ToggleLabel = styled((props: Omit<TextProps, 'as'>) => <Text {...props} as="h3" />)(({ theme }) => ({
  color: theme.color.gunMetal,
  fontWeight: 500
}))

const StyledToggle = styled((props: Omit<ToggleProps, 'label'>) => (
  <Toggle {...props} label="" isLight isLabelHidden readOnly={false} />
))(({ theme }) => ({
  marginRight: 8
}))

interface ActionAntiPollutionFormProps {
  action: Action
}

const ActionAntiPollutionForm: React.FC<ActionAntiPollutionFormProps> = ({ action }) => {
  const navigate = useNavigate()
  const { missionId, actionId } = useParams()
  const isMissionFinished = useIsMissionFinished(missionId)

  const { data: navAction, loading, error } = useActionById(actionId, missionId, action.source, action.type)
  const [mutateAntiPollution] = useAddOrUpdateAntiPollution()
  const [deleteAntiPollution] = useDeleteAntiPollution()

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
    const actionData = navAction?.data as unknown as ActionAntiPollution

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

      await mutateAntiPollution({ variables: { antiPollutionAction: updatedData } })
    }

    const deleteAction = async () => {
      await deleteAntiPollution({
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
              title={'Opération de lutte anti-pollution'}
              date={actionData.startDateTimeUtc}
              onDelete={deleteAction}
              showButtons={true}
              showStatus={true}
              actionSource={action.source}
              isMissionFinished={isMissionFinished}
              completenessForStats={navAction.completenessForStats}
            />
          </Stack.Item>

          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
              <Stack.Item alignSelf="baseline">
                <Icon.Info color={THEME.color.charcoal} size={20} />
              </Stack.Item>
              <Stack.Item>
                <Text as="h4" weight="normal" fontStyle="italic">
                  Ces actions conernent des opérations menées en coordination avec le CROSS lors de la suspicion /
                  détection / signalement de pollution en mer (rejets illicites...)
                  <br />
                  Les contrôles effectués de manière autonomes sont à rapporter au CACEM
                  <br />
                  En cas de doute, appelez le CACEM
                </Text>
              </Stack.Item>
            </Stack>
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
            <Stack direction={'column'} spacing={'0.5rem'}>
              <Stack.Item style={{ width: '100%', display: 'flex', flexDirection: 'row', alignItems: 'end' }}>
                <StyledToggle
                  name="isSimpleBrewingOperationDone"
                  checked={actionData?.isSimpleBrewingOperationDone}
                  onChange={async nextValue => {
                    await onChange('isSimpleBrewingOperationDone', nextValue)
                  }}
                  data-testid="action-antipol-simple-brewing-operation"
                />
                <ToggleLabel>Opération de brassage simple effectuée</ToggleLabel>
              </Stack.Item>
              <Stack.Item
                style={{ width: '100%', display: 'flex', flexDirection: 'row', alignItems: 'end', marginBottom: 32 }}
              >
                <StyledToggle
                  name="isAntiPolDeviceDeployed"
                  checked={actionData?.isAntiPolDeviceDeployed}
                  onChange={async nextValue => {
                    await onChange('isAntiPolDeviceDeployed', nextValue)
                  }}
                  data-testid="action-antipol-device-deployed"
                />
                <ToggleLabel>Mise en place d'un dispositif ANTIPOL (dispersant, barrage, etc...)</ToggleLabel>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Checkbox
                  readOnly={false}
                  isLight
                  name="detectedPollution"
                  label="Pollution détectée"
                  checked={actionData?.detectedPollution}
                  onChange={async nextValue => {
                    await onChange('detectedPollution', nextValue)
                  }}
                />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Checkbox
                  readOnly={false}
                  isLight
                  name="pollutionObservedByAuthorizedAgent"
                  label="Pollution constatée par un agent habilité"
                  checked={actionData?.pollutionObservedByAuthorizedAgent}
                  onChange={async nextValue => {
                    await onChange('pollutionObservedByAuthorizedAgent', nextValue)
                  }}
                />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Checkbox
                  readOnly={false}
                  isLight
                  name="diversionCarriedOut"
                  label="Déroutement effectué"
                  checked={actionData?.diversionCarriedOut}
                  onChange={async nextValue => {
                    await onChange('diversionCarriedOut', nextValue)
                  }}
                />
              </Stack.Item>
            </Stack>
          </Stack.Item>

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
      </form>
    )
  }
  return null
}

export default ActionAntiPollutionForm
