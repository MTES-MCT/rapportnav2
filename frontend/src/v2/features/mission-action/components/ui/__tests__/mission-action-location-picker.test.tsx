import { describe, expect, it } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import { LocationType } from '../../../../common/types/location-type'
import MissionActionLocationPicker from '../mission-action-location-picker'

describe('MissionActionLocationPicker', () => {
  it('renders the location type select', () => {
    render(<MissionActionLocationPicker />)
    expect(screen.getByText("Lieu de l'operation")).toBeInTheDocument()
  })

  it('renders coordinate input when locationType is GPS', () => {
    render(<MissionActionLocationPicker />, {
      formikValues: { locationType: LocationType.GPS, geoCoords: [48.8566, 2.3522] }
    })
    expect(screen.getByText("Lieu de l'operation")).toBeInTheDocument()
  })

  it('renders port search when locationType is PORT', () => {
    render(<MissionActionLocationPicker />, {
      formikValues: { locationType: LocationType.PORT }
    })
    expect(screen.getByText('Nom du port')).toBeInTheDocument()
  })

  it('renders city search when locationType is COMMUNE', () => {
    render(<MissionActionLocationPicker />, {
      formikValues: { locationType: LocationType.COMMUNE }
    })
    expect(screen.getByText('Nom de la commune')).toBeInTheDocument()
  })

  it('does not render port or city inputs when locationType is GPS', () => {
    render(<MissionActionLocationPicker />, {
      formikValues: { locationType: LocationType.GPS, geoCoords: [48.8566, 2.3522] }
    })
    expect(screen.queryByText('Nom du port')).not.toBeInTheDocument()
    expect(screen.queryByText('Nom de la commune')).not.toBeInTheDocument()
  })

  it('does not render coordinate or city inputs when locationType is PORT', () => {
    render(<MissionActionLocationPicker />, {
      formikValues: { locationType: LocationType.PORT }
    })
    expect(screen.queryByText('Nom de la commune')).not.toBeInTheDocument()
  })

  it('does not render coordinate or port inputs when locationType is COMMUNE', () => {
    render(<MissionActionLocationPicker />, {
      formikValues: { locationType: LocationType.COMMUNE }
    })
    expect(screen.queryByText('Nom du port')).not.toBeInTheDocument()
  })
})