// missionOpenByTag.test.js

import React from 'react'
import { render, screen } from '../../test-utils'

import MissionOpenByTag from './mission-open-by-tag'
import { MissionSourceEnum } from '../../types/env-mission-types'

describe('MissionOpenByTag component', () => {
  test('renders "Ouverte par l\'unité" with PRIMARY accent when missionSource is RAPPORTNAV', () => {
    render(<MissionOpenByTag missionSource={MissionSourceEnum.RAPPORTNAV} />)
    const tagElement = screen.getByText("Ouverte par l'unité")
    expect(tagElement).toBeInTheDocument()
    expect(getComputedStyle(tagElement).backgroundColor).toBe('rgb(229, 229, 235)')
  })

  test('renders "Ouverte par le CACEM" with SECONDARY accent when missionSource is MONITORENV', () => {
    render(<MissionOpenByTag missionSource={MissionSourceEnum.MONITORENV} />)
    const tagElement = screen.getByText('Ouverte par le CACEM')
    expect(getComputedStyle(tagElement).backgroundColor).toBe('rgb(246, 208, 18)')
  })
  test('renders "Ouverte par le CACEM" with SECONDARY accent when missionSource is POSEIDON_CACEM', () => {
    render(<MissionOpenByTag missionSource={MissionSourceEnum.POSEIDON_CACEM} />)
    const tagElement = screen.getByText('Ouverte par le CACEM')
    expect(getComputedStyle(tagElement).backgroundColor).toBe('rgb(246, 208, 18)')
  })
  test('renders "Ouverte par le CNSP" with SECONDARY accent when missionSource is not MONITORFISH', () => {
    render(<MissionOpenByTag missionSource={MissionSourceEnum.MONITORFISH} />)
    const tagElement = screen.getByText('Ouverte par le CNSP')
    expect(getComputedStyle(tagElement).backgroundColor).toBe('rgb(246, 208, 18)')
  })
  test('renders "Ouverte par le CNSP" with SECONDARY accent when missionSource is not POSEIDON_CNSP', () => {
    render(<MissionOpenByTag missionSource={MissionSourceEnum.POSEIDON_CNSP} />)
    const tagElement = screen.getByText('Ouverte par le CNSP')
    expect(getComputedStyle(tagElement).backgroundColor).toBe('rgb(246, 208, 18)')
  })
  test('renders "Ouverte par N/A" with SECONDARY accent when missionSource is unknown or unexisting', () => {
    render(<MissionOpenByTag missionSource={null as any} />)
    const tagElement = screen.getByText('Ouverte par N/A')
    expect(getComputedStyle(tagElement).backgroundColor).toBe('rgb(246, 208, 18)')
  })
})
