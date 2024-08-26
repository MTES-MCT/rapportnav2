import { FC } from 'react'
import { Mission } from '../../../../common/types/mission-types.ts'
import usePatchMissionEnv, { PatchMissionEnvInput } from '../../hooks/use-patch-mission-env.tsx'
import PatchableMonitorDateRange from '../../../../common/components/elements/patchable-monitor-daterange.tsx'

type MissionDateRangeProps = {
  mission: Mission
}
const MissionDaterange: FC<MissionDateRangeProps> = ({ mission }) => {
  const [mutateEnvMission] = usePatchMissionEnv()

  const onChange = async (startDateTimeUtc?: string, endDateTimeUtc?: string) => {
    const data: PatchMissionEnvInput = {
      missionId: mission.id,
      startDateTimeUtc,
      endDateTimeUtc
    }
    await mutateEnvMission({ variables: { mission: data } })
  }

  return (
    <PatchableMonitorDateRange
      label="Dates du rapport"
      isLight={false}
      onChange={onChange}
      startDateTimeUtc={mission.startDateTimeUtc}
      endDateTimeUtc={mission.endDateTimeUtc}
    />
  )
}

export default MissionDaterange
