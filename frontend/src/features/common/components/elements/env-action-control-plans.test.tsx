import React from 'react'
import { render, screen } from '../../../../test-utils.tsx'
import { describe, it, expect } from 'vitest'
import EnvActionControlPlans from './env-action-control-plans.tsx'
import { FormattedControlPlan } from '@common/types/env-mission-types'

describe('EnvActionControlPlans', () => {
  it('renders nothing when controlPlans is undefined', () => {
    render(<EnvActionControlPlans />)
    expect(screen.queryByTestId('theme')).toBeNull()
  })

  it('renders a single control plan correctly', () => {
    const controlPlans: FormattedControlPlan[] = [
      {
        theme: 'Test Theme',
        subThemes: ['Sub Theme 1', 'Sub Theme 2']
      }
    ]

    render(<EnvActionControlPlans controlPlans={controlPlans} />)

    expect(screen.getByText('Thématique de contrôle')).toBeInTheDocument()
    expect(screen.getByText('Test Theme')).toBeInTheDocument()
    expect(screen.getByText('Sous-thématique(s) de contrôle')).toBeInTheDocument()
    expect(screen.getByText('Sub Theme 1, Sub Theme 2')).toBeInTheDocument()
  })

  it('renders multiple control plans correctly', () => {
    const controlPlans: FormattedControlPlan[] = [
      {
        theme: 'Theme 1',
        subThemes: ['Sub Theme 1']
      },
      {
        theme: 'Theme 2',
        subThemes: ['Sub Theme 2', 'Sub Theme 3']
      }
    ]

    render(<EnvActionControlPlans controlPlans={controlPlans} />)

    expect(screen.getByText('Thématique de contrôle (1)')).toBeInTheDocument()
    expect(screen.getByText('Theme 1')).toBeInTheDocument()
    expect(screen.getByText('Sub Theme 1')).toBeInTheDocument()

    expect(screen.getByText('Thématique de contrôle (2)')).toBeInTheDocument()
    expect(screen.getByText('Theme 2')).toBeInTheDocument()
    expect(screen.getByText('Sub Theme 2, Sub Theme 3')).toBeInTheDocument()
  })

  it('displays "inconnues" when subThemes is empty', () => {
    const controlPlans: FormattedControlPlan[] = [
      {
        theme: 'Test Theme',
        subThemes: []
      }
    ]

    render(<EnvActionControlPlans controlPlans={controlPlans} />)

    expect(screen.getByText('inconnues')).toBeInTheDocument()
  })
})
