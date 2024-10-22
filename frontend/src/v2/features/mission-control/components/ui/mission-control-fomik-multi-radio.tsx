import { ControlType } from '@common/types/control-types'
import { FormikMultiRadio } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { useControlRegistry } from '../../hooks/use-control-registry'

const StackItemStyled = styled(Stack.Item)({
  width: '100%',
  paddingTop: 2,
  paddingBottom: 2
})

type MissionControlFormikMultiRadioProps = {
  control: ControlType
  radios: { name: string; label: string; extra?: boolean }[]
}

const MissionControlFormikMultiRadio: FC<MissionControlFormikMultiRadioProps> = ({ control, radios }) => {
  const { controlResultOptions, controlResultOptionsExtra } = useControlRegistry()
  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem">
      {radios.map(radio => (
        <StackItemStyled key={radio.name}>
          <FormikMultiRadio
            isInline
            key={radio.name}
            name={radio.name}
            label={radio.label}
            options={radio.extra ? controlResultOptionsExtra : controlResultOptions}
            data-testid={`control-${control.toLocaleLowerCase()}-form-${radio.name}`}
          />
        </StackItemStyled>
      ))}
    </Stack>
  )
}

export default MissionControlFormikMultiRadio
