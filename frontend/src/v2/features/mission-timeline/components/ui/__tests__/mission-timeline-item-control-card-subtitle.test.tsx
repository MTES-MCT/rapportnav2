import { ActionTargetTypeEnum } from '@common/types/env-mission-types'
import { render, screen } from '../../../../../../test-utils'
import { MissionSourceEnum } from '../../../../common/types/mission-types'
import { MissionTimelineAction } from '../../../types/mission-timeline-output'
import MissionTimelineItemControlCardSubtitle from '../mission-timeline-item-control-card-Subtitle'

describe('MissionTimelineItemControlCardSubtitle', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <MissionTimelineItemControlCardSubtitle
        action={
          {
            actionNumberOfControls: 5,
            source: MissionSourceEnum.MONITORENV,
            actionTargetType: ActionTargetTypeEnum.VEHICLE
          } as MissionTimelineAction
        }
      />
    )
    expect(wrapper).toMatchSnapshot()
  })

  it('should not display if source is not env', () => {
    render(
      <MissionTimelineItemControlCardSubtitle
        action={{ actionNumberOfControls: 5, source: MissionSourceEnum.MONITORFISH } as MissionTimelineAction}
      />
    )
    expect(screen.queryByText('sur des cibles de type')).not.toBeInTheDocument()
  })

  it('should display nombre de control inconnus', () => {
    render(
      <MissionTimelineItemControlCardSubtitle
        action={{ actionNumberOfControls: 0, source: MissionSourceEnum.MONITORENV } as MissionTimelineAction}
      />
    )
    expect(screen.getByText('Nombre de contrôles inconnu')).toBeInTheDocument()
  })

  it('should display le nombre de controls au singulier', () => {
    render(
      <MissionTimelineItemControlCardSubtitle
        action={{ actionNumberOfControls: 1, source: MissionSourceEnum.MONITORENV } as MissionTimelineAction}
      />
    )
    expect(screen.getByText('1 contrôle')).toBeInTheDocument()
    expect(screen.getByText(`réalisé sur des cibles de type`)).toBeInTheDocument()
  })

  it('should display le nombre de controls au pluriel', () => {
    render(
      <MissionTimelineItemControlCardSubtitle
        action={{ actionNumberOfControls: 5, source: MissionSourceEnum.MONITORENV } as MissionTimelineAction}
      />
    )
    expect(screen.getByText('5 contrôles')).toBeInTheDocument()
    expect(screen.getByText(`réalisés sur des cibles de type`)).toBeInTheDocument()
  })
})
