import { Panel, Stack, Toggle } from 'rsuite'
import { ControlAdministrative, ControlType } from '../../../types/control-types'
import { Label, MultiRadio, OptionValue, Textarea, THEME } from '@mtes-mct/monitor-ui'
import omit from 'lodash/omit'
import { controlResultOptions } from './control-result'
import { useParams } from 'react-router-dom'
import ControlTitleCheckbox from './control-title-checkbox'
import ControlInfraction from '../infractions/infraction-for-control'
import { FC, useEffect, useState } from 'react'
import useDeleteControl from './use-delete-control.tsx'
import useAddOrUpdateControl from './use-add-update-control.tsx'

interface ControlAdministrativeFormProps {
  data?: ControlAdministrative
  shouldCompleteControl?: boolean
  unitShouldConfirm?: boolean
}

const ControlAdministrativeForm: FC<ControlAdministrativeFormProps> = ({
  data,
  shouldCompleteControl,
  unitShouldConfirm
}) => {
  const { missionId, actionId } = useParams()

  const [observationsValue, setObservationsValue] = useState<string | undefined>(data?.observations)

  const controlOptions = controlResultOptions()

  const handleObservationsChange = (nextValue?: string) => {
    setObservationsValue(nextValue)
  }

  useEffect(() => {
    setObservationsValue(data?.observations)
  }, [data])

  const handleObservationsBlur = async () => {
    if (observationsValue !== data?.observations) {
      await onChange('observations', observationsValue)
    }
  }

  const [mutateControl, { loading: mutationLoading }] = useAddOrUpdateControl({
    controlType: ControlType.ADMINISTRATIVE
  })
  const [deleteControl] = useDeleteControl({ controlType: ControlType.ADMINISTRATIVE })

  const toggleControl = async (isChecked: boolean) =>
    isChecked
      ? onChange()
      : await deleteControl({
          variables: {
            actionId
          }
        })

  const onChange = async (field?: string, value?: any) => {
    let updatedData = {
      ...omit(data, '__typename', 'infractions'),
      id: data?.id,
      missionId: missionId,
      actionControlId: actionId,
      amountOfControls: 1,
      unitShouldConfirm: unitShouldConfirm
    }

    if (!!field && value !== undefined) {
      updatedData = {
        ...updatedData,
        [field]: value
      }
    }

    await mutateControl({ variables: { control: updatedData } })
  }

  return (
    <Panel
      header={
        <ControlTitleCheckbox
          controlType={ControlType.ADMINISTRATIVE}
          checked={!!data || shouldCompleteControl}
          disabled={mutationLoading}
          shouldCompleteControl={!!shouldCompleteControl && !!!data}
          onChange={(isChecked: boolean) => toggleControl(isChecked)}
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
                  disabled={mutationLoading}
                />
              </Stack.Item>
              <Stack.Item alignSelf="flex-end">
                <Label style={{ marginBottom: 0 }}>
                  <b>Contrôle confirmé par l’unité</b>
                </Label>
              </Stack.Item>
            </Stack>
          </Stack.Item>
        )}
        <Stack.Item style={{ width: '100%' }}>
          <MultiRadio
            value={data?.compliantOperatingPermit ?? undefined}
            error=""
            isInline
            label="Permis de mise en exploitation (autorisation à pêcher) conforme"
            name="compliantOperatingPermit"
            onChange={(nextValue: OptionValue) => onChange('compliantOperatingPermit', nextValue)}
            options={controlOptions}
            disabled={mutationLoading}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <MultiRadio
            value={data?.upToDateNavigationPermit ?? undefined}
            error=""
            isInline
            label="Permis de navigation à jour"
            name="upToDateNavigationPermit"
            onChange={(nextValue: OptionValue) => onChange('upToDateNavigationPermit', nextValue)}
            options={controlOptions}
            disabled={mutationLoading}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <MultiRadio
            value={data?.compliantSecurityDocuments ?? undefined}
            error=""
            isInline
            label="Titres de sécurité conformes"
            name="compliantSecurityDocuments"
            onChange={(nextValue: OptionValue) => onChange('compliantSecurityDocuments', nextValue)}
            options={controlOptions}
            disabled={mutationLoading}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Textarea
            name="observations"
            label="Observations (hors infraction) sur les pièces administratives"
            value={observationsValue}
            onChange={handleObservationsChange}
            onBlur={handleObservationsBlur}
            disabled={mutationLoading}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <ControlInfraction
            controlId={data?.id}
            infractions={data?.infractions}
            controlType={ControlType.ADMINISTRATIVE}
          />
        </Stack.Item>
      </Stack>
    </Panel>
  )
}

export default ControlAdministrativeForm
