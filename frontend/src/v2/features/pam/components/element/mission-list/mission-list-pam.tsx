import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { Accent, Tag, THEME } from '@mtes-mct/monitor-ui'
import { Mission } from '@common/types/mission-types.ts'
import MissionListHeaderPam from './mission-list-header-pam.tsx'
import MissionListItemPam from './mission-list-item-pam.tsx'
import Text from '@common/components/ui/text.tsx'
import { getMonthName } from '@common/utils/dates-for-humans.ts'

interface MissionListPamProps {
  missions?: Mission[]
  selectedMissionIds: number[]
  toggleOne: (missionId: number, isChecked?: boolean) => void
}

const groupMissionsByMonth = (missions: Mission[]) => {
  const grouped: Record<string, Mission[]> = {}

  missions.forEach(mission => {
    const startDate = new Date(mission.startDateTimeUtc)
    const monthKey = `${startDate.getFullYear()}-${String(startDate.getMonth() + 1).padStart(2, '0')}`
    if (!grouped[monthKey]) {
      grouped[monthKey] = []
    }
    grouped[monthKey].push(mission)
  })

  // Sort by monthKey in descending order and return as array of [key, missions]
  return Object.entries(grouped).sort(([a], [b]) => b.localeCompare(a))
}

const MissionListPam: FC<MissionListPamProps> = ({ missions, toggleOne, selectedMissionIds }) => {
  return (
    <Stack direction={'column'} spacing={'0.2rem'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <MissionListHeaderPam />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack
          direction={'column'}
          spacing={'0.2rem'}
          style={{ width: '100%', overflowY: 'auto', height: '50vh', minHeight: '50vh' }}
        >
          {!missions?.length ? (
            <Stack.Item alignSelf={'center'} style={{ marginTop: '10rem' }}>
              <Text as={'h3'}></Text>
              Aucune mission pour cette période de temps
            </Stack.Item>
          ) : (
            groupMissionsByMonth(missions ?? []).map(([monthKey, missions]) => (
              <>
                {/* Render Month Name */}
                <Stack.Item style={{ width: '100%' }} key={monthKey}>
                  <Stack direction={'row'}>
                    <Stack.Item>
                      <Tag accent={Accent.PRIMARY} style={{ minWidth: '50px' }}>
                        <Text as={'h3'} weight={'bold'}>
                          {getMonthName(monthKey)}
                        </Text>
                      </Tag>
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <Divider style={{ backgroundColor: THEME.color.charcoal, marginTop: '28px' }} />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>

                {/* Render Missions for the Month */}
                {missions.map(mission => (
                  <Stack.Item key={mission.id} style={{ width: '100%' }}>
                    <MissionListItemPam
                      mission={mission}
                      isSelected={(selectedMissionIds || []).indexOf(mission.id) !== -1}
                      onToggle={toggleOne}
                    />
                  </Stack.Item>
                ))}
              </>
            ))
          )}
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default MissionListPam
