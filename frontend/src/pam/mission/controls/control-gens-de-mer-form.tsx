import { Panel, Stack, Toggle } from 'rsuite'
import { ControlGensDeMer, ControlType } from '../../mission-types'
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
import { MUTATION_ADD_OR_UPDATE_CONTROL_GENS_DE_MER } from '../queries'
import { useMutation } from '@apollo/client'
import omit from 'lodash/omit'
import { ControlResultExtraOptions, controlResultOptions } from './control-result'
import { controlIsEnabled } from './utils'
import { useParams } from 'react-router-dom'
import ControlTitleCheckbox from './control-title-checkbox'

interface ControlGensDeMerFormProps {
  data?: ControlGensDeMer
  shouldCompleteControl?: boolean
  unitShouldConfirm?: boolean
}

const ControlGensDeMerForm: React.FC<ControlGensDeMerFormProps> = ({
  data,
  shouldCompleteControl,
  unitShouldConfirm
}) => {
  const { missionId, actionId } = useParams()

  const [mutate, { statusData, statusLoading, statusError }] = useMutation(MUTATION_ADD_OR_UPDATE_CONTROL_GENS_DE_MER, {
    refetchQueries: ['GetMissionById']
  })

  const toggleControl = (isChecked: boolean) =>
    isChecked ? onChange() : onChange('deletedAt', new Date().toISOString())

  const onChange = (field?: string, value?: any) => {
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
          staffOutnumbered: undefined,
          upToDateMedicalCheck: undefined,
          knowledgeOfFrenchLawAndLanguage: undefined,
          observations: undefined
        }
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
            label="Observations (hors infraction) sur les pièces administratives"
            value={data?.observations}
            onChange={(nextValue: string) => onChange('observations', nextValue)}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus} isFullWidth>
            Ajouter une infraction administrative
          </Button>
        </Stack.Item>
      </Stack>
    </Panel>
  )
}

export default ControlGensDeMerForm
