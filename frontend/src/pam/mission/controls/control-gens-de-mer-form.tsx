import { Panel, Stack, Toggle } from 'rsuite'
import { ControlGensDeMer, ControlType } from '../../../types/control-types'
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
import { DELETE_CONTROL_GENS_DE_MER, MUTATION_ADD_OR_UPDATE_CONTROL_GENS_DE_MER } from '../queries'
import { useMutation } from '@apollo/client'
import omit from 'lodash/omit'
import { ControlResultExtraOptions, controlResultOptions } from './control-result'
import { controlIsEnabled } from './utils'
import { useParams } from 'react-router-dom'
import ControlTitleCheckbox from './control-title-checkbox'
import ControlInfraction from '../infractions/infraction-for-control'
import { useEffect, useState } from 'react'

interface ControlGensDeMerFormProps {
  data?: ControlGensDeMer
  shouldCompleteControl?: boolean
  unitShouldConfirm?: boolean
  disableToggle?: boolean
}

const ControlGensDeMerForm: React.FC<ControlGensDeMerFormProps> = ({
  data,
  shouldCompleteControl,
  unitShouldConfirm,
  disableToggle
}) => {
  const { missionId, actionId } = useParams()

  const [observationsValue, setObservationsValue] = useState<string | undefined>(data?.observations)

  const handleObservationsChange = (nextValue?: string) => {
    setObservationsValue(nextValue)
  }

  useEffect(() => {
    setObservationsValue(data?.observations)
  }, [data])

  const handleObservationsBlur = () => {
    onChange('observations', observationsValue)
  }

  const [mutate, { statusData, statusLoading, statusError }] = useMutation(MUTATION_ADD_OR_UPDATE_CONTROL_GENS_DE_MER, {
    refetchQueries: ['GetMissionById']
  })

  const [deleteControl, { deleteData, deleteLoading, deleteError }] = useMutation(DELETE_CONTROL_GENS_DE_MER, {
    refetchQueries: ['GetMissionById']
  })

  const toggleControl = async (isChecked: boolean) =>
    isChecked
      ? onChange()
      : await deleteControl({
          variables: {
            actionId
          }
        })

  const onChange = (field?: string, value?: any) => {
    let updatedData = {
      ...omit(data, '__typename', 'infractions'),
      id: data?.id,
      missionId: missionId,
      actionControlId: actionId,
      amountOfControls: 1,
      unitShouldConfirm: unitShouldConfirm
    }

    if (!!field && value != undefined) {
      updatedData = {
        ...updatedData,
        [field]: value
      }
    }
    mutate({ variables: { control: updatedData } })
  }

  return (
    <Panel
      header={
        <ControlTitleCheckbox
          controlType={ControlType.GENS_DE_MER}
          checked={!!data || shouldCompleteControl}
          shouldCompleteControl={!!shouldCompleteControl && !!!data}
          onChange={disableToggle ? undefined : (isChecked: boolean) => toggleControl(isChecked)}
        />
      }
      // collapsible
      // defaultExpanded={controlIsEnabled(data)}
      style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
    >
      <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
        {unitShouldConfirm && (
          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" alignItems="center" spacing={'0.5rem'}>
              <Stack.Item>
                {/* TODO add Toggle component to monitor-ui */}
                <Toggle
                  checked={!!data?.unitHasConfirmed}
                  size="sm"
                  onChange={(checked: boolean) => onChange('unitHasConfirmed', checked)}
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
            value={data?.staffOutnumbered}
            error=""
            isInline
            label="Décision d’effectif conforme au nombre de personnes à bord"
            name="staffOutnumbered"
            onChange={(nextValue: OptionValue) => onChange('staffOutnumbered', nextValue)}
            options={controlResultOptions()}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <MultiRadio
            value={data?.upToDateMedicalCheck}
            error=""
            isInline
            label="Aptitudes médicales ; Visites médicales à jour"
            name="upToDateMedicalCheck"
            onChange={(nextValue: OptionValue) => onChange('upToDateMedicalCheck', nextValue)}
            options={controlResultOptions()}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <MultiRadio
            value={data?.knowledgeOfFrenchLawAndLanguage}
            error=""
            isInline
            label="Connaissance suffisante de la langue et de la loi français (navires fr/esp)"
            name="knowledgeOfFrenchLawAndLanguage"
            onChange={(nextValue: OptionValue) => onChange('knowledgeOfFrenchLawAndLanguage', nextValue)}
            options={controlResultOptions([ControlResultExtraOptions.NOT_CONCERNED])}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Textarea
            name="observations"
            label="Observations (hors infraction) sur les pièces administratives"
            value={observationsValue}
            onChange={handleObservationsChange}
            onBlur={handleObservationsBlur}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <ControlInfraction
            controlId={data?.id}
            infractions={data?.infractions}
            controlType={ControlType.GENS_DE_MER}
          />
        </Stack.Item>
      </Stack>
    </Panel>
  )
}

export default ControlGensDeMerForm
