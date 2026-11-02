import { render } from '../../../../../../test-utils'
import PublicHeader from '../public-header'
import { describe, it, expect } from 'vitest'

describe('PublicHeader', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<PublicHeader />)
    expect(wrapper).toMatchSnapshot()
  })
})
