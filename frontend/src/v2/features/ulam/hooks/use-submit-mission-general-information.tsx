import {
  MissionGeneralInfo2,
  MissionGeneralInfoExtended,
  MissionULAMGeneralInfoInitial
} from '../../common/types/mission-types.ts'
import { useEffect, useState } from 'react'

export type MissionGeneralInfoInput = {
  initial: MissionULAMGeneralInfoInitial,
  extended: MissionGeneralInfoExtended
}

const useHandleSubmitMissionGeneralInfoHook = (onChange: (newGeneralInfo: MissionGeneralInfo2) => Promise<unknown>, generalInfo?: MissionGeneralInfo2) => {

  const [initValue, setInitValue] = useState<MissionGeneralInfoInput>()

  useEffect(() => {
    if (!generalInfo) return
    const data = fromFieldValueToInput(generalInfo)
    setInitValue(data)
  }, [generalInfo])


   const handleSubmit = async (values: MissionGeneralInfoInput) => {
    const data = fromInputToFieldValue(values)

     await onChange({ ...data })
  }

  const fromFieldValueToInput = (data: MissionGeneralInfo2): MissionGeneralInfoInput => {
    return {
      initial: {
        id: data.id,
        startDateTimeUtc: data.startDateTimeUtc,
        endDateTimeUtc: data.endDateTimeUtc,
        missionReportType: data.missionReportType,
        reinforcementType: data.reinforcementType,
        missionTypes: data.missionTypes
      },
      extended: {
        isAllAgentsParticipating: data.isAllAgentsParticipating,
        isWithInterMinisterialService: data.isWithInterMinisterialService,
        isMissionArmed: data.isMissionArmed,
        crew: data.crew,
        observations: data.observations,
        resources: data.resources
      }
    }
  }

  const fromInputToFieldValue = (value: MissionGeneralInfoInput): MissionGeneralInfo2 => {
    return {
      id: value.initial.id,
      nbHourAtSea: value.initial.nbHourAtSea,
      reinforcementType: value.initial.reinforcementType,
      missionReportType: value.initial.missionReportType,
      isMissionArmed: value.extended.isMissionArmed,
      isAllAgentsParticipating: value.extended.isAllAgentsParticipating,
      isWithInterMinisterialService: value.extended.isWithInterMinisterialService,
      missionTypes: value.initial.missionTypes,
      startDateTimeUtc: value.initial.startDateTimeUtc,
      endDateTimeUtc: value.initial.endDateTimeUtc,
      resources: value.extended.resources,
      crew: value.extended.crew,
      observations: value.extended.observations
    }
  }


  return { handleSubmit, initValue }
}

export default useHandleSubmitMissionGeneralInfoHook

