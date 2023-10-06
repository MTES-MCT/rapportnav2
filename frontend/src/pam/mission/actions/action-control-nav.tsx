import React, { useEffect, useState } from 'react'
import {
  THEME,
  Icon,
  Button,
  Accent,
  Size,
  DateRangePicker,
  DateRange,
  MultiZoneEditor,
  Select,
  TextInput,
  Textarea,
  OptionValue
} from '@mtes-mct/monitor-ui'
import { Action, ActionControl, VESSEL_SIZE_OPTIONS } from '../../mission-types'
import { Panel, Stack } from 'rsuite'
import Title from '../../../ui/title'
import ControlGensDeMerForm from '../controls/control-gens-de-mer-form'
import ControlNavigationForm from '../controls/control-navigation-form'
import ControlAdministrativeForm from '../controls/control-administrative-form'
import ControlSecurityForm from '../controls/control-security-form'
import { formatDateTimeForFrenchHumans } from '../../../dates'
import { DELETE_ACTION_CONTROL, MUTATION_ADD_OR_UPDATE_ACTION_CONTROL } from '../queries'
import { useMutation } from '@apollo/client'
import omit from 'lodash/omit'
import { useParams } from 'react-router-dom'

interface ActionControlNavProps {
  action: Action
  resetSelectedAction: () => void
}

const ActionControlNav: React.FC<ActionControlNavProps> = ({ action, resetSelectedAction }) => {
  const { missionId } = useParams()
  const [control, setControl] = useState<ActionControl>((action.data || {}) as unknown as ActionControl)

  const [mutateControl, { statusData, statusLoading, statusError }] = useMutation(
    MUTATION_ADD_OR_UPDATE_ACTION_CONTROL,
    {
      refetchQueries: ['GetMissionById']
    }
  )

  const [deleteControl, { deleteData, deleteLoading, deleteError }] = useMutation(DELETE_ACTION_CONTROL, {
    refetchQueries: ['GetMissionById']
  })

  useEffect(() => {
    setControl((action.data || {}) as any as ActionControl)
  }, [action.data])

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
    debugger
    mutateControl({ variables: { controlAction: updatedData } })

    // TODO this shouldn't be like that - useState should not be used
    setControl(updatedData)
  }

  const deleteAction = () => {
    debugger
    deleteControl({
      variables: {
        id: action.id!
      }
    })
    resetSelectedAction()
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
          onChange={(nextUtcDateRange: DateRange) => {
            debugger
            onChange('dates', nextUtcDateRange)
          }}
        />
      </Stack.Item>
      {/* CONTROL ZONES FIELD */}
      <Stack.Item style={{ width: '100%' }}>
        <MultiZoneEditor label="Lieu du contrôle" addButtonLabel="Ajouter un point de contrôle" disabled={true} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack spacing="0.5rem" style={{ width: '100%' }}>
          <Stack.Item grow={1}>
            <Select
              label="Taille du navire"
              isLight={true}
              options={VESSEL_SIZE_OPTIONS}
              value={control.vesselSize}
              name="vesselSize"
              onChange={(nextValue: OptionValue) => onChange('vesselSize', nextValue)}
            />
          </Stack.Item>
          <Stack.Item grow={1}>
            <TextInput
              label="Immatriculation"
              value={control.vesselIdentifier}
              isLight={true}
              name="vesselIdentifier"
              onChange={(nextValue: string) => onChange('vesselIdentifier', nextValue)}
            />
          </Stack.Item>
          <Stack.Item grow={2}>
            <TextInput
              label="Identité de la personne contrôlée"
              value={control.identityControlledPerson}
              isLight={true}
              name="identityControlledPerson"
              onChange={(nextValue: string) => onChange('identityControlledPerson', nextValue)}
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
            <Panel
              header={
                <Title as="h3" color={THEME.color.gunmetal} weight="bold">
                  Contrôle administratif navire
                </Title>
              }
              collapsible
              style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
            >
              <ControlAdministrativeForm data={control.controlAdministrative} />{' '}
            </Panel>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Panel
              header={
                <Title as="h3" color={THEME.color.gunmetal} weight="bold">
                  Respect des règles de navigation
                </Title>
              }
              collapsible
              style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
            >
              <ControlNavigationForm data={control.controlNavigation} />
            </Panel>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Panel
              header={
                <Title as="h3" color={THEME.color.gunmetal} weight="bold">
                  Contrôle administratif gens de mer
                </Title>
              }
              collapsible
              style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
            >
              <ControlGensDeMerForm data={control.controlGensDeMer} />
            </Panel>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Panel
              header={
                <Title as="h3" color={THEME.color.gunmetal} weight="bold">
                  Equipements et respect des normes de sécurité
                </Title>
              }
              collapsible
              style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
            >
              <ControlSecurityForm data={control.controlNavigation} />
            </Panel>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Textarea
          label="Observations générales sur le contrôle"
          value={control.observations}
          isLight={true}
          name="observations"
          onChange={(nextValue: string) => onChange('observations', nextValue)}
        />
      </Stack.Item>
    </Stack>
  )
}

export default ActionControlNav
