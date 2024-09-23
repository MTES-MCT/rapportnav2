import styled from 'styled-components'
import Text, { TextProps } from './text'

export const ToggleLabel = styled((props: Omit<TextProps, 'as'>) => <Text {...props} as="h3" />)(({ theme }) => ({
  color: theme.color.gunMetal,
  fontWeight: 500
}))
