import { MissionActionType } from '@common/types/fish-mission-types.ts'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import { hasSatiPdf, useSati } from '../../../../sati/hooks/use-sati.tsx'
import { ActionType } from '../../../types/action-type.ts'
import { MissionAction } from '../../../types/mission-action.ts'
import { MissionSourceEnum } from '../../../types/mission-types.ts'
import { OwnerType } from '../../../types/owner-type.ts'
import ActionHeaderAction from '../action-header-action.tsx'

vi.mock('../../../../sati/components/ui/sati-pdf-export-button.tsx', () => ({
  default: ({ ownerId, actionId, disabled }: { ownerId: string; actionId: string; disabled?: boolean }) => (
    <button data-testid="sati-pdf-export-button" disabled={disabled}>{`${ownerId}/${actionId}`}</button>
  )
}))

vi.mock('../../../../sati/hooks/use-sati.tsx', async importOriginal => ({
  ...(await importOriginal<typeof import('../../../../sati/hooks/use-sati.tsx')>()),
  useSati: vi.fn()
}))

const mockSatiEnabled = (isSatiEnabled: boolean) =>
  vi.mocked(useSati).mockReturnValue({
    isSatiEnabled,
    canExportSatiPdf: action => isSatiEnabled && hasSatiPdf(action)
  })

const renderHeader = (
  source: MissionSourceEnum,
  actionType: ActionType,
  isCompleteForStats?: boolean,
  fishActionType: MissionActionType = MissionActionType.SEA_CONTROL
) =>
  render(
    <ActionHeaderAction
      action={
        { id: '12345', source, actionType, isCompleteForStats, data: { fishActionType } } as unknown as MissionAction
      }
      actionId="12345"
      ownerId="761"
      ownerType={OwnerType.MISSION}
    />
  )

describe('ActionHeaderAction', () => {
  beforeEach(() => {
    mockSatiEnabled(true)
  })

  describe('for a fish control with a SATI', () => {
    it('shows an enabled SATI export button next to the delete button when complete for stats', () => {
      renderHeader(MissionSourceEnum.MONITORFISH, ActionType.CONTROL, true)

      expect(screen.getByTestId('sati-pdf-export-button')).toHaveTextContent('761/12345')
      expect(screen.getByTestId('sati-pdf-export-button')).toBeEnabled()
      expect(screen.getByTestId('deleteButton')).toBeDisabled()
    })

    it.each([false, undefined])('disables the export when isCompleteForStats is %s', isCompleteForStats => {
      renderHeader(MissionSourceEnum.MONITORFISH, ActionType.CONTROL, isCompleteForStats)

      expect(screen.getByTestId('sati-pdf-export-button')).toBeDisabled()
    })

    it('shows no SATI export button when SATI is not enabled for the service', () => {
      mockSatiEnabled(false)

      renderHeader(MissionSourceEnum.MONITORFISH, ActionType.CONTROL, true)

      expect(screen.queryByTestId('sati-pdf-export-button')).not.toBeInTheDocument()
      expect(screen.getByTestId('deleteButton')).toBeDisabled()
    })
  })

  describe('for any other action', () => {
    it('shows the delete button for a fish air control, which has no SATI', () => {
      renderHeader(MissionSourceEnum.MONITORFISH, ActionType.CONTROL, true, MissionActionType.AIR_CONTROL)

      expect(screen.queryByTestId('sati-pdf-export-button')).not.toBeInTheDocument()
      expect(screen.getByTestId('deleteButton')).toBeInTheDocument()
    })

    it.each([
      [MissionSourceEnum.MONITORENV, ActionType.CONTROL],
      [MissionSourceEnum.RAPPORT_NAV, ActionType.CONTROL],
      [MissionSourceEnum.MONITORFISH, ActionType.SURVEILLANCE]
    ])('shows the delete button and no export button for %s / %s', (source, actionType) => {
      renderHeader(source, actionType, true)

      expect(screen.queryByTestId('sati-pdf-export-button')).not.toBeInTheDocument()
      expect(screen.getByTestId('deleteButton')).toBeInTheDocument()
    })

    it('only enables delete for RapportNav actions', () => {
      renderHeader(MissionSourceEnum.RAPPORT_NAV, ActionType.CONTROL)

      expect(screen.getByTestId('deleteButton')).toBeEnabled()
    })
  })
})
