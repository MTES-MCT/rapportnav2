import React from 'react'
import { Stack, Toggle } from 'rsuite'
import { Accent, Button, Size, THEME, MultiSelect, Textarea } from '@mtes-mct/monitor-ui'
import Text from '../../../ui/text'
import { Infraction } from '../../mission-types'
import { FormalNoticeEnum } from '../../env-mission-types'

interface InfractionFormProps {
  infraction?: Infraction
  availableNatinfs?: string[]
  onChange: (field: string, value: any) => void
  onCancel: () => void
}

const InfractionForm: React.FC<InfractionFormProps> = ({ infraction, availableNatinfs, onChange, onCancel }) => {
  return (
    <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
          <Stack.Item>
            {/* TODO add Toggle component to monitor-ui */}
            <Toggle
              checked={infraction?.formalNotice === FormalNoticeEnum.YES}
              defaultValue={FormalNoticeEnum.NO}
              role="toggle-formal-notice"
              size="sm"
              onChange={(checked: boolean) =>
                onChange('formalNotice', checked ? FormalNoticeEnum.YES : FormalNoticeEnum.NO)
              }
            />
          </Stack.Item>
          <Stack.Item>
            <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
              PV émis
            </Text>
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
          value={infraction?.observations}
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
            <Button accent={Accent.PRIMARY} type="submit" size={Size.NORMAL} role="validate-infraction">
              Valider l'infraction
            </Button>
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default InfractionForm
