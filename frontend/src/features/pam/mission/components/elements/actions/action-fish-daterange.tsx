import { FC } from 'react'
import { PatchActionEnvInput } from '../../../hooks/use-patch-action-env.tsx'
import PatchableMonitorDateRange from '../../../../../common/components/elements/patchable-monitor-daterange.tsx'
import usePatchActionFish from '../../../hooks/use-patch-action-fish.tsx'

type ActionFishDateRangeProps = {
  missionId?: string
  actionId?: string
  startDateTimeUtc?: string
  endDateTimeUtc?: string
  observationsByUnit?: string
}
const ActionFishDateRange: FC<ActionFishDateRangeProps> = ({
  missionId,
  actionId,
  startDateTimeUtc,
  endDateTimeUtc,
  observationsByUnit
}) => {
  const [mutateFishAction] = usePatchActionFish()

  const onChange = async (newStartDateTimeUtc?: string, newEndDateTimeUtc?: string) => {
    const input: PatchActionEnvInput = {
      missionId: missionId,
      actionId: actionId,
      startDateTimeUtc: newStartDateTimeUtc,
      endDateTimeUtc: newEndDateTimeUtc,
      observationsByUnit
    }
    await mutateFishAction({
      variables: {
        action: input
      }
    })
  }

  return (
    <PatchableMonitorDateRange
      label="Date et heure de début et de fin"
      isLight={true}
      onChange={onChange}
      startDateTimeUtc={startDateTimeUtc}
      endDateTimeUtc={endDateTimeUtc}
    />
  )
}

export default ActionFishDateRange
