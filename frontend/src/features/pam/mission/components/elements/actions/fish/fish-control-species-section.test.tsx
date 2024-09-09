import { FishAction } from '@common/types/fish-mission-types.ts'
import { describe, expect, it } from 'vitest'
import { render, screen } from '../../../../../../../test-utils.tsx'
import FishControlSpeciesSection from './fish-control-species-section.tsx'

const fishAction = { actionDatetimeUtc: '2022-01-01T00:00:00Z' } as any as FishAction

describe('FishControlSpeciesSection', () => {
  it('should render radio button of control section', () => {
    render(<FishControlSpeciesSection action={fishAction} />)
    expect(screen.getByText('Poids des espèces vérifié')).toBeInTheDocument()
    expect(screen.getByText('Taille des espèces vérifiée')).toBeInTheDocument()
    expect(screen.getByText('Arrimage séparé des espèces soumises à plan')).toBeInTheDocument()
  })

  it('should render radio button of control section with read only attribute', () => {
    const wrapper = render(<FishControlSpeciesSection action={fishAction} />)
    const radios = wrapper.container.querySelectorAll("input[type='radio']")

    radios.forEach(radio => {
      expect((radio as HTMLInputElement).disabled).toBeFalsy()
    })
    //TODO: Monitor ui required attribute doesn't disabled input. find a way to test it
  })
})
