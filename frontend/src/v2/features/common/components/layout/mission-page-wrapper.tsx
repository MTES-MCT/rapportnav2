import { THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { FlexboxGrid } from 'rsuite'
import MissionPageError from '../ui/mission-page-error'
import MissionPageLoading from '../ui/mission-page-loading'

type MissionPageWrapperProps = {
  isLoading?: boolean
  missionHeader: JSX.Element
  missionFooter: JSX.Element
  missionAction?: JSX.Element
  missionTimeLine?: JSX.Element
  error?: { message?: string }
  missionGeneralInformations?: JSX.Element
}

const MissionPageWrapper: React.FC<MissionPageWrapperProps> = ({
  error,
  missionHeader,
  missionFooter,
  isLoading,
  missionAction,
  missionTimeLine,
  missionGeneralInformations
}: MissionPageWrapperProps) => {
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
      {missionHeader}
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
            {missionGeneralInformations}
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
            {missionTimeLine}
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
            {missionAction}
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </>
      {missionFooter}
    </div>
  )
}

export default MissionPageWrapper
