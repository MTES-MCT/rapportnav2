import React, { JSX, useState } from 'react'
import { Col, Container, FlexboxGrid, Stack } from 'rsuite'
import { Icon } from '@mtes-mct/monitor-ui'
import MissionListHeaderUlam from './mission-list-header-ulam.tsx'
import MissionListItemUlam from './mission-list-item-ulam.tsx'
import { Mission } from '@common/types/mission-types.ts'
import { useMissionReportExport } from '../../../common/hooks/use-mission-report-export.tsx'

interface MissionListUlamProps {
  dateRangeNavigator: JSX.Element
  missions?: Mission[]
}

const MissionListUlam: React.FC<MissionListUlamProps> = ({ missions, dateRangeNavigator, missionListing }) => {
  const [openIndex, setOpenIndex] = useState<number | null>(null)

  return (
    <>
      <FlexboxGrid justify="center" style={{ padding: '4rem 2rem', display: 'flex', flex: 1 }}>
        <FlexboxGrid.Item as={Col} colspan={24} xxl={20}>
          <Container>
            <h3 style={{ marginBottom: '45px' }}>
              <Icon.MissionAction size={26} /> Missions
            </h3>
            <h4 style={{ marginBottom: '25px' }}>Mes rapports de mission</h4>
          </Container>
          {dateRangeNavigator}
          <Container>
            <Stack direction="column" alignItems="flex-start" spacing="0.2rem" style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <MissionListHeaderUlam />
              </Stack.Item>
              <Stack.Item style={{ width: '100%', height: '100%' }}>
                {missions?.map((mission, index) => (
                  <MissionListItemUlam
                    mission={mission}
                    index={index}
                    openIndex={openIndex}
                    setOpenIndex={setOpenIndex}
                  />
                ))}
              </Stack.Item>
            </Stack>
          </Container>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </>
  )
}

export default MissionListUlam
