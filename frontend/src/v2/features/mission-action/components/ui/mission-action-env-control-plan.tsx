import Text from '@common/components/ui/text.tsx'
import { FormattedControlPlan } from '@common/types/env-mission-types.ts'
import { Label, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'

type MissionActionEnvControlPlanProps = {
  controlPlans?: FormattedControlPlan[]
}

const MissionActionEnvControlPlan: FC<MissionActionEnvControlPlanProps> = ({ controlPlans }) => {
  return (
    <Stack direction={'column'} divider={<Divider style={{ width: '100%', backgroundColor: THEME.color.lightGray }} />}>
      {controlPlans?.map((theme: FormattedControlPlan, i: number) => (
        <Stack.Item style={{ width: '100%' }} key={theme.theme} data-testid={'theme'}>
          <Stack direction={'column'} spacing={'1rem'}>
            <Stack.Item style={{ width: '100%' }}>
              <Label>{`Thématique ${controlPlans?.length > 1 ? `(${i + 1})` : ''}`}</Label>
              <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                {theme.theme ?? 'inconnue'}
              </Text>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Label>Sous-thématique(s)</Label>
              <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                {theme.subThemes?.length ? theme?.subThemes?.join(', ') : 'inconnues'}
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
      ))}
    </Stack>
  )
}

export default MissionActionEnvControlPlan
