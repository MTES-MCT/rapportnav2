import { Accent, Icon, Tag, THEME } from '@mtes-mct/monitor-ui'
import { MissionCrewAbsence } from '../../../common/types/crew-type.ts'
import { differenceInCalendarDays } from 'date-fns'
import { formatShortDate } from '@common/utils/dates-for-humans.ts'
import { useMissionCrewAbsenceReason } from '../../hooks/use-crew-absence-reason.tsx'

interface CrewAbsenceTagProps {
  absence: MissionCrewAbsence
}

const CrewAbsenceTag: React.FC<CrewAbsenceTagProps> = ({ absence }: CrewAbsenceTagProps) => {
  const { getAbsenceReasonLabel } = useMissionCrewAbsenceReason()

  const FullMissionAbsenceTag = (
    <Tag accent={Accent.PRIMARY} withCircleIcon={true} Icon={Icon.CircleFilled} iconColor={THEME.color.maximumRed}>
      {`${getAbsenceReasonLabel(absence.reason)}`}
    </Tag>
  )
  const TemporaryAbsenceTag = (
    <Tag
      accent={Accent.PRIMARY}
      Icon={Icon.Calendar}
      iconColor={THEME.color.slateGray}
      title={`Absence du ${formatShortDate(absence.startDate)} au ${formatShortDate(absence.endDate)}`}
    >
      {`${getAbsenceReasonLabel(absence.reason)} - abs ${differenceInCalendarDays(absence.endDate, absence.startDate) + 1}j`}
    </Tag>
  )
  return absence.isAbsentFullMission ? FullMissionAbsenceTag : TemporaryAbsenceTag
}

export default CrewAbsenceTag
