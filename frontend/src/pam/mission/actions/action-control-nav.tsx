import React from 'react'
import {
  THEME,
  Icon,
  Button,
  Accent,
  Size,
  DateRangePicker,
  DateRange,
  CoordinatesInput,
  Coordinates,
  Select,
  TextInput,
  Textarea,
  OptionValue,
  CoordinatesFormat
} from '@mtes-mct/monitor-ui'
import { Action, ActionControl, ActionSource, VESSEL_SIZE_OPTIONS, VesselType } from '../../mission-types'
import { Panel, Stack } from 'rsuite'
import Title from '../../../ui/title'
import { formatDateTimeForFrenchHumans } from '../../../dates'
import { DELETE_ACTION_CONTROL, MUTATION_ADD_OR_UPDATE_ACTION_CONTROL } from '../queries'
import { useMutation } from '@apollo/client'
import omit from 'lodash/omit'
import { useNavigate, useParams } from 'react-router-dom'
import ControlAdministrativeForm from '../controls/control-administrative-form'
import ControlNavigationForm from '../controls/control-navigation-form'
import ControlGensDeMerForm from '../controls/control-gens-de-mer-form'
import ControlSecurityForm from '../controls/control-security-form'

interface ActionControlNavProps {
  action: Action
}

const ActionControlNav: React.FC<ActionControlNavProps> = ({ action }) => {
  const navigate = useNavigate()
  const { missionId } = useParams()

  const control = action.data as unknown as ActionControl

  const [mutateControl, { statusData, statusLoading, statusError }] = useMutation(
    MUTATION_ADD_OR_UPDATE_ACTION_CONTROL,
    {
      refetchQueries: ['GetMissionById']
    }
  )

  const [deleteControl, { deleteData, deleteLoading, deleteError }] = useMutation(DELETE_ACTION_CONTROL, {
    refetchQueries: ['GetMissionById']
  })

  const onChange = (field: string, value: any) => {
    let updatedField = {}
    if (field == 'dates') {
      debugger
      const startDateTimeUtc = value[0].toISOString()
      const endDateTimeUtc = value[1].toISOString()
      updatedField = {
        startDateTimeUtc,
        endDateTimeUtc
      }
    } else if (field == 'geom') {
      debugger
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
      startDateTimeUtc: action.startDateTimeUtc,
      endDateTimeUtc: action.endDateTimeUtc,
      ...updatedField
    }

    mutateControl({ variables: { controlAction: updatedData } })
  }

  const deleteAction = () => {
    deleteControl({
      variables: {
        id: action.id!
      }
    })
    navigate(`/pam/missions/${missionId}`)
  }

  return (
    <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
      {/* TITLE AND BUTTONS */}
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
          <Stack.Item alignSelf="baseline">
            <Icon.Control color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item grow={2}>
            <Stack direction="column" alignItems="flex-start">
              <Stack.Item>
                <Title as="h2">
                  Contrôles {action.startDateTimeUtc && `(${formatDateTimeForFrenchHumans(action.startDateTimeUtc)})`}
                </Title>
              </Stack.Item>
              <Stack.Item>
                <Title as="h2">
                  {control.controlMethod} - {control.vesselType}
                </Title>
              </Stack.Item>
            </Stack>
          </Stack.Item>
          <Stack.Item>
            <Stack direction="row" spacing="0.5rem">
              <Stack.Item>
                <Button accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Duplicate} disabled>
                  Dupliquer
                </Button>
              </Stack.Item>
              <Stack.Item>
                <Button accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Delete} onClick={deleteAction}>
                  Supprimer
                </Button>
              </Stack.Item>
            </Stack>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      {/* INFO TEXT */}
      <Stack.Item>
        <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
          <Stack.Item alignSelf="baseline">
            <Icon.Info color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item>
            <Title as="h3" weight="normal">
              Pour la saisie des contrôles de la pêche et de l’environnement marin, veuillez appeler les centres
              concernés.
              <br />
              Pêche : CNSP / Environnement Marin : CACEM
            </Title>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      {/* DATE FIELDS */}
      <Stack.Item>
        <DateRangePicker
          defaultValue={[action.startDateTimeUtc || new Date(), action.endDateTimeUtc || new Date()]}
          label="Date et heure de début et de fin"
          withTime={true}
          isCompact={true}
          isLight={true}
          onChange={(nextUtcDateRange?: DateRange) => {
            debugger
            onChange('dates', nextUtcDateRange)
          }}
        />
      </Stack.Item>
      {/* CONTROL ZONES FIELD */}
      <Stack.Item style={{ width: '100%' }}>
        <CoordinatesInput
          defaultValue={[control.latitude, control.longitude]}
          coordinatesFormat={CoordinatesFormat.DECIMAL_DEGREES}
          label="Lieu du contrôle"
          isLight={true}
          onChange={(nextCoordinates?: Coordinates) => onChange('geom', nextCoordinates)}
        />
      </Stack.Item>
      {/* VESSEL INFORMATION */}
      <Stack.Item style={{ width: '100%' }}>
        <Stack spacing="0.5rem" style={{ width: '100%' }}>
          <Stack.Item grow={1} basis={'25%'}>
            <Select
              label="Taille du navire"
              isLight={true}
              options={VESSEL_SIZE_OPTIONS}
              value={control.vesselSize}
              name="vesselSize"
              onChange={(nextValue: OptionValue) => onChange('vesselSize', nextValue)}
            />
          </Stack.Item>
          <Stack.Item grow={1} basis={'25%'}>
            <TextInput
              label="Immatriculation"
              value={control.vesselIdentifier}
              isLight={true}
              name="vesselIdentifier"
              onChange={(nextValue?: string) => onChange('vesselIdentifier', nextValue)}
            />
          </Stack.Item>
          <Stack.Item grow={2} basis={'50%'}>
            <TextInput
              label="Identité de la personne contrôlée"
              value={control.identityControlledPerson}
              isLight={true}
              name="identityControlledPerson"
              onChange={(nextValue?: string) => onChange('identityControlledPerson', nextValue)}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" spacing="0.5rem" style={{ width: '100%' }}>
          <Stack.Item style={{ width: '100%' }}>
            <Title as="h3">Contrôle(s) effectué(s) par l’unité sur le navire</Title>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <ControlAdministrativeForm data={control.controlAdministrative} />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <ControlNavigationForm data={control.controlNavigation} />
          </Stack.Item>
          {/* ne pas montrer les controles gens de mer pour la plaisance de loisir */}
          {(action.data as unknown as ActionControl)?.vesselType !== VesselType.SAILING_LEISURE && (
            <Stack.Item style={{ width: '100%' }}>
              <ControlGensDeMerForm data={control.controlGensDeMer} />
            </Stack.Item>
          )}
          <Stack.Item style={{ width: '100%' }}>
            <ControlSecurityForm data={control.controlSecurity} />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Textarea
          label="Observations générales sur le contrôle"
          value={control.observations}
          isLight={true}
          name="observations"
          onChange={(nextValue?: string) => onChange('observations', nextValue)}
        />
      </Stack.Item>
    </Stack>
  )
}

export default ActionControlNav
