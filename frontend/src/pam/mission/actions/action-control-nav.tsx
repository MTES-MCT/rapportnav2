import React from 'react'
import {
  THEME,
  Icon,
  Button,
  Accent,
  Size,
  DateRangePicker,
  MultiZoneEditor,
  Select,
  TextInput,
  Textarea
} from '@mtes-mct/monitor-ui'
import { ControlTargetText, NavAction, VESSEL_SIZE_OPTIONS } from '../../mission-types'
import { Panel, PanelGroup, Stack } from 'rsuite'
import Title from '../../../ui/title'
import ControlNavigationRulesForm from '../controls/control-navigation-rules-form'
import ControlEquipmentAndSecurityForm from '../controls/control-equipment-and-security-form'
import ControlVesselAdministrativeForm from '../controls/control-administrative-form'
import ControlGensDeMerForm from '../controls/control-gens-de-mer-form'

interface ActionControlNavProps {
  action: NavAction
}

const ActionControlNav: React.FC<ActionControlNavProps> = ({ action }) => {
  const control = action.data.controlAction
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
                <Title as="h2">Contrôles</Title>
              </Stack.Item>
              <Stack.Item>
                <Title as="h2">{control.vesselType}</Title>
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
                <Button accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Delete}>
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
          defaultValue={[action.startDateTimeUtc, action.endDateTimeUtc]}
          label="Date et heure de début et de fin"
          withTime={true}
          isCompact={true}
          isLight={true}
        />
      </Stack.Item>
      {/* CONTROL ZONES FIELD */}
      <Stack.Item style={{ width: '100%' }}>
        <MultiZoneEditor label="Lieu du contrôle" addButtonLabel="Ajouter un point de contrôle" disabled={true} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack spacing="0.5rem" style={{ width: '100%' }}>
          <Stack.Item grow={1}>
            <Select label="Taille du navire" isLight={true} options={VESSEL_SIZE_OPTIONS} value={control.vesselSize} />
          </Stack.Item>
          <Stack.Item grow={1}>
            <TextInput label="Immatriculation" value={control.vesselIdentifier} isLight={true} />
          </Stack.Item>
          <Stack.Item grow={2}>
            <TextInput
              label="Identité de la personne contrôlée"
              value={control.identityControlledPerson}
              isLight={true}
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
              <ControlVesselAdministrativeForm data={control.controlsVesselAdministrative} />{' '}
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
              <ControlNavigationRulesForm data={control.controlsNavigationRules} />
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
              <ControlGensDeMerForm data={control.controlsGensDeMer} />
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
              <ControlEquipmentAndSecurityForm data={control.controlsNavigationRules} />
            </Panel>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Textarea label="Observations générales sur le contrôle" value={control.observations} isLight={true} />
      </Stack.Item>
    </Stack>
  )
}

export default ActionControlNav
