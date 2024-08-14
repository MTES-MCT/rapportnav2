import { FC } from 'react'
import usePatchActionEnv, { PatchActionEnvInput } from './use-patch-action-env.tsx'
import PatchableMonitorDateRange from '../../forms/patchable-monitor-daterange.tsx'

type ActionEnvDateRangeProps = {
  missionId?: string
  actionId?: string
  startDateTimeUtc?: string
  endDateTimeUtc?: string
}
const ActionEnvDateRange: FC<ActionEnvDateRangeProps> = ({ missionId, actionId, startDateTimeUtc, endDateTimeUtc }) => {
  const [mutateEnvAction] = usePatchActionEnv()

  const onChange = async (newStartDateTimeUtc?: string, newEndDateTimeUtc?: string) => {
    const input: PatchActionEnvInput = {
      missionId: missionId,
      actionId: actionId,
      startDateTimeUtc: newStartDateTimeUtc,
      endDateTimeUtc: newEndDateTimeUtc
    }
    await mutateEnvAction({
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

export default ActionEnvDateRange
