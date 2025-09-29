import Text from '@common/components/ui/text.tsx'
import { Label, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { EnvTheme } from '@common/types/env-themes.ts'

type MissionActionEnvThemesProps = {
  themes?: EnvTheme[]
}

const MissionActionEnvThemes: FC<MissionActionEnvThemesProps> = ({ themes }) => {
  return (
    <Stack direction={'column'} divider={<Divider style={{ width: '100%', backgroundColor: THEME.color.lightGray }} />}>
      {themes?.map((theme: EnvTheme, i: number) => (
        <Stack.Item style={{ width: '100%' }} key={theme.id} data-testid={'theme'}>
          <Stack direction={'column'} spacing={'1rem'}>
            <Stack.Item style={{ width: '100%' }}>
              <Label>{`Thématique ${themes?.length > 1 ? `(${i + 1})` : ''}`}</Label>
              <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                {theme.name ?? 'inconnue'}
              </Text>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Label>Sous-thématique(s)</Label>
              <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                {theme.subThemes?.length ? theme.subThemes.map(st => st.name).join(', ') : 'inconnues'}
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
      ))}
    </Stack>
  )
}

export default MissionActionEnvThemes
