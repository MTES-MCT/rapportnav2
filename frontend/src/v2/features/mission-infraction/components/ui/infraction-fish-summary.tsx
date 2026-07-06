import { Natinf } from '@common/types/infraction-types.ts'
import { find } from 'lodash'
import React from 'react'
import { Stack } from 'rsuite'
import MissionNatinfTagFish from '../../../common/components/ui/mission-natinfs-tag-fish.tsx'
import TextByCnsp from '../../../common/components/ui/text-by-cnsp.tsx'
import useNatinfListQuery from '../../../common/services/use-natinf-service.tsx'
import { FishInfraction } from '../../types/infraction-input.tsx'
import InfractionSummary from '../layout/infraction-summary.tsx'
import InfractionTypeTag from './mission-infraction-type-tag.tsx'

interface InfractionSummaryProps {
  infractions: FishInfraction[]
}

export function getFlatInfractionFromThreatsHierarchy(infraction: FishInfraction, natinfs?: Natinf[]): any {
  const natinf: Natinf = find(natinfs, { natinfCode: infraction.natinf })

  return {
    ...infraction,
    ...natinf
  }
}

const InfractionFishSummary: React.FC<InfractionSummaryProps> = ({ infractions }) => {
  const { data: natinfs } = useNatinfListQuery()
  const infractionsWithNatinfData: FishInfraction & Natinf[] = infractions.map((inf: FishInfraction) =>
    getFlatInfractionFromThreatsHierarchy(inf, natinfs)
  )
  const showIndex = infractionsWithNatinfData.length > 1

  return (
    <Stack direction="column" spacing={'.5rem'} style={{ width: '100%' }} data-testid="infraction-fish-summary">
      {infractionsWithNatinfData.map((infraction: FishInfraction & Natinf, index) => (
        <Stack.Item style={{ width: '100%' }} key={`${infraction.natinf}-${index}`}>
          <InfractionSummary
            index={index}
            tags={
              <Stack direction="row" spacing={'0.5rem'}>
                <Stack.Item>
                  <InfractionTypeTag type={infraction.infractionType} />
                </Stack.Item>
                <Stack.Item>
                  <MissionNatinfTagFish
                    natinf={(infraction as FishInfraction).natinf}
                    description={(infraction as FishInfraction).threatCharacterization}
                  />
                </Stack.Item>
              </Stack>
            }
            footerTag={<TextByCnsp />}
            observations={infraction?.comments ?? 'Aucune observation'}
            title={`Infraction ${showIndex ? index + 1 + ' ' : ''}${infraction.threat ? `: ${infraction.threat}` : ''}`}
          />
        </Stack.Item>
      ))}
    </Stack>
  )
}

export default InfractionFishSummary
