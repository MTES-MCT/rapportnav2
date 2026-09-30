import Text from '@common/components/ui/text'
import { Tag } from '@mtes-mct/monitor-ui'
import React from 'react'
import { useMissionTag } from '../../../common/hooks/use-mission-tag'
import { MissionActionSummary, MissionSourceEnum } from '../../../common/types/mission-types'
import { Stack } from 'rsuite'

interface MissionActionRecapTagsProps {
  actionsSummary?: MissionActionSummary[]
}

const pluralize = (count: number, singular: string, plural: string): string =>
  `${count} ${count > 1 ? plural : singular}`

// Which recap metrics are displayed per source:
//  - MonitorFish -> controls only
//  - MonitorEnv  -> controls + surveillances
//  - RapportNav  -> controls + surveillances + autres
const getLabelsForSource = (summary: MissionActionSummary): string[] => {
  const labels: string[] = []
  const showSurveillances =
    summary.source !== MissionSourceEnum.MONITORFISH && summary.source !== MissionSourceEnum.POSEIDON_CNSP
  const showOthers = summary.source === MissionSourceEnum.RAPPORT_NAV

  if (summary.nbControls > 0) labels.push(pluralize(summary.nbControls, 'contrôle', 'contrôles'))
  if (showSurveillances && summary.nbSurveillances > 0)
    labels.push(pluralize(summary.nbSurveillances, 'surveillance', 'surveillances'))
  if (showOthers && summary.nbOtherActions > 0)
    labels.push(pluralize(summary.nbOtherActions, 'autre action', 'autres actions'))

  return labels
}

// One source's recap tags. Kept as its own component so `useMissionTag` (a hook) is called once per source
// rather than inside a loop.
const SourceRecapTags: React.FC<{ summary: MissionActionSummary }> = ({ summary }) => {
  const { getSourceColor } = useMissionTag(summary.source)
  const color = getSourceColor()
  const labels = getLabelsForSource(summary)

  if (labels.length === 0) return null

  return (
    <Stack spacing={'4px'}>
      {labels.map(label => (
        <Stack.Item key={`${summary.source}-${label}`}>
          <Tag backgroundColor="transparent" borderColor={color} color={color}>
            <Text as={'h3'} weight="medium" color={color}>
              {label}
            </Text>
          </Tag>
        </Stack.Item>
      ))}
    </Stack>
  )
}

const MissionActionRecapTags: React.FC<MissionActionRecapTagsProps> = ({ actionsSummary }) => {
  if (!actionsSummary?.length) return null

  return (
    <Stack data-testid={'mission-action-recap-tags'} justifyContent={'flex-start'} direction={'column'} spacing={'8px'}>
      {actionsSummary.map(summary => (
        <Stack.Item key={summary.source} style={{ width: '100%' }}>
          <SourceRecapTags summary={summary} />
        </Stack.Item>
      ))}
    </Stack>
  )
}

export default MissionActionRecapTags
