import { render, screen } from '../../../test-utils'
import { getComponentForAction } from './action-mapping.ts'
import { ActionTypeEnum, MissionSourceEnum } from '../../../types/env-mission-types.ts'
import { Action } from '../../../types/action-types.ts' // Adjust the import path
import { vi } from 'vitest'

vi.mock('./action-control-env', () => ({ default: () => <div>Mock ActionControlEnv</div> }))
vi.mock('./action-surveillance-env', () => ({ default: () => <div>Mock ActionSurveillanceEnv</div> }))
vi.mock('./action-control-fish', () => ({ default: () => <div>Mock ActionControlFish</div> }))
vi.mock('./action-control-nav', () => ({ default: () => <div>Mock ActionControlNav</div> }))
vi.mock('./action-status-form', () => ({ default: () => <div>Mock ActionStatusForm</div> }))
vi.mock('./action-note-form', () => ({ default: () => <div>Mock ActionNoteForm</div> }))
vi.mock('./action-rescue-form', () => ({ default: () => <div>Mock ActionRescueForm</div> }))

describe('getComponentForAction', () => {
  test('returns null if no action', () => {
    const action = undefined
    const Component = getComponentForAction(action)
    expect(Component).toBeNull()
  })
  test('returns ActionControlEnv component for ENV CONTROL action', () => {
    const action = {
      type: ActionTypeEnum.CONTROL,
      source: MissionSourceEnum.MONITORENV
    } as Action
    const Component = getComponentForAction(action)
    render(<Component />)
    expect(screen.getByText('Mock ActionControlEnv')).toBeInTheDocument()
  })
  test('returns ActionSurveillanceEnv component for ENV CONTROL action', () => {
    const action = {
      type: ActionTypeEnum.SURVEILLANCE,
      source: MissionSourceEnum.MONITORENV
    } as Action
    const Component = getComponentForAction(action)
    render(<Component />)
    expect(screen.getByText('Mock ActionSurveillanceEnv')).toBeInTheDocument()
  })

  test('returns ActionControlFish component for FISH CONTROL action', () => {
    const action = {
      type: ActionTypeEnum.CONTROL,
      source: MissionSourceEnum.MONITORFISH
    } as Action
    const Component = getComponentForAction(action)
    render(<Component />)
    expect(screen.getByText('Mock ActionControlFish')).toBeInTheDocument()
  })
  test('returns ActionControlNav component for NAV CONTROL action', () => {
    const action = {
      type: ActionTypeEnum.CONTROL,
      source: MissionSourceEnum.RAPPORTNAV
    } as Action
    const Component = getComponentForAction(action)
    render(<Component />)
    expect(screen.getByText('Mock ActionControlNav')).toBeInTheDocument()
  })
  test('returns ActionStatusForm component for NAV STATUS action', () => {
    const action = {
      type: ActionTypeEnum.STATUS,
      source: MissionSourceEnum.RAPPORTNAV
    } as Action
    const Component = getComponentForAction(action)
    render(<Component />)
    expect(screen.getByText('Mock ActionStatusForm')).toBeInTheDocument()
  })
  test('returns ActionNoteForm component for NAV Note action', () => {
    const action = {
      type: ActionTypeEnum.NOTE,
      source: MissionSourceEnum.RAPPORTNAV
    } as Action
    const Component = getComponentForAction(action)
    render(<Component />)
    expect(screen.getByText('Mock ActionNoteForm')).toBeInTheDocument()
  })
  test('returns ActionRescueForm component for NAV Rescue action', () => {
    const action = {
      type: ActionTypeEnum.RESCUE,
      source: MissionSourceEnum.RAPPORTNAV
    } as Action
    const Component = getComponentForAction(action)
    render(<Component />)
    expect(screen.getByText('Mock ActionRescueForm')).toBeInTheDocument()
  })
})
