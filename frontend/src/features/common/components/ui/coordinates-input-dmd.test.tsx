import { render, screen } from '../../../../test-utils'
import { CoordinateInputDMD } from './coordonates-input-dmd'

describe('Coordinates input DMD', () => {
  it('should display coordonate with place holder', () => {
    render(<CoordinateInputDMD label="My beautiful label" name={'geoCoords'} />)
    expect(screen.getByText('My beautiful label (12° 12.12′ N 121° 21.21′ E)')).toBeInTheDocument()
  })
})
