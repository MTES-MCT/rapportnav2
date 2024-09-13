import React, { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { Label, THEME } from '@mtes-mct/monitor-ui'
import { FormattedControlPlan } from '@common/types/env-mission-types.ts'
import Text from '@common/components/ui/text.tsx'

type EnvActionControlPlansProps = {
  controlPlans?: FormattedControlPlan[]
}

const EnvActionControlPlans: FC<EnvActionControlPlansProps> = ({ controlPlans }) => {
  return (
    <Stack
      direction={'column'}
      // spacing={'2rem'}
      divider={<Divider style={{ width: '100%', backgroundColor: THEME.color.lightGray }} />}
    >
      {controlPlans?.map((theme: FormattedControlPlan, i: number) => (
        <Stack.Item style={{ width: '100%' }} key={theme.theme} data-testid={'theme'}>
          <Stack direction={'column'} spacing={'1rem'}>
            <Stack.Item style={{ width: '100%' }}>
              <Label>Thématique de contrôle {controlPlans?.length > 1 ? `(${i + 1})` : ''}</Label>
              <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                {theme.theme ? theme.theme : 'inconnue'}
              </Text>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Label>Sous-thématique(s) de contrôle</Label>
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

export default EnvActionControlPlans
