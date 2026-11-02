import { render } from '../../../../../../test-utils'
import PublicFooter from '../public-footer'
import { describe, it, expect } from 'vitest'

describe('PublicFooter', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<PublicFooter />)
    expect(wrapper).toMatchSnapshot()
  })

  it('should render the DSFR bottom links in order', () => {
    const { container } = render(<PublicFooter />)
    const links = Array.from(container.querySelectorAll('.fr-footer__bottom-link')).map(a => [
      a.textContent,
      a.getAttribute('href')
    ])
    expect(links).toEqual([
      ['Plan du site', '/plan-du-site'],
      ['Accessibilité : non conforme', '/accessibilite'],
      ['Mentions légales', '/mentions-legales'],
      ['Données personnelles', '/donnees-personnelles']
    ])
  })
})
