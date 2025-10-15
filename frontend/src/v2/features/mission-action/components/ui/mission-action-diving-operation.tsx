import { FormikCheckbox, THEME } from '@mtes-mct/monitor-ui'
import { Divider } from 'rsuite'

const MissionActionDivingOperation: React.FC = () => {
  return (
    <>
      <Divider style={{ backgroundColor: THEME.color.charcoal, marginBottom: 4 }} />
      <FormikCheckbox isLight name="hasDivingDuringOperation" label="Plongée au cours de l'opération" />
    </>
  )
}

export default MissionActionDivingOperation
