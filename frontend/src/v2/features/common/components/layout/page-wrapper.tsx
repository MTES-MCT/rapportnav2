import { THEME } from '@mtes-mct/monitor-ui'
import React, { JSX } from 'react'
import { FlexboxGrid } from 'rsuite'
import MissionPageError from '../ui/mission-page-error'
import MissionPageLoading from '../ui/mission-page-loading'

type PageWrapperProps = {
  isLoading?: boolean
  header: JSX.Element
  footer: JSX.Element
  action?: JSX.Element
  timeline?: JSX.Element
  error?: { message?: string }
  generalInformations?: JSX.Element
}

const PageWrapper: React.FC<PageWrapperProps> = ({
  error,
  header,
  footer,
  isLoading,
  action,
  timeline,
  generalInformations
}: PageWrapperProps) => {
  if (isLoading) return <MissionPageLoading />
  if (error) return <MissionPageError error={error} />

  return (
    <div
      style={{
        margin: 0,
        display: 'flex',
        minHeight: '100vh',
        maxHeight: '100vh',
        flexDirection: 'column'
      }}
    >
      {header}
      <>
        <FlexboxGrid justify="space-between" style={{ display: 'flex', flex: 1 }}>
          <FlexboxGrid.Item
            colspan={8}
            style={{
              flex: 1,
              padding: '2rem',
              height: '2000px',
              overflowY: 'hidden',
              minHeight: 'calc(100vh - 2 * 60px)',
              maxHeight: 'calc(100vh - 2 * 60px)'
            }}
          >
            {generalInformations}
          </FlexboxGrid.Item>
          <FlexboxGrid.Item
            colspan={8}
            style={{
              flex: 1,
              padding: '2rem',
              height: '2000px',
              overflowY: 'hidden',
              minHeight: 'calc(100vh - 2 * 60px)',
              maxHeight: 'calc(100vh - 2 * 60px)',
              backgroundColor: THEME.color.cultured
            }}
          >
            {timeline}
          </FlexboxGrid.Item>
          <FlexboxGrid.Item
            colspan={8}
            style={{
              flex: 1,
              padding: '2rem',
              height: '2000px',
              overflowY: 'hidden',
              minHeight: 'calc(100vh - 2 * 60px)',
              maxHeight: 'calc(100vh - 2 * 60px)',
              backgroundColor: THEME.color.gainsboro
            }}
          >
            {action}
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </>
      {footer}
    </div>
  )
}

export default PageWrapper
