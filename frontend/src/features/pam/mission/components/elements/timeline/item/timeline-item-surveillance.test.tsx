import { render, screen } from '../../../../../../../test-utils.tsx'
import { ActionTypeEnum, EnvAction, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import { Action, ActionStatusType } from '@common/types/action-types.ts'
import ActionEnvSurveillance from './timeline-item-surveillance.tsx'

const actionMock = {
  id: '1',
  missionId: 1,
  type: ActionTypeEnum.SURVEILLANCE,
  source: MissionSourceEnum.MONITORENV,
  status: ActionStatusType.UNKNOWN,
  startDateTimeUtc: '2022-01-01T00:00:00Z',
  endDateTimeUtc: '2022-01-01T01:00:00Z',
  data: {
    actionType: ActionTypeEnum.SURVEILLANCE,
    observations: null,
    geom: 'MULTIPOINT ((-8.52318191 48.30305604))',
    formattedControlPlans: {
      subThemes: ['subtheme1', 'subtheme2'],
      themes: ['environnement - police mouillage']
    }
  } as any as EnvAction
}

const props = (action: Action = actionMock, onClick = vi.fn()) => ({
  action,
  onClick
})
describe('ActionEnvSurveillance', () => {
  test('should render', () => {
    render(<ActionEnvSurveillance {...props()} />)
    expect(screen.getByText('ajouté par CACEM')).toBeInTheDocument()
  })
  describe('the title', () => {
    test('should render the theme', () => {
      render(<ActionEnvSurveillance {...props()} />)
      expect(screen.getByText('environnement - police mouillage')).toBeInTheDocument()
    })
    test('should render empty text when no theme', () => {
      const mock = {
        ...actionMock,
        data: {
          ...actionMock.data,
          formattedControlPlans: [
            {
              themes: undefined
            }
          ]
        }
      }
      render(<ActionEnvSurveillance {...props(mock)} />)
      expect(screen.getByTestId('theme').textContent).toEqual('environnement marin')
    })
  })
})
