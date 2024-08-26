import { FC } from 'react'
import usePatchActionEnv, { PatchActionEnvInput } from '../../../hooks/use-patch-action-env.tsx'
import PatchableMonitorDateRange from '../../../../../common/components/elements/patchable-monitor-daterange.tsx'
import usePatchActionFish from '../../../hooks/use-patch-action-fish.tsx'

type ActionFishDateRangeProps = {
  missionId?: string
  actionId?: string
  startDateTimeUtc?: string
  endDateTimeUtc?: string
}
const ActionFishDateRange: FC<ActionFishDateRangeProps> = ({
  missionId,
  actionId,
  startDateTimeUtc,
  endDateTimeUtc
}) => {
  const [mutateFishAction] = usePatchActionFish()

  const onChange = async (newStartDateTimeUtc?: string, newEndDateTimeUtc?: string) => {
    const input: PatchActionEnvInput = {
      missionId: missionId,
      actionId: actionId,
      startDateTimeUtc: newStartDateTimeUtc,
      endDateTimeUtc: newEndDateTimeUtc
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
