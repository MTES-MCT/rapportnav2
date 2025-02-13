import { render } from '../../../../../../test-utils'
import { MissionTimelineAction } from '../../../types/mission-timeline-output'
import MissionTimelineItemSurveillanceCardTitle from '../mission-timeline-item-surveillance-card-title'

describe('MissionTimelineItemSurveillanceCardTitle', () => {
  it('should match the snapshot', () => {
    const action: MissionTimelineAction = {
      formattedControlPlans: [
        {
          theme: 'Espèce protégée et leur habitat (faune et flore)',
          subThemes: ['Dérangement / perturbation intentionnelle des espèces animales protégées']
        },
        { theme: 'Pêche de loisir (autre que PAP)', subThemes: ['Pêche sous-marine', 'Pêche embarquée'] }
      ]
    } as MissionTimelineAction
    const wrapper = render(<MissionTimelineItemSurveillanceCardTitle action={action} />)
    expect(wrapper).toMatchSnapshot()
  })
})
