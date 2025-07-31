import { THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { FlexboxGrid } from 'rsuite'

const INQUIRY_ITEMS_HEADERS = [
  {
    colspan: 6
  },
  {
    colspan: 2,
    header: `Date d'ouverture`
  },
  {
    colspan: 9,
    header: `Agent en charge du contrôle`
  },
  {
    colspan: 3,
    header: `Statut Controle`
  },
  {
    colspan: 3,
    header: `Statut du rapport`
  },
  {
    colspan: 3
  },
  {
    colspan: 1
  }
]

interface InquiryListHeaderProps {}

const InquiryListHeader: React.FC<InquiryListHeaderProps> = () => {
  return (
    <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 1rem' }}>
      {INQUIRY_ITEMS_HEADERS.map((item, index) => (
        <FlexboxGrid.Item key={`${item.header}-${index}`} colspan={item.colspan}>
          <p key={`${item.header}-${index}`} style={{ color: THEME.color.slateGray, fontSize: '12px' }}>
            {item.header}
          </p>
        </FlexboxGrid.Item>
      ))}
    </FlexboxGrid>
  )
}

export default InquiryListHeader
