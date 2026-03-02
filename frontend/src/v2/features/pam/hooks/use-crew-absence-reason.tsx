import { MissionCrewAbsenceReason } from '../../common/types/crew-type.ts'

export const useMissionCrewAbsenceReason = () => {
  const ABSENCE_REASON_OPTIONS = [
    { value: 'SICK_LEAVE', label: 'Arrêt maladie' },
    { value: 'TRAINING', label: 'Formation' },
    { value: 'RECOVERING', label: 'Récupération' },
    { value: 'HOLIDAYS', label: 'Congés' },
    { value: 'MEETING', label: 'Réunion' },
    { value: 'DISPATCHED_ELSEWHERE', label: 'Renforcement extérieur' },
    { value: 'OTHER', label: 'Autre' }
  ]

  const ABSENCE_REASON_LABEL_BY_VALUE: Record<MissionCrewAbsenceReason, string> = Object.fromEntries(
    ABSENCE_REASON_OPTIONS.map(o => [o.value, o.label])
  ) as Record<MissionCrewAbsenceReason, string>

  function getAbsenceReasonLabel(reason: MissionCrewAbsenceReason): string {
    return ABSENCE_REASON_LABEL_BY_VALUE[reason]
  }

  return {
    ABSENCE_REASON_OPTIONS,
    getAbsenceReasonLabel
  }
}
