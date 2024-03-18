import React, { useEffect, useState } from 'react'
import {
  Accent,
  Button,
  Coordinates,
  CoordinatesFormat,
  CoordinatesInput,
  DateRangePicker,
  Icon,
  Label,
  OptionValueType,
  Select,
  Size,
  Textarea,
  TextInput,
  THEME
} from '@mtes-mct/monitor-ui'
import { VesselTypeEnum } from '../../../types/mission-types'
import { Action, ActionControl } from '../../../types/action-types'
import { Stack } from 'rsuite'
import Text from '../../../ui/text'
import { formatDateTimeForFrenchHumans } from '../../../utils/dates.ts'
import omit from 'lodash/omit'
import { useNavigate, useParams } from 'react-router-dom'
import ControlAdministrativeForm from '../controls/control-administrative-form'
import ControlNavigationForm from '../controls/control-navigation-form'
import ControlGensDeMerForm from '../controls/control-gens-de-mer-form'
import ControlSecurityForm from '../controls/control-security-form'
import { controlMethodToHumanString, VESSEL_SIZE_OPTIONS, vesselTypeToHumanString } from '../controls/utils'
import useActionById from "./use-action-by-id.tsx";
import useAddOrUpdateControl from "./use-add-update-action-control.tsx";
import useDeleteActionControl from "./use-delete-action-control.tsx";
import { isEqual } from "lodash";

export interface ActionControlNavProps {
  action: Action
}

