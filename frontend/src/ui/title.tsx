import React, { ReactNode } from 'react'
import styled from 'styled-components'
import { THEME } from '@mtes-mct/monitor-ui'

interface TitleProps {
  as: 'h1' | 'h2' | 'h3'
  color?: string
  weight?: string
  children: ReactNode
}

const createTitleStyled = (fontSize: number, fontWeight: string, color: string) => {
  return styled.p`
    color: ${color || '#282f3e'};
    font-size: ${fontSize}px;
    font-weight: ${fontWeight || 'normal'};
    letter-spacing: 0px;
  `
}

const H1 = (color: string) => createTitleStyled(22, 'bold', color)
const H2 = (color: string) => createTitleStyled(16, 'bold', color)
const H3 = (color: string, weight: string = 'normal') => createTitleStyled(13, weight, color)

const TitleComponentMap = (color: string, weight?: string) => ({
  ['h1']: H1(color),
  ['h2']: H2(color),
  ['h3']: H3(color, weight)
})
const Title: React.FC<TitleProps> = ({ children, as, color, weight }) => {
  const fontColor = color ?? THEME.color.charcoal
  const Component = TitleComponentMap(fontColor, weight)[as]

  if (!Component) {
    return null
  }

  return <Component>{children}</Component>
}

export default Title
