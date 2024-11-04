import Text from '@common/components/ui/text.tsx'
import { Label, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'

interface MissionInfractionEmptyProps {}

const MissionInfractionEmpty: React.FC<MissionInfractionEmptyProps> = () => {
  return (
    <Stack
      direction="column"
      style={{
        width: '100%',
        backgroundColor: THEME.color.white,
        padding: '1rem'
      }}
    >
      <Stack.Item style={{ width: '100%' }}>
        <Label>Infractions</Label>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Text as="h3" weight="medium">
          Aucune infraction
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionInfractionEmpty
