import { THEME } from '@mtes-mct/monitor-ui'
import { JSX } from 'react'
import { Divider, FlexboxGrid } from 'rsuite'

type PageSectionWrapperProps = {
  hide?: boolean
  sectionBody?: JSX.Element
  sectionHeader?: JSX.Element
}

const PageSectionWrapper: React.FC<PageSectionWrapperProps> = ({ hide, sectionBody, sectionHeader }) => {
  if (hide) return <></>
  return (
    <FlexboxGrid>
      <FlexboxGrid.Item style={{ width: '100%', height: '50px' }}>{sectionHeader}</FlexboxGrid.Item>
      <FlexboxGrid.Item style={{ width: '100%' }}>
        <Divider style={{ backgroundColor: THEME.color.charcoal }} />
      </FlexboxGrid.Item>
      <FlexboxGrid.Item
        style={{
          width: '100%',
          height: '2000px',
          overflowY: 'auto',
          minHeight: 'calc(100vh - 260px)',
          maxHeight: 'calc(100vh - 260px)'
        }}
      >
        {sectionBody}
      </FlexboxGrid.Item>
    </FlexboxGrid>
  )
}

export default PageSectionWrapper
