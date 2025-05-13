import { ControlType } from '@common/types/control-types'
import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { FC, useState } from 'react'
import { Divider, Stack } from 'rsuite'
import { TargetType } from '../../../common/types/target-types'
import MissionInfractionEnvForm from '../../../mission-infraction/components/elements/mission-infraction-env-form'
import MissionInfractionEnvSummary from '../../../mission-infraction/components/ui/mission-infraction-env-summary'
import { TargetInfraction } from '../../../mission-infraction/hooks/use-infraction-env-form'

export interface MissionTargetEnvInfractionFormProps {
  index: number
  value: TargetInfraction
  targetType?: TargetType
  onDelete: (index: number) => void
  availableControlTypes?: ControlType[]
  onSubmit: (value?: TargetInfraction) => Promise<unknown>
}

const MissionTargetEnvInfractionForm: FC<MissionTargetEnvInfractionFormProps> = ({
  index,
  value,
  onDelete,
  onSubmit,
  targetType,
  availableControlTypes
}) => {
  const [showForm, setShowForm] = useState(false)

  const handleSubmit = async (value?: TargetInfraction) => {
    onSubmit(value)
    setShowForm(false)
  }

  return (
    <Stack
      direction="column"
      spacing={'0.5rem'}
      style={{ width: '100%', marginBottom: '.2em' }}
      key={`infraction-${index}`}
    >
      {!showForm && (
        <Stack.Item style={{ width: '100%' }}>
          <Divider />
          <Stack direction="row" spacing={'.5em'} style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%' }}>
              <MissionInfractionEnvSummary
                natinfs={value?.infraction?.natinfs}
                controlType={value?.control?.controlType}
                infractionType={value?.infraction?.infractionType}
              />
            </Stack.Item>
            <Stack.Item style={{ justifyContent: 'flex-end' }}>
              <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                <Stack.Item>
                  <IconButton
                    Icon={Icon.EditUnbordered}
                    size={Size.NORMAL}
                    role="delete-target"
                    accent={Accent.SECONDARY}
                    onClick={() => setShowForm(true)}
                    disabled={!availableControlTypes?.length}
                  />
                </Stack.Item>
                <Stack.Item>
                  <IconButton
                    Icon={Icon.Delete}
                    size={Size.NORMAL}
                    role="delete-target"
                    accent={Accent.SECONDARY}
                    onClick={() => onDelete(index)}
                  />
                </Stack.Item>
              </Stack>
            </Stack.Item>
          </Stack>
        </Stack.Item>
      )}
      {showForm && (
        <Stack.Item
          style={{
            width: '100%',
            padding: '1rem',
            marginTop: '1rem',
            backgroundColor: THEME.color.cultured
          }}
        >
          <MissionInfractionEnvForm
            value={value}
            editTarget={false}
            editInfraction={true}
            onSubmit={handleSubmit}
            targetType={targetType}
            onClose={() => setShowForm(false)}
            availableControlTypes={availableControlTypes}
          />
        </Stack.Item>
      )}
    </Stack>
  )
}

export default MissionTargetEnvInfractionForm
