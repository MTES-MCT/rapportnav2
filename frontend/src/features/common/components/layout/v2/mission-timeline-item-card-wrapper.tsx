import { ActionStyle } from '@common/hooks/use-action-registry.tsx'
import { IconProps, THEME } from '@mtes-mct/monitor-ui'
import React, { createElement, DetailedHTMLProps, FunctionComponent } from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import styled from 'styled-components'

const DivStyled = styled(
  ({
    isSelected,
    ...props
  }: { isSelected: Boolean } & DetailedHTMLProps<React.HTMLAttributes<HTMLDivElement>, HTMLDivElement>) => (
    <div {...props} />
  )
)(({ style, isSelected }) => ({
  cursor: 'pointer',
  minHeight: `${style?.minHeight ? 0 : style?.borderColor ? '48px' : '52px'}`,
  background: `${style?.backgroundColor ?? 'inherit'} 0% 0% no-repeat padding-box`,
  border: `${style?.borderColor ? (isSelected ? `3px solid ${THEME.color.blueGray} !important` : `1px solid ${style.borderColor}`) : 'none'}`,
  textAlign: 'left',
  letterSpacing: 0
}))

type MissionTimelineCardWrapperProps = {
  tags?: JSX.Element
  style?: ActionStyle
  title?: JSX.Element
  isSelected: boolean
  status?: JSX.Element
  footer?: JSX.Element
  subTitle?: JSX.Element
  icon?: FunctionComponent<IconProps>
  onClick: () => void
}

const MissionTimelineCardWrapper: React.FC<MissionTimelineCardWrapperProps> = ({
  icon,
  tags,
  title,
  style,
  status,
  footer,
  onClick,
  subTitle,
  isSelected
}) => {
  return (
    <DivStyled style={style} onClick={onClick} isSelected={isSelected}>
      <FlexboxGrid
        style={{
          width: '100%',
          cursor: 'pointer'
        }}
        justify="start"
      >
        <FlexboxGrid.Item
          style={{
            width: '100%',
            padding: '1rem'
          }}
        >
          <Stack direction="column">
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" spacing="0.5rem">
                <Stack.Item alignSelf="flex-start">
                  {status}
                  {icon && createElement<IconProps>(icon, { color: THEME.color.charcoal, size: 20 })}
                </Stack.Item>
                <Stack.Item alignSelf="flex-start" style={{ width: '100%' }}>
                  <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
                    <Stack.Item>
                      <Stack direction="row" spacing="0.25rem">
                        <Stack.Item>{title}</Stack.Item>
                      </Stack>
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <Stack direction="column" spacing="0.25rem" alignItems="flex-start" style={{ width: '100%' }}>
                        {subTitle && <Stack.Item>{subTitle}</Stack.Item>}
                        {tags && (
                          <Stack.Item alignSelf="flex-start" style={{ width: '100%' }} grow={3}>
                            {tags}
                          </Stack.Item>
                        )}
                      </Stack>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              {footer && (
                <Stack.Item style={{ width: '100%', display: 'flex', justifyContent: status ? 'start' : 'end' }}>
                  {footer}
                </Stack.Item>
              )}
            </Stack.Item>
          </Stack>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </DivStyled>
  )
}

export default MissionTimelineCardWrapper
