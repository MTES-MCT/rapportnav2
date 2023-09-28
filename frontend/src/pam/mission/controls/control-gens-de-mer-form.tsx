import { Stack } from 'rsuite'
import { ControlGensDeMer } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea, MultiRadio } from '@mtes-mct/monitor-ui'
import { CONTROL_MULTIRADIO_OPTIONS } from './utils'

interface ControlGensDeMerFormProps {
  data?: ControlGensDeMer
}

const ControlGensDeMerForm: React.FC<ControlGensDeMerFormProps> = ({ data }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={data?.staffOutnumbered}
          error=""
          isInline
          label="Décision d’effectif conforme au nombre de personnes à bord"
          name="staffOutnumbered"
          onChange={function noRefCheck() {}}
          options={CONTROL_MULTIRADIO_OPTIONS}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={data?.upToDateMedicalCheck}
          error=""
          isInline
          label="Aptitudes médicales ; Visites médicales à jour"
          name="upToDateMedicalCheck"
          onChange={function noRefCheck() {}}
          options={CONTROL_MULTIRADIO_OPTIONS}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={data?.knowledgeOfFrenchLawAndLanguage}
          error=""
          isInline
          label="Connaissance suffisante de la langue et de la loi français (navires fr/esp)"
          name="knowledgeOfFrenchLawAndLanguage"
          onChange={function noRefCheck() {}}
          options={CONTROL_MULTIRADIO_OPTIONS}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Textarea label="Observations (hors infraction) sur les pièces administratives" value={data?.observations} />
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
