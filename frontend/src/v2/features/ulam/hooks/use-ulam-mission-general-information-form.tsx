import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form.tsx'
import {
  MissionGeneralInfo2,
  MissionGeneralInfoExtended,
  MissionULAMGeneralInfoInitial
} from '../../common/types/mission-types.ts'

export type MissionGeneralInfoInput = {
  initial: MissionULAMGeneralInfoInitial
  extended: MissionGeneralInfoExtended
} & MissionGeneralInfo2

export const useUlamMissionGeneralInfoForm = (
  onChange: (newGeneralInfo: MissionGeneralInfo2) => Promise<unknown>,
  value: MissionGeneralInfo2
) => {
  const fromFieldValueToInput = (data: MissionGeneralInfo2): MissionGeneralInfoInput => {
    const initial = {
      id: data.id,
      nbHourAtSea: data.nbHourAtSea,
      startDateTimeUtc: data.startDateTimeUtc,
      endDateTimeUtc: data.endDateTimeUtc,
      missionReportType: data.missionReportType,
      reinforcementType: data.reinforcementType,
      missionTypes: data.missionTypes
    }
    const extended = {
      isAllAgentsParticipating: data.isAllAgentsParticipating,
      isWithInterMinisterialService: data.isWithInterMinisterialService,
      isMissionArmed: data.isMissionArmed,
      crew: data.crew,
      observations: data.observations,
      resources: data.resources,
      interMinisterialServices: data.interMinisterialServices
    }
    return { ...data, initial, extended }
  }

  const fromInputToFieldValue = (value: MissionGeneralInfoInput): MissionGeneralInfo2 => {
    const { initial, extended, ...newData } = value
    return {
      ...newData,
      nbHourAtSea: initial.nbHourAtSea,
      reinforcementType: initial.reinforcementType,
      missionReportType: initial.missionReportType,
      isMissionArmed: extended.isMissionArmed,
      isAllAgentsParticipating: extended.isAllAgentsParticipating,
      isWithInterMinisterialService: extended.isWithInterMinisterialService,
      missionTypes: initial.missionTypes,
      startDateTimeUtc: initial.startDateTimeUtc,
      endDateTimeUtc: initial.endDateTimeUtc,
      resources: extended.resources,
      crew: extended.crew,
      observations: extended.observations,
      interMinisterialServices: extended.interMinisterialServices
    }
  }

  const { initValue, handleSubmit } = useAbstractFormik<MissionGeneralInfo2, MissionGeneralInfoInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    ['isMissionArmed', 'isAllAgentsParticipating', 'isWithInterMinisterialService']
  )

  const onSubmit = async (valueToSubmit?: MissionGeneralInfo2) => {
    if (!valueToSubmit) return
    await onChange(valueToSubmit)
  }

  const handleSubmitOverride = async (
    value?: MissionGeneralInfoInput,
    errors?: FormikErrors<MissionGeneralInfoInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  return { initValue, handleSubmit: handleSubmitOverride }
}
