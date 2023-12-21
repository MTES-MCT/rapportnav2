import React, { ReactNode } from 'react'
import styled from 'styled-components'
import { THEME } from '@mtes-mct/monitor-ui'

interface TitleProps {
  as: 'h1' | 'h2' | 'h3' | 'h4'
  color?: string
  weight?: 'normal' | 'medium' | 'bold'
  style?: 'normal' | 'italic'
  decoration?: 'normal' | 'underline'
  children: ReactNode
}

const fontWeights = {
  normal: '400',
  medium: '500',
  bold: '700'
}

const createTitleStyled = (
  fontSize: number,
  fontWeight: string,
  color: string,
  fontStyle?: string,
  decoration?: string
) => {
  return styled.p`
    color: ${color || '#282f3e'};
    font-size: ${fontSize}px;
    font-weight: ${fontWeights[fontWeight]};
    font-style: ${fontStyle || 'normal'};
    text-decoration: ${decoration || 'none'};
    letter-spacing: 0px;
    text-align: left;
    line-spacing: 18px;
    character-spacing: 0;
  `
}

const H1 = (color: string, weight: string = 'bold', fontStyle?: string, decoration?: string) =>
  createTitleStyled(22, weight, color, fontStyle, decoration)
const H2 = (color: string, weight: string = 'bold', fontStyle?: string, decoration?: string) =>
  createTitleStyled(16, weight, color, fontStyle, decoration)
const H3 = (color: string, weight: string = 'normal', fontStyle?: string, decoration?: string) =>
  createTitleStyled(13, weight, color, fontStyle, decoration)
const H4 = (color: string, weight: string = 'normal', fontStyle?: string, decoration?: string) =>
  createTitleStyled(11, weight, color, fontStyle, decoration)

const TextComponentMap = (color: string, weight?: string, fontStyle?: string, decoration?: string) => ({
  ['h1']: H1(color, weight, fontStyle, decoration),
  ['h2']: H2(color, weight, fontStyle, decoration),
  ['h3']: H3(color, weight, fontStyle, decoration),
  ['h4']: H4(color, weight, fontStyle, decoration)
})
const Text: React.FC<TitleProps> = ({children, as, color, weight, style, decoration}) => {
  const fontColor = color ?? THEME.color.charcoal
  const Component = TextComponentMap(fontColor, weight, style, decoration)[as]

  if (!Component) {
    return null
  }

  return <Component>{children}</Component>
}

export default Text
