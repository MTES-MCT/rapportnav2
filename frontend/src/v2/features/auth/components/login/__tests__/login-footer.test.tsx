import { render } from '../../../../../../test-utils'
import LoginFooter from '../login-footer'
import { describe, it, expect } from 'vitest'

describe('LoginFooter', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<LoginFooter />)
    expect(wrapper).toMatchSnapshot()
  })
})
