import { ControlMethod } from '@common/types/control-types'
import { VesselTypeEnum } from '../../../../common/types/vessel-type'
import { render, screen } from '../../../../../../test-utils'
import { ActionType } from '../../../../common/types/action-type'
import { MissionTimelineAction } from '../../../types/mission-timeline-output'
import MissionTimelineItemNavControlCardTitle from '../mission-timeline-item-nav-control-card-title'

describe('MissionTimelineItemNavControlCardTitle', () => {
  it('should render nothing when action is undefined', () => {
    const { container } = render(<MissionTimelineItemNavControlCardTitle action={undefined} />)
    expect(container.firstChild).toBeNull()
  })

  it('should render nothing when action type is undefined', () => {
    const action = {} as MissionTimelineAction
    const { container } = render(<MissionTimelineItemNavControlCardTitle action={action} />)
    expect(container.firstChild).toBeNull()
  })

  it('should render nothing when action type is not in registry', () => {
    const action: MissionTimelineAction = {
      type: ActionType.NOTE
    } as MissionTimelineAction
    const { container } = render(<MissionTimelineItemNavControlCardTitle action={action} />)
    expect(container.firstChild).toBeNull()
  })

  describe('ActionType.OTHER_CONTROL', () => {
    it('should render "Contrôles Autre"', () => {
      const action: MissionTimelineAction = {
        type: ActionType.OTHER_CONTROL
      } as MissionTimelineAction
      render(<MissionTimelineItemNavControlCardTitle action={action} />)
      expect(screen.getByText('Contrôles')).toBeInTheDocument()
      expect(screen.getByText('Autre')).toBeInTheDocument()
    })
  })

  describe('ActionType.CONTROL_SLEEPING_FISHING_GEAR', () => {
    it('should render "Contrôles d\'engins de pêche dormant"', () => {
      const action: MissionTimelineAction = {
        type: ActionType.CONTROL_SLEEPING_FISHING_GEAR
      } as MissionTimelineAction
      render(<MissionTimelineItemNavControlCardTitle action={action} />)
      expect(screen.getByText('Contrôles')).toBeInTheDocument()
      expect(screen.getByText("d'engins de pêche dormant")).toBeInTheDocument()
    })
  })

  describe('ActionType.CONTROL_NAUTICAL_LEISURE', () => {
    it('should render "Contrôles de loisirs nautiques"', () => {
      const action: MissionTimelineAction = {
        type: ActionType.CONTROL_NAUTICAL_LEISURE
      } as MissionTimelineAction
      render(<MissionTimelineItemNavControlCardTitle action={action} />)
      expect(screen.getByText('Contrôles')).toBeInTheDocument()
      expect(screen.getByText('de loisirs nautiques')).toBeInTheDocument()
    })
  })

  describe('ActionType.CONTROL', () => {
    it('should render control method and vessel type', () => {
      const action: MissionTimelineAction = {
        type: ActionType.CONTROL,
        controlMethod: ControlMethod.SEA,
        vesselType: VesselTypeEnum.COMMERCIAL
      } as MissionTimelineAction
      render(<MissionTimelineItemNavControlCardTitle action={action} />)
      expect(screen.getByText('Contrôles')).toBeInTheDocument()
      expect(screen.getByText('en Mer - Navire de commerce')).toBeInTheDocument()
    })

    it('should render AIR control method', () => {
      const action: MissionTimelineAction = {
        type: ActionType.CONTROL,
        controlMethod: ControlMethod.AIR,
        vesselType: VesselTypeEnum.SAILING
      } as MissionTimelineAction
      render(<MissionTimelineItemNavControlCardTitle action={action} />)
      expect(screen.getByText('aérien - Navire de plaisance professionnelle')).toBeInTheDocument()
    })

    it('should render LAND control method', () => {
      const action: MissionTimelineAction = {
        type: ActionType.CONTROL,
        controlMethod: ControlMethod.LAND,
        vesselType: VesselTypeEnum.MOTOR
      } as MissionTimelineAction
      render(<MissionTimelineItemNavControlCardTitle action={action} />)
      expect(screen.getByText('à Terre - Navire de services (travaux...)')).toBeInTheDocument()
    })

    it('should handle missing control method and vessel type', () => {
      const action: MissionTimelineAction = {
        type: ActionType.CONTROL
      } as MissionTimelineAction
      render(<MissionTimelineItemNavControlCardTitle action={action} />)
      expect(screen.getByText('Contrôles')).toBeInTheDocument()
      expect(screen.getByText('-')).toBeInTheDocument()
    })
  })

  describe('ActionType.CONTROL_SECTOR_FISHING', () => {
    it('should render "Contrôles d\'établissement - filière pêche"', () => {
      const action: MissionTimelineAction = {
        type: ActionType.CONTROL_SECTOR_FISHING
      } as MissionTimelineAction
      render(<MissionTimelineItemNavControlCardTitle action={action} />)
      expect(screen.getByText('Contrôles')).toBeInTheDocument()
      expect(screen.getByText("d'établissement - filière pêche")).toBeInTheDocument()
    })
  })

  describe('ActionType.CONTROL_SECTOR_PLAISANCE', () => {
    it('should render "Contrôles d\'établissement - filière plaisance"', () => {
      const action: MissionTimelineAction = {
        type: ActionType.CONTROL_SECTOR_PLAISANCE
      } as MissionTimelineAction
      render(<MissionTimelineItemNavControlCardTitle action={action} />)
      expect(screen.getByText('Contrôles')).toBeInTheDocument()
      expect(screen.getByText("d'établissement - filière plaisance")).toBeInTheDocument()
    })
  })

  describe('ActionType.CONTROL_ROADSIDE', () => {
    it('should render "Contrôles routier"', () => {
      const action: MissionTimelineAction = {
        type: ActionType.CONTROL_ROADSIDE
      } as MissionTimelineAction
      render(<MissionTimelineItemNavControlCardTitle action={action} />)
      expect(screen.getByText('Contrôles')).toBeInTheDocument()
      expect(screen.getByText('routier')).toBeInTheDocument()
    })
  })

  describe('legacy ActionType.CONTROL_SECTOR', () => {
    it('should render nothing: it was split and is no longer in the registry', () => {
      const action: MissionTimelineAction = {
        type: ActionType.CONTROL_SECTOR
      } as MissionTimelineAction
      const { container } = render(<MissionTimelineItemNavControlCardTitle action={action} />)
      expect(container.firstChild).toBeNull()
    })
  })
})