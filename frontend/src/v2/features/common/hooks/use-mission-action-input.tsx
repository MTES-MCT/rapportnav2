import { MissionSourceEnum } from '@common/types/env-mission-types'
import { MissionActionInput } from '../types/mission-action-input'
import { MissionActionOutput } from '../types/mission-action-output'
import { MissionEnvActionDataInput } from '../types/mission-env-action-input'
import { MissionEnvActionOutput } from '../types/mission-env-action-output'
import { MissionFishActionDataInput } from '../types/mission-fish-action-input'
import { MissionFishActionOutput } from '../types/mission-fish-action-output'
import { MissionNavActionDataInput } from '../types/mission-nav-action-input'
import { MissionNavActionOutput } from '../types/mission-nav-action-output'

interface MissionActionInputHook {
  getMissionActionInput: (action: MissionActionOutput) => MissionActionInput
}

export function useMissionActionInput(): MissionActionInputHook {
  const getMissionActionInput = (action: MissionActionOutput): MissionActionInput => {
    const data = getMissionActionDataInput(action)
    return {
      id: action.id,
      source: action.source,
      missionId: action.missionId,
      actionType: action.actionType,
      ...data
    }
  }

  const getMissionActionDataInput = (action: MissionActionOutput) => {
    return {
      env: action.source === MissionSourceEnum.MONITORENV ? getInputFromEnvAction(action) : undefined,
      nav: action.source === MissionSourceEnum.RAPPORTNAV ? getInputFromNavAction(action) : undefined,
      fish: action.source === MissionSourceEnum.MONITORFISH ? getInputFromFishAction(action) : undefined
    }
  }

  const getInputFromEnvAction = (output: MissionActionOutput): MissionEnvActionDataInput => {
    const action = output as MissionEnvActionOutput
    return {
      startDateTimeUtc: action.data?.startDateTimeUtc,
      endDateTimeUtc: action.data?.endDateTimeUtc,
      observations: action.data?.observations,
      infractions: action.data?.infractions.map(infraction => ({ ...infraction, control })),
      controlSecurity: action.data?.controlSecurity,
      controlGensDeMer: action.data?.controlGensDeMer,
      controlNavigation: action.data?.controlNavigation,
      controlAdministrative: action.data?.controlAdministrative
    }
  }

  const getInputFromNavAction = (output: MissionActionOutput): MissionNavActionDataInput => {
    const action = output as MissionNavActionOutput
    return { ...action.data }
  }

  const getInputFromFishAction = (output: MissionActionOutput): MissionFishActionDataInput => {
    const action = output as MissionFishActionOutput
    return {
      startDateTimeUtc: action.data?.startDateTimeUtc,
      endDateTimeUtc: action.data?.endDateTimeUtc,
      observations: action.data?.observations,
      controlSecurity: action.data?.controlSecurity,
      controlGensDeMer: action.data?.controlGensDeMer,
      controlNavigation: action.data?.controlNavigation,
      controlAdministrative: action.data?.controlAdministrative
    }
  }

  return {
    getMissionActionInput
  }
}
