import { beforeEach } from 'node:test'
import { vi } from 'vitest'
import { fireEvent, mockQueryResult, render, screen } from '../../test-utils.tsx'
import MissionObservationByUnit from './mission-observation-unit.tsx'
import usePatchMissionEnv from './use-patch-mission-env.tsx'

const patchMissionObservationMock = vi.fn()

vi.mock('./use-patch-mission-env', () => ({
  default: () => [patchMissionObservationMock, { error: undefined }]
}))

describe('MissionObservation', () => {
  beforeEach(() => {
    ;(usePatchMissionEnv as any).mockReturnValue(mockQueryResult({ id: 'myId' }))
  })
  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()
  })

  it('should render observation', () => {
    render(<MissionObservationByUnit missionId={1} observationsByUnit={'My beautiful observation'} />)
    expect(patchMissionObservationMock).not.toHaveBeenCalled()
    expect(screen.getByText('My beautiful observation')).toBeInTheDocument()
    expect(screen.getByText(`Observation générale à l'échelle de la mission (remarques, résumé)`)).toBeInTheDocument()
  })

  it('should call update observation', () => {
    const wrapper = render(<MissionObservationByUnit missionId={1} observationsByUnit={'My beautiful observation'} />)
    const element = wrapper.getByTestId('mission-general-observation')
    fireEvent.change(element, {
      target: { value: 'my new observations' }
    })
    expect(patchMissionObservationMock).toHaveBeenCalled()
  })

  it('should not call update observation under 4', () => {
    const wrapper = render(<MissionObservationByUnit missionId={1} observationsByUnit={'My beautiful observation'} />)
    const element = wrapper.getByTestId('mission-general-observation')
    fireEvent.change(element, {
      target: { value: 'my' }
    })
    expect(patchMissionObservationMock).not.toHaveBeenCalled()
  })
})
