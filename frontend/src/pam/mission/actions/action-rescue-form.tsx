import React, { useEffect, useState } from 'react'
import {
  Accent,
  Button, Checkbox, Coordinates, CoordinatesFormat, CoordinatesInput,
  DateRangePicker,
  Icon, Label, MultiRadio, NumberInput, OptionValue, Radio,
  Size,
  Textarea,
  TextInput,
  THEME
} from '@mtes-mct/monitor-ui'
import { Action, ActionRescue } from '../../../types/action-types'
import { FlexboxGrid, Stack, Toggle } from 'rsuite'
import Text from '../../../ui/text'
import { useNavigate, useParams } from 'react-router-dom'
import useActionById from "./use-action-by-id.tsx";
import { RESCUE_TYPE_OPTIONS } from '../controls/utils.ts'
import omit from 'lodash/omit'
import useAddUpdateRescue from '../rescues/use-add-update-rescue.tsx'
import { isEqual } from 'lodash'

interface ActionRescueFormProps {
  action: Action
}

const ActionRescueForm: React.FC<ActionRescueFormProps> = ({action}) => {
  const navigate = useNavigate()
  const {missionId, actionId} = useParams()

  const [outputValueWithBoolean, setOutputValueWithBoolean] = useState<boolean | undefined>(undefined);
  const [showVesselStack, setShowVesselStack] = useState(false);
  const [showPersonStack, setShowPersonStack] = useState(false);

  const {data: navAction, loading, error} = useActionById(actionId, missionId, action.source, action.type)
  const [mutateRescue] = useAddUpdateRescue()
//  const [deleteNote] = useDeleteNote()

  const [observationsValue, setObservationsValue] = useState<string | undefined>(undefined)

  const [locationObservationValue, setLocationObservationValue] = useState<string | undefined>(undefined)

  useEffect(() => {
    setObservationsValue(navAction?.data?.observations)
    setLocationObservationValue(navAction?.data?.locationDescription)
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

      if (field === 'dates') {
        let updatedField = {
          startDateTimeUtc: value[0].toISOString(),
          endDateTimeUtc: value[1].toISOString(),
          missionId: missionId,
          ...omit(actionData,
            '__typename'),
        }
        await mutateRescue({variables: {rescueAction: updatedField}})
      }
      else if (field === 'geoCoords') {
        let updatedField = {
          latitude: value[0],
          longitude: value[1],
          missionId: missionId,
          ...omit(actionData,
            '__typename'),
        }
        await mutateRescue({variables: {rescueAction: updatedField}})
      }
      else {
        const updatedData = {
          missionId: missionId,
          ...omit(actionData, '__typename'),
          [field]: typeof value === 'string' ? value.trim() : value
        }
        await mutateRescue({variables: {rescueAction: updatedData}})
      }
    }

    const toggleRescue = (isChecked) => {
      setOutputValueWithBoolean(isChecked)
      if (isChecked) {
        setShowVesselStack(true)
        setShowPersonStack(false)
        onChange('isVesselRescue', true)
        onChange('isPersonRescue', false)
      } else {
        setShowVesselStack(false)
        setShowPersonStack(true)
        onChange('isPersonRescue', true)
        onChange('isVesselRescue', false)
      }
    }

    const deleteAction = async () => {
     /** await deleteNote({
        variables: {
          id: action.id!
        }
      })**/
      navigate(`/pam/missions/${missionId}`)
    }

    return (
      <form style={{width: '100%'}} data-testid={"action-rescue-form"}>
        <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{width: '100%'}}>
          {/* TITLE AND BUTTONS */}
          <Stack.Item style={{width: '100%'}}>
            <Stack direction="row" spacing="0.5rem" style={{width: '100%'}}>
              <Stack.Item alignSelf="baseline">
                <Icon.Note color={THEME.color.charcoal} size={20}/>
              </Stack.Item>
              <Stack.Item grow={2}>
                <Stack direction="column" alignItems="flex-start">
                  <Stack.Item>
                    <Text as="h2" weight="bold">
                      Assistance / sauvetage
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
                    <Button accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Delete}
                            onClick={deleteAction} data-testid={"deleteButton"}>
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
                  role={"ok"}
                  onChange={async (nextValue?: [Date, Date] | [string, string]) => {
                    await onChange('dates', nextValue)
                  }}
                />
              </Stack.Item>
            </Stack>
          </Stack.Item>

          <Stack.Item style={{width: '100%'}}>
            <Label>Lieu du contrôle</Label>
            <CoordinatesInput
              label={"lieu"}
              name={"geoCoords"}
              defaultValue={[
                actionData?.latitude ?? 0.0 as any,
                actionData?.longitude ?? 0.0 as any
              ]}
              coordinatesFormat={CoordinatesFormat.DEGREES_MINUTES_DECIMALS}
              // label="Lieu du contrôle"
               isLight={true}
              disabled={false}
              onChange={ (nextCoordinates?: Coordinates, prevCoordinates?: Coordinates) => {
                if (!isEqual(nextCoordinates, prevCoordinates)) {
                   onChange('geoCoords', nextCoordinates)
                }
              }}
            />
          </Stack.Item>

          <Stack.Item style={{width: '100%'}}>
            <TextInput label={"Précision concernant la localisation"}
                       name={"locationDescription"}
                       isLight={true}
                       value={locationObservationValue}
                       onChange={handleLocationObservationChange}
                       onBlur={handleLocationDescriptionBlur}
            />
          </Stack.Item>

          <Stack.Item style={{ width: '100%' }}>
            <MultiRadio
              value={actionData?.isVesselRescue ?? false}
              label=""
              name="myMultiRadioWithBooleans"
              onChange={nextOptionValue => toggleRescue(nextOptionValue)}
              options={RESCUE_TYPE_OPTIONS} />

          </Stack.Item>


        </Stack>
        {showVesselStack && (
          <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{width: '100%', marginTop:'35px'}} >
            <Stack.Item>
              <Label >
                <Toggle title={"test"}  /> Opération suivie (DEFREP)</Label>
            </Stack.Item>

            <Stack.Item style={{marginBottom: 15}}>
              <Checkbox
                readOnly={false}
                isLight
                name="isVesselNoticed"
                label="Le navire a été mis en demeure"
                checked={actionData?.isVesselNoticed}
                style={{marginBottom: 8}}
                onChange={ async (nextValue) => {
                  await onChange('isVesselNoticed', nextValue)
                }}
              />
              <Checkbox
                readOnly={false}
                isLight
                name="isVesselTowed"
                label="Le navire a été remorqué"
                checked={actionData?.isVesselTowed}
                onChange={ async (nextValue) => {
                  await onChange('isVesselTowed', nextValue)
                }}
              />
            </Stack.Item>
        </Stack>)}

        {showPersonStack && (
          <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{width: '100%', marginTop:'35px'}} >

            <Stack.Item>
              <Label >
                <Toggle title={"test"}  /> Opération suivie par le CROSS/MRCC</Label>
            </Stack.Item>

            <FlexboxGrid style={{flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: 30}}>
              <FlexboxGrid.Item colspan={12} style={{flex: 1, marginRight: 10}}>
                <NumberInput
                  label="Nb de personnes secourues"
                  name="numberOfPersonRescued"
                  placeholder="0"
                  isLight={true}
                  value={actionData?.numberPersonsRescued}
                  onChange={ async (nextValue) => {
                    await onChange('numberPersonsRescued', nextValue)
                  }}
                />
              </FlexboxGrid.Item>

              <FlexboxGrid.Item colspan={12} style={{flex: 1, marginRight: 10}}>
                <NumberInput
                  label="Nb de personnes disparues / décédées"
                  name="numberOfDeaths"
                  placeholder="0"
                  isLight={true}
                  value={actionData?.numberOfDeaths}
                  onChange={ async (nextValue) => {
                    await onChange('numberOfDeaths', nextValue)
                  }}
                />
              </FlexboxGrid.Item>
            </FlexboxGrid>
          </Stack>)
        }


        <Stack>
          <Stack.Item>
            <Textarea
              name="observations"
              label="Observations"
              value={observationsValue}
              onChange={handleObservationsChange}
              onBlur={handleObservationsBlur}
              isLight={true}
              style={{width:'100%', marginTop: 15}}
            />
          </Stack.Item>
        </Stack>
      </form>
    )
  }
  return null
}

export default ActionRescueForm
