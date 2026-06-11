import React from 'react'
import { Dialog as MonitorUiDialog, DialogProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

// Dialog: function component — style overrides target inner elements via .Component-Dialog
const CustomDialog = styled((props: DialogProps) => <MonitorUiDialog {...props} />)`
  > div:last-child {
    min-width: unset !important;
  }
`

// Body: plain styled-component — styled() works directly
CustomDialog.Body = styled(MonitorUiDialog.Body)`
  overflow: visible !important;
`

// Action: plain styled-component — styled() works directly
CustomDialog.Action = styled(MonitorUiDialog.Action)``

// Title: function component that doesn't forward className — wrap in a styled div
const TitleWrapper = styled.div`
  > div {
  }
`
CustomDialog.Title = ({ children, ...props }: React.ComponentProps<typeof MonitorUiDialog.Title>) => (
  <TitleWrapper>
    <MonitorUiDialog.Title {...props}>{children}</MonitorUiDialog.Title>
  </TitleWrapper>
)

export { CustomDialog as Dialog }
