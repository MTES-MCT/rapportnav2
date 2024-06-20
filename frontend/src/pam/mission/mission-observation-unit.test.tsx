import { beforeEach } from 'node:test'
import { vi } from 'vitest'
import { fireEvent, mockQueryResult, render, screen } from '../../test-utils.tsx'
import MissionObservationByUnit from './mission-observation-unit.tsx'
import useUpdateMissionEnv from './use-update-mission-env.tsx'

const updateMissionObservationMock = vi.fn()

vi.mock('./use-update-mission-env', () => ({
  default: () => [updateMissionObservationMock, { error: undefined }]
}))

describe('MissionObservation', () => {
  beforeEach(() => {
    ;(useUpdateMissionEnv as any).mockReturnValue(mockQueryResult({ id: 'myId' }))
  })
  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()
  })

  it('should render observation', () => {
    render(<MissionObservationByUnit missionId={1} observationsByUnit={'My beautiful observation'} />)
    expect(updateMissionObservationMock).not.toHaveBeenCalled()
    expect(screen.getByText('My beautiful observation')).toBeInTheDocument()
    expect(screen.getByText(`Observation générale à l'échelle de la mission (remarques, résumé)`)).toBeInTheDocument()
  })

  it('should call update observation', () => {
    const wrapper = render(<MissionObservationByUnit missionId={1} observationsByUnit={'My beautiful observation'} />)
    const element = wrapper.getByTestId('mission-general-observation')
    fireEvent.change(element, {
      target: { value: 'my new observations' }
    })
    expect(updateMissionObservationMock).toHaveBeenCalled()
  })

  it('should not call update observation under 4', () => {
    const wrapper = render(<MissionObservationByUnit missionId={1} observationsByUnit={'My beautiful observation'} />)
    const element = wrapper.getByTestId('mission-general-observation')
    fireEvent.change(element, {
      target: { value: 'my' }
    })
    expect(updateMissionObservationMock).not.toHaveBeenCalled()
  })
})
