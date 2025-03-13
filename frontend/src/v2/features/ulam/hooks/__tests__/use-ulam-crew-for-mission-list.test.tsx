import { describe, expect, test } from 'vitest'
import { MissionCrew } from '@common/types/crew-types.ts'
import { useUlamCrewForMissionList } from '../use-ulam-crew-for-mission-list.tsx'

const missionCrews: MissionCrew[] = [
  {
    id: "1",
    agent: {
      id: "1",
      firstName: "John",
      lastName: "Doe",
      services: []
    }
  },
  {
    id: "2",
    agent: {
      id: "2",
      firstName: "Paul",
      lastName: "Sernine",
      services: []
    }
  }
]


describe('useUlamCrewForMissionList', () => {

  test('returns "--" if crewList is empty', () => {
    const { text } = useUlamCrewForMissionList([])
    expect(text).toBe('--')
  })

  test('returns fullName separated by comma if crewList contains agents', () => {
    const { text } = useUlamCrewForMissionList(missionCrews)
    expect(text).toBe('John Doe, Paul Sernine')
  })
})
