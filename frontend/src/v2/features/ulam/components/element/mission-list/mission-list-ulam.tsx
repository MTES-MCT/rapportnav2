import { FC, useState } from 'react'
import { Container, Stack } from 'rsuite'
import MissionListHeaderUlam from './mission-list-header-ulam.tsx'
import MissionListItemUlam from './mission-list-item-ulam.tsx'
import { Mission } from '@common/types/mission-types.ts'
import Text from '@common/components/ui/text.tsx'

interface MissionListUlamProps {
  missions?: Mission[]
}

const MissionListUlam: FC<MissionListUlamProps> = ({ missions }) => {
  const [openIndex, setOpenIndex] = useState<number | null>(null)

  return (
    <Container>
      <Stack direction="column" alignItems="flex-start" spacing="0.2rem" style={{ width: '100%' }}>
        <Stack.Item style={{ width: '100%' }}>
          <MissionListHeaderUlam />
        </Stack.Item>

        <Stack.Item style={{ width: '100%' }}>
          <Stack
            direction={'column'}
            spacing={'0.2rem'}
            style={{ width: '100%', overflowY: 'scroll', height: '50vh', minHeight: '50vh', maxHeight: '50vh' }}
          >
            <Stack.Item style={{ width: '100%' }}>
              {!missions?.length ? (
                <Stack.Item alignSelf={'center'} style={{ marginTop: '10rem' }}>
                  <Text as={'h3'}></Text>
                  Aucune mission pour cette période de temps
                </Stack.Item>
              ) : (
                missions?.map((mission, index) => (
                  <MissionListItemUlam
                    key={mission.id}
                    mission={mission}
                    index={index}
                    openIndex={openIndex}
                    setOpenIndex={setOpenIndex}
                  />
                ))
              )}
            </Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>
    </Container>
  )
}

export default MissionListUlam
