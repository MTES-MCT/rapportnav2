import { ControlType } from '@common/types/control-types.ts'
import { ActionTargetTypeEnum } from '@common/types/env-mission-types.ts'
import { InfractionByTarget } from '@common/types/infraction-types.ts'
import { Accent, Button, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import React, { useState } from 'react'
import { Stack } from 'rsuite'
import MissionNatinfTag from '../../../common/components/ui/mission-natinfs-tag.tsx'
import MissionInfractionEnvSummaryByCacemForm from '../ui/mission-infraction-env-summary-by-cacem.tsx'
import MissionInfractionEnvTitle from '../ui/mission-infraction-env-title.tsx'
import MissionInfractionTypeTag from '../ui/mission-infraction-type-tag.tsx'
import MissionInfractionEnvForm from './mission-infraction-env-form.tsx'

interface MissionInfractionEnvItemProps {
  index: number
  name: string
  isActionDisabled?: boolean
  byTarget: InfractionByTarget
  onDelete?: (id?: string) => void
  availableControlTypes?: ControlType[]
  actionTargetType?: ActionTargetTypeEnum
}

const MissionInfractionEnvItem: React.FC<MissionInfractionEnvItemProps> = ({
  name,
  index,
  onDelete,
  byTarget,
  actionTargetType,
  isActionDisabled,
  availableControlTypes
}) => {
  const [showForm, setShowForm] = useState(false)
  const [showDetail, setShowDetail] = useState(false)

  const handleCloseForm = () => {
    setShowForm(false)
    setShowDetail(false)
  }

  const handleShowForm = () => {
    setShowForm(true)
    setShowDetail(true)
  }

  return (
    <div key={`infraction-item-${index}`}>
      {byTarget.infractions.map((infraction, i) => (
        <Stack key={`${infraction.controlType}-${i}`} direction="column" spacing={'0.5rem'} style={{ width: '100%' }}>
          <Stack.Item
            style={{
              width: '100%',
              padding: '0.7rem',
              marginBottom: '0.5rem',
              backgroundColor: THEME.color.white
            }}
          >
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" alignItems="center" justifyContent="space-between" spacing={'0.5rem'}>
                <Stack.Item>
                  <MissionInfractionEnvTitle
                    controlType={infraction.controlType}
                    identityControlledPerson={infraction?.target?.identityControlledPerson}
                  />
                </Stack.Item>
                <Stack.Item>
                  {infraction.controlType && (
                    <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                      <Stack.Item>
                        <IconButton
                          size={Size.NORMAL}
                          role="edit-infraction"
                          accent={Accent.SECONDARY}
                          Icon={Icon.EditUnbordered}
                          disabled={isActionDisabled}
                          onClick={() => setShowForm(true)}
                        />
                      </Stack.Item>
                      <Stack.Item>
                        <IconButton
                          Icon={Icon.Delete}
                          size={Size.NORMAL}
                          role="delete-infraction"
                          accent={Accent.SECONDARY}
                          disabled={isActionDisabled}
                          onClick={() => {
                            if (onDelete) onDelete(infraction.id)
                          }}
                        />
                      </Stack.Item>
                    </Stack>
                  )}
                  {!infraction.controlType && (
                    <Stack direction="row" alignItems="baseline" spacing={'0.5rem'} justifyContent={'flex-end'}>
                      <Stack.Item>
                        <IconButton
                          size={Size.NORMAL}
                          role="display-infraction"
                          accent={Accent.SECONDARY}
                          onClick={() => setShowDetail(!showDetail)}
                          Icon={showDetail ? Icon.Hide : Icon.Display}
                        />
                      </Stack.Item>
                    </Stack>
                  )}
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              {showDetail && <MissionInfractionEnvSummaryByCacemForm infraction={infraction} />}
              {!showDetail && (
                <Stack direction="row" spacing={'0.5rem'}>
                  <Stack.Item>
                    <MissionInfractionTypeTag type={infraction.infractionType} />
                  </Stack.Item>
                  <Stack.Item>
                    <MissionNatinfTag natinfs={infraction.natinfs} />
                  </Stack.Item>
                </Stack>
              )}
            </Stack.Item>
            {actionTargetType === ActionTargetTypeEnum.VEHICLE && !showForm && (
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" alignItems="baseline" spacing={'0.5rem'} justifyContent={'flex-end'}>
                  <Stack.Item>
                    <Button
                      accent={Accent.SECONDARY}
                      size={Size.NORMAL}
                      Icon={Icon.Plus}
                      role={'add-infraction'}
                      onClick={handleShowForm}
                    >
                      infraction pour cette cible
                    </Button>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            )}
            <Stack.Item style={{ width: '100%' }}>
              {showForm && (
                <div
                  style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem', marginTop: '1rem' }}
                >
                  <Field name={`${name}.${index}`}>
                    {(field: FieldProps<InfractionByTarget>) => (
                      <MissionInfractionEnvForm
                        hideTarget={true}
                        fieldFormik={field}
                        name={`${name}.${index}`}
                        onClose={handleCloseForm}
                        actionTargetType={actionTargetType}
                        availableControlTypes={availableControlTypes}
                      />
                    )}
                  </Field>
                </div>
              )}
            </Stack.Item>
          </Stack.Item>
        </Stack>
      ))}
    </div>
  )
}

export default MissionInfractionEnvItem
