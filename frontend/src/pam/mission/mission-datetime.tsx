import { FC } from 'react'
import { DateRangePicker } from '@mtes-mct/monitor-ui'
import { Mission } from '../../types/mission-types.ts'
import usePatchMissionEnv, { PatchMissionEnvInput } from './use-patch-mission-env.tsx'

type MissionDatetimeProps = {
  mission: Mission
}
const MissionDatetime: FC<MissionDatetimeProps> = ({ mission }) => {
  const [mutateEnvMission] = usePatchMissionEnv()

  const onChange = async (value: any) => {
    const startDateTimeUtc = value[0].toISOString()
    const endDateTimeUtc = value[1].toISOString()
    const data: PatchMissionEnvInput = {
      missionId: mission.id,
      startDateTimeUtc,
      endDateTimeUtc
    }
    await mutateEnvMission({ variables: { mission: data } })
  }

  return (
    <DateRangePicker
      defaultValue={[mission.startDateTimeUtc || new Date(), mission.endDateTimeUtc || new Date()]}
      label="Dates du rapport"
      withTime={true}
      isCompact={true}
      onChange={async (nextValue?: [Date, Date] | [string, string]) => {
        await onChange(nextValue)
      }}
    />
  )
}

export default MissionDatetime
