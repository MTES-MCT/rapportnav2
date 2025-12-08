import { Accent, Icon, Tag, THEME } from '@mtes-mct/monitor-ui'
import { getAbsenceReasonLabel, MissionCrewAbsence } from '../../../common/types/crew-type.ts'
import { differenceInCalendarDays } from 'date-fns'

interface CrewAbsenceTagProps {
  absence: MissionCrewAbsence
}

const CrewAbsenceTag: React.FC<CrewAbsenceTagProps> = ({ absence }: CrewAbsenceTagProps) => {
  const FullMissionAbsenceTag = (
    <Tag accent={Accent.PRIMARY} withCircleIcon={true} Icon={Icon.CircleFilled} iconColor={THEME.color.maximumRed}>
      {`${getAbsenceReasonLabel(absence.reason)}`}
    </Tag>
  )
  const TemporaryAbsenceTag = (
    <Tag accent={Accent.PRIMARY} Icon={Icon.Calendar}>
      {`${getAbsenceReasonLabel(absence.reason)} - abs ${differenceInCalendarDays(absence.endDate, absence.startDate) + 1}j`}
    </Tag>
  )
  return absence.isAbsentFullMission ? FullMissionAbsenceTag : TemporaryAbsenceTag
}

export default CrewAbsenceTag
