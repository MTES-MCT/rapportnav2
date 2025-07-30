import Text from '@common/components/ui/text.tsx'
import { Accent, Icon, IconButton, THEME } from '@mtes-mct/monitor-ui'
import { useGlobalRoutes } from '@router/use-global-routes'
import React, { MouseEvent, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { Divider, FlexboxGrid, Stack } from 'rsuite'
import styled from 'styled-components'
import { useAgent } from '../../../common/hooks/use-agent'
import { useDate } from '../../../common/hooks/use-date'
import { Inquiry } from '../../../common/types/inquiry'
import { OwnerType } from '../../../common/types/owner-type'
import { User } from '../../../common/types/user'
import { useInquiry } from '../../hooks/use-inquiry'
import InquiryStatusCompleteness from './inquiry-status-completeness'
import InquiryStatusTag from './inquiry-status-tag'

interface InquiryListItemProps {
  inquiry: Inquiry
  index: number
  length: number
  openIndex: number | null
  setOpenIndex: (index: number | null) => void
  user?: User
  onSelect: (id: string) => void
}

const ListItemWithHover = styled.div`
  height: inherit;
  background-color: ${THEME.color.cultured};
  transition: background-color 0.3s ease;

  &:hover {
    background-color: ${THEME.color.blueGray25};
  }
`

const MissionCrewItem = styled.div`
  color: ${THEME.color.charcoal};
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-right: 8px;
`

const InquiryListItem: React.FC<InquiryListItemProps> = ({ index, length, inquiry, openIndex, setOpenIndex }) => {
  const navigate = useNavigate()
  const isOpen = openIndex === index
  const { getAgentById } = useAgent()
  const { getUrl } = useGlobalRoutes()
  const { getStatusReport } = useInquiry()
  const { formaDateMissionNameUlam, formatDateForFrenchHumans } = useDate()

  const listItemRef = useRef<HTMLDivElement>(null)

  const isGoToMissionButton = (target: any) =>
    target.closest('button')?.getAttribute('data-testid') === 'go-to-mission-btn'

  const onClickRow = (e: MouseEvent) => {
    if (isGoToMissionButton(e.target)) goToMission(inquiry.id)
    setOpenIndex(isOpen ? null : index)
  }

  const goToMission = (id?: string) => {
    if (id) navigate(`${getUrl(OwnerType.INQUIRY)}/${id}`)
  }

  return (
    <ListItemWithHover ref={listItemRef} onClick={onClickRow} data-testid="mission-list-item-with-hover">
      <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 1rem', marginBottom: '4px' }}>
        <FlexboxGrid.Item colspan={6} data-testid={'mission-list-item-mission_number'}>
          <Text weight={'bold'} as={'h2'}>
            {`Contrôle n°${formaDateMissionNameUlam(inquiry.startDateTimeUtc)}/${length - index}`}
          </Text>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={2} data-testid={'mission-list-item-start_date'}>
          <Text as={'h3'}>{formatDateForFrenchHumans(inquiry.startDateTimeUtc)}</Text>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={9} data-testid={'mission-list-item-crew'}>
          <MissionCrewItem data-testid={'mission-list-item-crew__text'} title={''}>
            {getAgentById(inquiry.agentId)}
          </MissionCrewItem>
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-mission_status'}>
          <InquiryStatusTag status={inquiry.status} />
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={3} data-testid={'mission-list-item-completeness'}>
          <InquiryStatusCompleteness status={getStatusReport(inquiry)} />
        </FlexboxGrid.Item>

        <FlexboxGrid.Item colspan={1} data-testid={'mission-list-item-icon-edit'} style={{ textAlign: 'right' }}>
          <Stack spacing={'1rem'} justifyContent={'flex-end'}>
            <Stack.Item>
              <IconButton
                Icon={Icon.Chevron}
                accent={Accent.TERTIARY}
                data-testid={'expand-collapse-btn'}
                style={{ transform: isOpen ? 'rotate(180deg)' : '' }}
              />
            </Stack.Item>
            <Stack.Item>
              <IconButton
                Icon={Icon.Edit}
                accent={Accent.TERTIARY}
                data-testid={'go-to-mission-btn'}
                onClick={() => goToMission(inquiry.id)}
              />
            </Stack.Item>
          </Stack>
        </FlexboxGrid.Item>

        {isOpen && (
          <FlexboxGrid.Item colspan={24} data-testid={'mission-list-item-more'}>
            <Divider
              style={{
                backgroundColor: THEME.color.charcoal
              }}
            />
            <FlexboxGrid justify="space-between" style={{ width: '100%', marginBottom: '1rem' }}>
              <FlexboxGrid.Item style={{ maxWidth: '60%', overflowWrap: 'break-word' }}>
                <p
                  style={{
                    color: THEME.color.gunMetal,
                    fontSize: '13px',
                    paddingBottom: '15px'
                  }}
                >
                  {inquiry.conclusion}
                </p>
              </FlexboxGrid.Item>
            </FlexboxGrid>
          </FlexboxGrid.Item>
        )}
      </FlexboxGrid>
    </ListItemWithHover>
  )
}

export default InquiryListItem
