import React, { useState } from 'react'
import styled from 'styled-components'
import { FlexboxGrid, Stack, Toggle } from 'rsuite'
import { Accent, Icon, Button, Size, THEME, Label, MultiSelect, Textarea } from '@mtes-mct/monitor-ui'
import Title from '../../../ui/title'
import { Infraction } from '../../mission-types'
import { FormalNoticeEnum } from '../../env-mission-types'

interface InfractionFormProps {
  data?: Infraction
  availableNatinfs?: string[]
  onSubmit: (data: any) => void
  onCancel: () => void
}

const InfractionForm: React.FC<InfractionFormProps> = ({ data, availableNatinfs, onSubmit, onCancel }) => {
  const [formData, setFormData] = useState<Infraction | undefined>(data)

  const onChange = (field: string, value: any) => {
    setFormData((prevData: any) => ({ ...prevData, [field]: value }))
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // Handle form submission
    // You can also validate the form data before submitting
    onSubmit(formData)
  }

  return (
    <form onSubmit={handleSubmit}>
      <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
            <Stack.Item>
              {/* TODO add Toggle component to monitor-ui */}
              <Toggle
                checked={formData?.formalNotice === true}
                size="sm"
                onChange={(checked: boolean) => onChange('formalNotice', checked)}
              />
            </Stack.Item>
            <Stack.Item>
              <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
                PV émis
              </Title>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <MultiSelect
            error=""
            label="NATINF"
            name="natinfs"
            onChange={function noRefCheck() {}}
            options={[
              {
                label: 'First Option',
                value: 'FIRST_OPTION'
              },
              {
                label: 'Second Option',
                value: 'SECOND_OPTION'
              },
              {
                label: 'Third Option',
                value: 'THIRD_OPTION'
              }
            ]}
            placeholder=""
            searchable
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Textarea
            label="Observations générales sur le contrôle"
            value={formData?.observations}
            name="observations"
            onChange={(nextValue?: string) => onChange('observations', nextValue)}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Stack justifyContent="flex-end" spacing={'1rem'} style={{ width: '100%' }}>
            <Stack.Item>
              <Button accent={Accent.TERTIARY} type="submit" size={Size.NORMAL} onClick={onCancel}>
                Annuler
              </Button>
            </Stack.Item>
            <Stack.Item>
              <Button accent={Accent.PRIMARY} type="submit" size={Size.NORMAL}>
                Valider l'infraction
              </Button>
            </Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>
    </form>
  )
}

export default InfractionForm
