import Text from '@common/components/ui/text.tsx'
import { FC, useState } from 'react'
import { Container, Stack } from 'rsuite'
import { MissionListItem } from '../../../../common/types/mission-types.ts'
import MissionListHeaderUlam from './mission-list-header-ulam.tsx'
import MissionListItemUlam from './mission-list-item-ulam.tsx'
import { User } from '../../../../common/types/user.ts'

interface MissionListUlamProps {
  missions?: MissionListItem[],
  user?: User
}

const MissionListUlam: FC<MissionListUlamProps> = ({ missions, user }) => {
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
                  <Text as={'h3'}>Aucune mission pour cette période de temps</Text>
                </Stack.Item>
              ) : (
                missions?.map((mission, index) => (
                  <MissionListItemUlam
                    index={index}
                    mission={mission}
                    openIndex={openIndex}
                    setOpenIndex={setOpenIndex}
                    key={`${mission.id}-${index}`}
                    missionsLength={missions.length}
                    user={user}
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
