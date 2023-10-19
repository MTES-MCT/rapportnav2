import { Stack } from 'rsuite'
import { ControlGensDeMer } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea, MultiRadio, OptionValue } from '@mtes-mct/monitor-ui'
import { CONTROL_MULTIRADIO_OPTIONS } from './utils'
import { MUTATION_ADD_OR_UPDATE_CONTROL_GENS_DE_MER } from '../queries'
import { useMutation } from '@apollo/client'
import { useEffect, useState } from 'react'
import omit from 'lodash/omit'

interface ControlGensDeMerFormProps {
  missionId: String
  actionControlId: String
  data?: ControlGensDeMer
}

const ControlGensDeMerForm: React.FC<ControlGensDeMerFormProps> = ({ missionId, actionControlId, data }) => {
  const [actionData, setActionData] = useState<ControlGensDeMer>((data || {}) as unknown as ControlGensDeMer)

  useEffect(() => {
    setActionData((data || {}) as any as ControlGensDeMer)
  }, [data])

  const [mutate, { statusData, statusLoading, statusError }] = useMutation(MUTATION_ADD_OR_UPDATE_CONTROL_GENS_DE_MER, {
    refetchQueries: ['GetMissionById']
  })

  const onChange = (field: string, value: any) => {
    const updatedData = {
      ...omit(actionData, '__typename'),
      missionId: missionId,
      actionControlId: actionControlId,
      [field]: value
    }
    debugger
    mutate({ variables: { control: updatedData } })

    // TODO this shouldn't be like that - useState should not be used
    setActionData(updatedData)
  }

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={actionData?.staffOutnumbered}
          error=""
          isInline
          label="Décision d’effectif conforme au nombre de personnes à bord"
          name="staffOutnumbered"
          onChange={(nextValue: OptionValue) => onChange('staffOutnumbered', nextValue)}
          options={CONTROL_MULTIRADIO_OPTIONS}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={actionData?.upToDateMedicalCheck}
          error=""
          isInline
          label="Aptitudes médicales ; Visites médicales à jour"
          name="upToDateMedicalCheck"
          onChange={(nextValue: OptionValue) => onChange('upToDateMedicalCheck', nextValue)}
          options={CONTROL_MULTIRADIO_OPTIONS}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={actionData?.knowledgeOfFrenchLawAndLanguage}
          error=""
          isInline
          label="Connaissance suffisante de la langue et de la loi français (navires fr/esp)"
          name="knowledgeOfFrenchLawAndLanguage"
          onChange={(nextValue: OptionValue) => onChange('knowledgeOfFrenchLawAndLanguage', nextValue)}
          options={CONTROL_MULTIRADIO_OPTIONS}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Textarea
          label="Observations (hors infraction) sur les pièces administratives"
          value={actionData?.observations}
          onChange={(nextValue: string) => onChange('observations', nextValue)}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus} isFullWidth>
          Ajouter une infraction administrative
        </Button>
      </Stack.Item>
    </Stack>
  )
}

export default ControlGensDeMerForm
