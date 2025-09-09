import { ControlType } from '@common/types/control-types'
import { Accent, Button, Icon, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import { FC, useState } from 'react'
import { Infraction, TargetInfraction } from '../../../common/types/target-types'
import { useInfraction } from '../../hooks/use-infraction'
import MissionInfractionForm from './mission-infraction-form'

export interface MissionInfractionFormNewProps {
  isDisabled: boolean
  controlType: ControlType
  onSubmit: (infraction: Infraction) => void
}

const MissionInfractionFormNew: FC<MissionInfractionFormNewProps> = ({ isDisabled, onSubmit, controlType }) => {
  const { getInfractionByControlTypeButton } = useInfraction()
  const [shownewForm, setShowNewForm] = useState<boolean>(false)

  const handleSubmit = async (value?: TargetInfraction) => {
    if (!value?.infraction) return
    onSubmit(value.infraction)
    setShowNewForm(false)
  }

  return (
    <>
      {shownewForm && (
        <>
          <Label>Ajout d'infraction</Label>
          <div style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem' }}>
            <MissionInfractionForm
              editTarget={false}
              editControl={false}
              editInfraction={true}
              onSubmit={handleSubmit}
              value={{} as TargetInfraction}
              onClose={() => setShowNewForm(false)}
            />
          </div>
        </>
      )}
      {!shownewForm && (
        <Button
          Icon={Icon.Plus}
          isFullWidth={true}
          size={Size.NORMAL}
          accent={Accent.SECONDARY}
          role="add-infraction-button"
          onClick={() => setShowNewForm(true)}
          disabled={isDisabled}
        >
          {`${getInfractionByControlTypeButton(controlType)}`}
        </Button>
      )}
    </>
  )
}

export default MissionInfractionFormNew