const ActionControlNav: React.FC<ActionControlNavProps> = ({action}) => {

  const navigate = useNavigate()
  const {missionId, actionId} = useParams()
  const [observationsValue, setObservationsValue] = useState<string | undefined>(undefined)
  const [identityControlledPersonValue, setIdentityControlledPersonValue] = useState<string | undefined>(undefined)
  const [vesselIdentifierValue, setVesselIdentifierValue] = useState<string | undefined>(undefined)

  const [mutateControl] = useAddOrUpdateControl()

  const [deleteControl] = useDeleteActionControl()

  const {data: navAction, loading, error} = useActionById(actionId, missionId, action.source, action.type)

  useEffect(() => {
    setObservationsValue(navAction?.data?.observations)
    setIdentityControlledPersonValue(navAction?.data?.identityControlledPerson)
    setVesselIdentifierValue(navAction?.data?.vesselIdentifier)
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
    const control = navAction?.data as unknown as ActionControl


    const handleObservationsChange = (nextValue?: string) => {
      setObservationsValue(nextValue)
    }
    const handleObservationsBlur = async () => {
      await onChange('observations', observationsValue)
    }

    const handleIdentityControlledPersonChange = (nextValue?: string) => {
      setIdentityControlledPersonValue(nextValue)
    }
    const handleIdentityControlledPersonBlur = async () => {
      await onChange('identityControlledPerson', identityControlledPersonValue)
    }
    const handleVesselIdentifierChange = (nextValue?: string) => {
      setVesselIdentifierValue(nextValue)
    }
    const handleVesselIdentifierBlur = async () => {
      await onChange('vesselIdentifier', vesselIdentifierValue)
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
        ...omit(control, [
          '__typename',
          'controlAdministrative',
          'controlGensDeMer',
          'controlNavigation',
          'controlSecurity'
        ]),
        startDateTimeUtc: navAction.startDateTimeUtc,
        endDateTimeUtc: navAction.endDateTimeUtc,
        ...updatedField
      }

      await mutateControl({variables: {controlAction: updatedData}})
    }

    const deleteAction = async () => {
      await deleteControl({
        variables: {
          id: action.id
        }
      })
      navigate(`/pam/missions/${missionId}`)
    }

    return (
      <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{width: '100%'}}
             data-testid={"action-control-nav"}>
        {/* TITLE AND BUTTONS */}
        <Stack.Item style={{width: '100%'}}>
          <Stack direction="row" spacing="0.5rem" style={{width: '100%'}}>
            <Stack.Item alignSelf="baseline">
              <Icon.ControlUnit color={THEME.color.charcoal} size={20}/>
            </Stack.Item>
            <Stack.Item grow={2}>
              <Stack direction="column" alignItems="flex-start">
                <Stack.Item>
                  <Text as="h2">
                    Contrôles {navAction.startDateTimeUtc && `(${formatDateTimeForFrenchHumans(navAction.startDateTimeUtc)})`}
                  </Text>
                </Stack.Item>
                <Stack.Item>
                  <Text as="h2">
                    {controlMethodToHumanString(control?.controlMethod)} - {vesselTypeToHumanString(control?.vesselType)}
                  </Text>
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item>
              <Stack direction="row" spacing="0.5rem">
                <Stack.Item>
                  <Button accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Duplicate}
                          disabled={true}>
                    Dupliquer
                  </Button>
                </Stack.Item>
                <Stack.Item>
                  <Button accent={Accent.PRIMARY} size={Size.SMALL} Icon={Icon.Delete}
                          onClick={deleteAction} data-testid={"deleteButton"}>
                    Supprimer
                  </Button>
                </Stack.Item>
              </Stack>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        {/* INFO TEXT */}
        <Stack.Item>
          <Stack direction="row" spacing="0.5rem" style={{width: '100%'}}>
            <Stack.Item alignSelf="baseline">
              <Icon.Info color={THEME.color.charcoal} size={20}/>
            </Stack.Item>
            <Stack.Item>
              <Text as="h3" weight="normal" fontStyle="italic">
                Pour la saisie des contrôles de la pêche et de l’environnement marin, veuillez appeler
                les
                centres
                concernés.
                <br/>
                Pêche : CNSP / Environnement Marin : CACEM
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        {/* DATE FIELDS */}
        <Stack.Item>
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
        {/* CONTROL ZONES FIELD */}
        <Stack.Item>
          <CoordinatesInput
            name="geoCoords"
            defaultValue={[control.latitude, control.longitude]}
            coordinatesFormat={CoordinatesFormat.DEGREES_MINUTES_DECIMALS}
            label="Lieu du contrôle"
            isLight={true}
            role={"coordinates"}
            onChange={async (nextCoordinates?: Coordinates, prevCoordinates?: Coordinates) => {
              if (!isEqual(nextCoordinates, prevCoordinates)) {
                await onChange('geoCoords', nextCoordinates)
              }
            }}
          />
        </Stack.Item>
        {/* VESSEL INFORMATION */}
        <Stack.Item style={{width: '100%'}}>
          <Stack spacing="0.5rem" style={{width: '100%'}}>
            <Stack.Item grow={1} basis={'25%'}>
              <Select
                label="Taille du navire"
                isLight={true}
                options={VESSEL_SIZE_OPTIONS}
                value={control.vesselSize}
                data-testid={"vesselSize"}
                name="vesselSize"
                role="vesselSize"
                onChange={(nextValue: OptionValueType) => onChange('vesselSize', nextValue)}
              />
            </Stack.Item>
            <Stack.Item grow={1} basis={'25%'}>
              <TextInput
                label="Immatriculation"
                isLight={true}
                data-testid={"vesselIdentifier"}
                name="vesselIdentifier"
                value={vesselIdentifierValue}
                onChange={handleVesselIdentifierChange}
                onBlur={handleVesselIdentifierBlur}
              />
            </Stack.Item>
            <Stack.Item grow={2} basis={'50%'}>
              <TextInput
                label="Identité de la personne contrôlée"
                isLight={true}
                name="identityControlledPerson"
                data-testid={"identityControlledPerson"}
                value={identityControlledPersonValue}
                onChange={handleIdentityControlledPersonChange}
                onBlur={handleIdentityControlledPersonBlur}
              />
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{width: '100%'}}>
          <Stack direction="column" spacing="0.5rem" style={{width: '100%'}}>
            <Stack.Item style={{width: '100%'}}>
              <Label>Contrôle(s) effectué(s) par l’unité sur le navire</Label>
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
              <ControlAdministrativeForm data={control.controlAdministrative}/>
            </Stack.Item>
            <Stack.Item style={{width: '100%'}}>
              <ControlNavigationForm data={control.controlNavigation}/>
            </Stack.Item>
            {/* ne pas montrer les controles gens de mer pour la plaisance de loisir */}
            {control.vesselType !== VesselTypeEnum.SAILING_LEISURE && (
              <Stack.Item style={{width: '100%'}}>
                <ControlGensDeMerForm data={control.controlGensDeMer}/>
              </Stack.Item>
            )}
            <Stack.Item style={{width: '100%'}}>
              <ControlSecurityForm data={control.controlSecurity}/>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{width: '100%'}}>
          <Textarea
            label="Observations générales sur le contrôle"
            isLight={true}
            name="observations"
            data-testid="observations"
            value={observationsValue}
            onChange={handleObservationsChange}
            onBlur={handleObservationsBlur}
          />
        </Stack.Item>
      </Stack>
    )
  }

  return null
}

export default ActionControlNav
