import { render, screen } from '../../../../../../test-utils'
import { MissionTimelineAction } from '../../../types/mission-timeline-output'
import MissionTimelineItemFishControlCardTitle from '../mission-timeline-item-fish-control-card-title.tsx'
import { MissionActionType } from '@common/types/fish-mission-types.ts'

describe('MissionTimelineItemFishControlCardTitle', () => {
  const baseAction: Partial<MissionTimelineAction> = {
    fishActionType: MissionActionType.SEA_CONTROL,
    vesselName: 'Poseidon'
  }

  it('shows mandatory fields (action type + vessel name)', () => {
    render(<MissionTimelineItemFishControlCardTitle action={baseAction as MissionTimelineAction} />)

    expect(screen.getByText('Contrôle en mer - Poseidon', { exact: false })).toBeInTheDocument()
  })

  it('includes externalReferenceNumber when present', () => {
    const action = {
      ...baseAction,
      externalReferenceNumber: 'REF-123'
    } as MissionTimelineAction

    render(<MissionTimelineItemFishControlCardTitle action={action} />)

    expect(screen.getByText('REF-123', { exact: false })).toBeInTheDocument()
  })

  it('includes flagState when present', () => {
    const action = {
      ...baseAction,
      flagState: 'FR'
    } as MissionTimelineAction

    render(<MissionTimelineItemFishControlCardTitle action={action} />)

    expect(screen.getByText('FR', { exact: false })).toBeInTheDocument()
  })

  it('includes both optional fields when present', () => {
    const action = {
      ...baseAction,
      externalReferenceNumber: 'REF-999',
      flagState: 'FR'
    } as MissionTimelineAction

    render(<MissionTimelineItemFishControlCardTitle action={action} />)

    expect(screen.getByText('Contrôle en mer - Poseidon - FR - REF-999')).toBeInTheDocument()
  })

  it('does not include externalReferenceNumber when null', () => {
    const action = {
      ...baseAction,
      externalReferenceNumber: null
    } as MissionTimelineAction

    render(<MissionTimelineItemFishControlCardTitle action={action} />)

    const text = screen.getByTestId('mission-timeline-item-card-title').textContent
    expect(text).not.toContain('null')
  })

  it('does not include flagState when null', () => {
    const action = {
      ...baseAction,
      flagState: null
    } as MissionTimelineAction

    render(<MissionTimelineItemFishControlCardTitle action={action} />)

    const text = screen.getByTestId('mission-timeline-item-card-title').textContent
    expect(text).not.toContain('null')
  })

  it('does not render awkward separators when optional fields missing', () => {
    const action = {
      ...baseAction,
      externalReferenceNumber: undefined,
      flagState: undefined
    } as MissionTimelineAction

    render(<MissionTimelineItemFishControlCardTitle action={action} />)

    const text = screen.getByTestId('mission-timeline-item-card-title').textContent

    // Should be exactly two items joined by " - "
    expect(text).toBe('Contrôle en mer - Poseidon ')
  })
})
