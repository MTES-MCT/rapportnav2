import React from 'react'
import { Dialog as MonitorUiDialog, DialogProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

// Box: function component — style overrides target inner elements via .Component-Dialog
const Box = styled((props: DialogProps) => <MonitorUiDialog {...props} />)`
  > div:last-child {
    min-width: 600px !important;
  }
`

// Body: plain styled-component — styled() works directly
const Body = styled(MonitorUiDialog.Body)`
  overflow: visible !important;
  max-height: calc(100vh - 96px);
`

// Action: plain styled-component — styled() works directly
const Action = styled(MonitorUiDialog.Action)``

// Title: function component that doesn't forward className — wrap in a styled div
const TitleWrapper = styled.div`
  > div {
  }
`
const Title = ({ children, ...props }: React.ComponentProps<typeof MonitorUiDialog.Title>) => (
  <TitleWrapper>
    <MonitorUiDialog.Title {...props}>{children}</MonitorUiDialog.Title>
  </TitleWrapper>
)

// Object.assign (rather than imperative `Box.Title = ...` assignment) lets TypeScript infer the
// compound type as an intersection, the same way monitor-ui types its own Dialog.
export const Dialog = Object.assign(Box, { Body, Action, Title })
