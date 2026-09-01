import { render } from '../../../../../../test-utils'
import LoginHeader from '../login-header'
import { describe, it, expect } from 'vitest'

describe('LoginHeader', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<LoginHeader />)
    expect(wrapper).toMatchSnapshot()
  })
})
