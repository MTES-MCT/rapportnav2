import { render, screen } from '../../../../../../test-utils.tsx'
import MissionListCountTag from '../mission-list-count-tag.tsx'

describe('MissionListCountTag', () => {
  test('pluralizes "rapports" for several reports', () => {
    render(<MissionListCountTag count={3} />)
    expect(screen.getByText('3 rapports')).toBeInTheDocument()
  })

  test('uses the singular "rapport" for a single report', () => {
    render(<MissionListCountTag count={1} />)
    expect(screen.getByText('1 rapport')).toBeInTheDocument()
  })

  test('uses the singular form for zero', () => {
    render(<MissionListCountTag count={0} />)
    expect(screen.getByText('0 rapport')).toBeInTheDocument()
  })
})
