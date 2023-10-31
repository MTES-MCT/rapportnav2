import { Form, Panel, Stack, Toggle } from 'rsuite'
import { ControlAdministrative, ControlType } from '../../mission-types'
import {
  THEME,
  Icon,
  Button,
  Accent,
  Size,
  Textarea,
  MultiRadio,
  OptionValue,
  Checkbox,
  Label
} from '@mtes-mct/monitor-ui'
import { useMutation } from '@apollo/client'
import { GET_MISSION_BY_ID, MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE } from '../queries'
import omit from 'lodash/omit'
import { controlResultOptions } from './control-result'
import { controlIsEnabled } from './utils'
import { useParams } from 'react-router-dom'
import ControlTitleCheckbox from './control-title-checkbox'
import { useState } from 'react'
import InfractionForm from '../infractions/infraction-form'
import InfractionSummary from '../infractions/infraction-summary'

interface ControlAdministrativeFormProps {
  data?: ControlAdministrative
  shouldCompleteControl?: boolean
  unitShouldConfirm?: boolean
}

const ControlAdministrativeForm: React.FC<ControlAdministrativeFormProps> = ({
  data,
  shouldCompleteControl,
  unitShouldConfirm
}) => {
  const { missionId, actionId } = useParams()

  const [showInfractions, setShowInfractions] = useState<boolean>(false)

  const [mutate, { statusData, statusLoading, statusError }] = useMutation(
    MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE,
    {
      refetchQueries: [GET_MISSION_BY_ID]
    }
  )

  const toggleControl = (isChecked: boolean) =>
    isChecked ? onChange() : onChange('deletedAt', new Date().toISOString())

  const onChange = async (field?: string, value?: any) => {
    let updatedData = {
      ...omit(data, '__typename'),
      deletedAt: undefined,
      missionId: missionId,
      actionControlId: actionId,
      amountOfControls: 1,
      unitShouldConfirm: unitShouldConfirm
    }

    if (!!field && !!value) {
      updatedData = {
        ...updatedData,
        [field]: value
      }
      // TODO - data reset should not be handle by frontend
      if (field === 'deletedAt') {
        updatedData = {
          ...updatedData,
          compliantOperatingPermit: undefined,
          upToDateNavigationPermit: undefined,
          compliantSecurityDocuments: undefined,
          observations: undefined
        }
      }
    }

    await mutate({ variables: { control: updatedData } })
  }

  return (
    <Panel
      header={
        <ControlTitleCheckbox
          controlType={ControlType.ADMINISTRATIVE}
          checked={!!data || shouldCompleteControl}
          shouldCompleteControl={!!shouldCompleteControl && !!!data}
        />
      }
      // collapsible
      // defaultExpanded={controlIsEnabled(data)}
      style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
    >
      <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
        {unitShouldConfirm && (
          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
              <Stack.Item>
                {/* TODO add Toggle component to monitor-ui */}
                <Toggle
                  checked={!!data?.unitHasConfirmed}
                  size="sm"
                  onChange={(checked: boolean) => onChange('unitHasConfirmed', checked)}
                />
              </Stack.Item>
              <Stack.Item>
                <Label>
                  <b>Contrôle confirmé par l’unité</b>
                </Label>
              </Stack.Item>
            </Stack>
          </Stack.Item>
        )}
        <Stack.Item style={{ width: '100%' }}>
          <MultiRadio
            value={data?.compliantOperatingPermit}
            error=""
            isInline
            label="Permis de mise en exploitation (autorisation à pêcher) conforme"
            name="compliantOperatingPermit"
            onChange={(nextValue: OptionValue) => onChange('compliantOperatingPermit', nextValue)}
            options={controlResultOptions()}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <MultiRadio
            value={data?.upToDateNavigationPermit}
            error=""
            isInline
            label="Permis de navigation à jour"
            name="upToDateNavigationPermit"
            onChange={(nextValue: OptionValue) => onChange('upToDateNavigationPermit', nextValue)}
            options={controlResultOptions()}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <MultiRadio
            value={data?.compliantSecurityDocuments}
            error=""
            isInline
            label="Titres de sécurité conformes"
            name="compliantSecurityDocuments"
            onChange={(nextValue: OptionValue) => onChange('compliantSecurityDocuments', nextValue)}
            options={controlResultOptions()}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Textarea
            label="Observations (hors infraction) sur les pièces administratives"
            value={data?.observations}
            onChange={(nextValue: string) => onChange('observations', nextValue)}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          {true ? (
            <div style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem' }}>
              <InfractionSummary />
            </div>
          ) : showInfractions ? (
            <>
              <Label>Ajout d'infraction</Label>
              <div style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem' }}>
                <InfractionForm />
              </div>
            </>
          ) : (
            <Button
              onClick={() => setShowInfractions(true)}
              accent={Accent.SECONDARY}
              size={Size.NORMAL}
              Icon={Icon.Plus}
              isFullWidth
            >
              Ajouter une infraction administrative
            </Button>
          )}
        </Stack.Item>
      </Stack>
    </Panel>
  )
}

export default ControlAdministrativeForm
