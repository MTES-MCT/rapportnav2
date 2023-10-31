import React from 'react'
import { Stack } from 'rsuite'
import { Accent, Icon, Size, THEME, IconButton, Tag, Button } from '@mtes-mct/monitor-ui'
import Title from '../../../ui/title'

interface EnvInfractionSummaryProps {
  data?: any
  onAdd?: (data: any) => void
}
const EnvInfractionSummary: React.FC<EnvInfractionSummaryProps> = ({ data, onAdd }) => {
  return (
    <Stack direction="column" spacing={'0.5rem'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" alignItems="center" justifyContent="space-between" spacing={'0.5rem'}>
          <Stack.Item>
            <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
              Navire peche blabla
            </Title>
          </Stack.Item>
          <Stack.Item>
            <Button onClick={() => onAdd({})} accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus}>
              infraction pour cette cible
            </Button>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      {[1, 2].map((whatever: any) => (
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="row" spacing={'0.5rem'}>
            <Stack.Item>
              <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
                infraction controle blablabla
              </Title>
            </Stack.Item>
            {[1, 2].map((aa: any) => (
              <Stack.Item>
                <Tag accent={Accent.PRIMARY}>
                  <b>Avec PV</b>
                </Tag>
              </Stack.Item>
            ))}
          </Stack>
        </Stack.Item>
      ))}
    </Stack>
  )
}

export default EnvInfractionSummary
