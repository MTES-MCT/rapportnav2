import { render, screen } from '../../../../../../test-utils.tsx'
import { describe, it, expect } from 'vitest'
import CrewAbsenceTag from '../crew-absence-tag'
import { MissionCrewAbsence, MissionCrewAbsenceReason } from '../../../../common/types/crew-type.ts'

describe('CrewAbsenceTag', () => {
  describe('Full mission absence', () => {
    it('renders the absence reason label when isAbsentFullMission is true', () => {
      const absence: MissionCrewAbsence = {
        isAbsentFullMission: true,
        reason: MissionCrewAbsenceReason.SICK_LEAVE
      }
      render(<CrewAbsenceTag absence={absence} />)
      expect(screen.getByText('Arrêt maladie')).toBeInTheDocument()
    })

    it('matches snapshot for full mission absence', () => {
      const absence: MissionCrewAbsence = {
        isAbsentFullMission: true,
        reason: MissionCrewAbsenceReason.TRAINING
      }
      const { container } = render(<CrewAbsenceTag absence={absence} />)
      expect(container).toMatchSnapshot()
    })
  })

  describe('Temporary absence', () => {
    it('renders the absence reason label with duration when isAbsentFullMission is false', () => {
      const absence: MissionCrewAbsence = {
        isAbsentFullMission: false,
        reason: MissionCrewAbsenceReason.HOLIDAYS,
        startDate: new Date('2024-01-01'),
        endDate: new Date('2024-01-03')
      }
      render(<CrewAbsenceTag absence={absence} />)
      expect(screen.getByText('Congés - abs 3j')).toBeInTheDocument()
    })

    it('renders 1 day absence correctly', () => {
      const absence: MissionCrewAbsence = {
        isAbsentFullMission: false,
        reason: MissionCrewAbsenceReason.MEETING,
        startDate: new Date('2024-01-15'),
        endDate: new Date('2024-01-15')
      }
      render(<CrewAbsenceTag absence={absence} />)
      expect(screen.getByText('Réunion - abs 1j')).toBeInTheDocument()
    })

    it('matches snapshot for temporary absence', () => {
      const absence: MissionCrewAbsence = {
        isAbsentFullMission: false,
        reason: MissionCrewAbsenceReason.RECOVERING,
        startDate: new Date('2024-01-01'),
        endDate: new Date('2024-01-05')
      }
      const { container } = render(<CrewAbsenceTag absence={absence} />)
      expect(container).toMatchSnapshot()
    })
  })

  describe('Different absence reasons', () => {
    it.each([
      [MissionCrewAbsenceReason.SICK_LEAVE, 'Arrêt maladie'],
      [MissionCrewAbsenceReason.TRAINING, 'Formation'],
      [MissionCrewAbsenceReason.RECOVERING, 'Récupération'],
      [MissionCrewAbsenceReason.HOLIDAYS, 'Congés'],
      [MissionCrewAbsenceReason.MEETING, 'Réunion'],
      [MissionCrewAbsenceReason.DISPATCHED_ELSEWHERE, 'Renforcement extérieur'],
      [MissionCrewAbsenceReason.OTHER, 'Autre']
    ])('renders correct label for %s reason', (reason, expectedLabel) => {
      const absence: MissionCrewAbsence = {
        isAbsentFullMission: true,
        reason
      }
      render(<CrewAbsenceTag absence={absence} />)
      expect(screen.getByText(expectedLabel)).toBeInTheDocument()
    })
  })
})
