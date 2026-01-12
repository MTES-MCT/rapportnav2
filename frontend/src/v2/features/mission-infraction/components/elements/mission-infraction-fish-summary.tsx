import React from 'react'
import MissionNatinfTagFish from '../../../common/components/ui/mission-natinfs-tag-fish.tsx'
import { FishInfraction } from '../../types/infraction-input.tsx'
import { Natinf } from '@common/types/infraction-types.ts'
import useNatinfListQuery from '../../../common/services/use-natinf-service.tsx'
import { find } from 'lodash'
import MissionInfractionEmpty from '../ui/mission-infraction-empty.tsx'
import { Stack } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'
import Text from '@common/components/ui/text.tsx'
import MissionInfractionTypeTag from '../ui/mission-infraction-type-tag.tsx'

interface MissionInfractionSummaryProps {
  infractions: FishInfraction[]
}

export function getFlatInfractionFromThreatsHierarchy(infraction: FishInfraction, natinfs: Natinf[]): any {
  const natinf: Natinf = find(natinfs, { natinfCode: infraction.natinf })

  return {
    ...infraction,
    ...natinf
  }
}

const MissionInfractionFishSummary: React.FC<MissionInfractionSummaryProps> = ({ infractions }) => {
  const { data: natinfs } = useNatinfListQuery()
  const infractionsWithNatinfData: FishInfraction & Natinf[] = infractions.map((inf: FishInfraction) =>
    getFlatInfractionFromThreatsHierarchy(inf, natinfs)
  )
  const showIndex = infractionsWithNatinfData.length > 1

  return (
    <>
      {infractionsWithNatinfData?.length === 0 ? (
        <MissionInfractionEmpty />
      ) : (
        infractionsWithNatinfData.map((infraction: FishInfraction & Natinf, index) => (
          <Stack
            key={`${infraction.natinfCode}-${index}`}
            direction="column"
            spacing={'0.5rem'}
            style={{
              width: '100%',
              padding: '1rem',
              backgroundColor: THEME.color.cultured,
              marginBottom: index === infractions.length - 1 ? 0 : '0.1rem'
            }}
          >
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" alignItems="center" justifyContent="space-between" spacing={'0.5rem'}>
                <Stack.Item>
                  <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                    {`Infraction ${showIndex ? index + 1 + ' ' : ''}${infraction.threat ? `: ${infraction.threat}` : ''}`}
                  </Text>
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" spacing={'0.5rem'}>
                <Stack.Item>
                  <MissionInfractionTypeTag type={infraction.infractionType} />
                </Stack.Item>
                <Stack.Item>
                  <MissionNatinfTagFish
                    natinf={(infraction as FishInfraction).natinf}
                    description={(infraction as FishInfraction).threatCharacterization}
                  />
                </Stack.Item>
              </Stack>
            </Stack.Item>
            {infraction?.comments && (
              <Stack.Item style={{ width: '100%' }}>
                <Text as="h3">{infraction?.comments}</Text>
              </Stack.Item>
            )}
          </Stack>
        ))
      )}
    </>
  )
}

export default MissionInfractionFishSummary
