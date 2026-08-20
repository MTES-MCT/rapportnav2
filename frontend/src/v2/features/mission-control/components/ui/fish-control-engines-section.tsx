import Text from '@common/components/ui/text'
import { GearControl, WireType } from '@common/types/fish-mission-types.ts'
import { Checkbox, Label, MultiRadio, THEME } from '@mtes-mct/monitor-ui'
import { isEmpty } from 'lodash'
import React from 'react'
import { Stack } from 'rsuite'
import { usecontrolCheck } from '../../../common/hooks/use-control-check'
import { MissionFishActionData } from '../../../common/types/mission-action'

interface FishControlEnginesSectionProps {
  action: MissionFishActionData
}

const WIRE_TYPE_LABELS: Record<WireType, string> = {
  [WireType.SINGLE]: 'Simple',
  [WireType.MANY]: 'Multiple'
}

const FishControlEnginesSection: React.FC<FishControlEnginesSectionProps> = ({ action }) => {
  const { controlCheckRadioOptions, controlCheckRadioBooleanOptions } = usecontrolCheck()
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
      <Stack.Item>
        <Label>Inspection des engins</Label>
      </Stack.Item>
      {action?.gearOnboard?.map((gearControl: GearControl, i: number) => (
        <Stack.Item
          style={{ backgroundColor: THEME.color.white, width: '100%', border: `1px dashed ${THEME.color.lightGray}` }}
          key={`${gearControl.gearCode}${i}`}
        >
          <Stack direction="column" alignItems="flex-start" spacing={'1rem'} style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%', padding: '1rem 1rem 0' }}>
              <div style={{ backgroundColor: THEME.color.lightGray, padding: '0.5rem 1rem', width: 'fit-content' }}>
                <Text as="h3" weight="medium">
                  {`${gearControl.gearCode} – ${gearControl.gearName}`}
                </Text>
              </div>
            </Stack.Item>

            <Stack.Item style={{ padding: '0 1rem' }}>
              <MultiRadio
                isInline
                isRequired
                readOnly={true}
                name="gearWasControlled"
                label="Engin contrôlé"
                onChange={function noRefCheck() {}}
                options={controlCheckRadioBooleanOptions}
                value={gearControl?.gearWasControlled ?? undefined}
              />
            </Stack.Item>

            <Stack.Item style={{ padding: '0 1rem' }}>
              <MultiRadio
                isInline
                isRequired
                readOnly={true}
                name="gearMarkingIsCompliant"
                label="Marquage de l’engin conforme"
                onChange={function noRefCheck() {}}
                options={controlCheckRadioOptions}
                value={gearControl?.gearMarkingIsCompliant ?? undefined}
              />
            </Stack.Item>

            <Stack.Item style={{ padding: '0 1rem' }}>
              <Stack direction="row" alignItems="flex-start" spacing={'2rem'}>
                <Stack.Item>
                  <Label>Maillage déclaré</Label>
                  <Text as="h3" weight="medium">
                    {`${gearControl.declaredMesh ?? ''} mm`}
                  </Text>
                </Stack.Item>
                <Stack.Item>
                  <Label>Maillage mesuré</Label>
                  <Text as="h3" weight="medium">
                    {`${gearControl.controlledMesh ?? ''} mm`}
                  </Text>
                </Stack.Item>
                <Stack.Item>
                  <Label>Ep. moyenne de fil</Label>
                  <Text as="h3" weight="medium">
                    {`${gearControl.averageWireThickness ?? ''} mm`}
                  </Text>
                </Stack.Item>
                <Stack.Item>
                  <Label>Type de fil</Label>
                  <Text as="h3" weight="medium">
                    {gearControl.wireType ? WIRE_TYPE_LABELS[gearControl.wireType] : undefined}
                  </Text>
                </Stack.Item>
              </Stack>
            </Stack.Item>

            {gearControl.hasUncontrolledMesh && (
              <Stack.Item style={{ padding: '0 1rem' }}>
                <Checkbox readOnly={true} name="hasUncontrolledMesh" label="Maillage non mesuré" checked={true} />
              </Stack.Item>
            )}

            {!isEmpty(gearControl?.comments) && (
              <Stack.Item style={{ padding: '0 1rem 1rem' }}>
                <Label>{`${gearControl.gearCode} : autres mesures et dispositifs`}</Label>
                <Text as="h3" weight="medium">
                  {gearControl.comments}
                </Text>
              </Stack.Item>
            )}
          </Stack>
        </Stack.Item>
      ))}
    </Stack>
  )
}

export default FishControlEnginesSection
