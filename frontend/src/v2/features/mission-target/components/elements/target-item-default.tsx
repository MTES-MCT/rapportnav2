import { ControlType } from '@common/types/control-types.ts'
import { VehicleTypeEnum } from '@common/types/env-mission-types.ts'
import { Accent, Button, Icon, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { Target, TargetType } from '../../../common/types/target-types.ts'
import { useTarget } from '../../hooks/use-target.tsx'
import MissionTargetForm from './mission-target-form.tsx'
import TargetInfractionList from './target-infraction-list.tsx'

interface TargetItemProps {
  name: string
  targetType: TargetType
  vehicleType?: VehicleTypeEnum
  fieldFormik: FieldProps<Target>
  availableControlTypes?: ControlType[]
}

const TargetItemDefault: React.FC<TargetItemProps> = ({
  name,
  vehicleType,
  fieldFormik,
  targetType,
  availableControlTypes
}) => {
  const [target, setTarget] = useState<Target>()
  const { getAvailableControlTypes } = useTarget()
  const [editInfraction, setEditInfraction] = useState<boolean>(false)

  const handleClose = () => setEditInfraction(false)
  const handleAddInfraction = () => setEditInfraction(true)

  useEffect(() => {
    if (!fieldFormik.field.value) return
    setTarget(fieldFormik.field.value)
  }, [fieldFormik.field.value])

  return (
    <Stack
      direction="column"
      alignItems="flex-start"
      style={{
        width: '100%',
        backgroundColor: THEME.color.white
      }}
      data-testid="target-item"
    >
      <Stack.Item style={{ width: '100%' }}>
        {!editInfraction && (
          <Button
            Icon={Icon.Plus}
            isFullWidth={true}
            size={Size.NORMAL}
            role="show-target"
            data-testid="show-target"
            accent={Accent.SECONDARY}
            onClick={handleAddInfraction}
            title={'Infraction pour cette cible'}
          >
            {`Ajouter une infraction (hors pol. pêche)`}
          </Button>
        )}
      </Stack.Item>
      {editInfraction && (
        <Stack.Item style={{ width: '100%' }} data-testid={'mission-target-form'}>
          <Stack.Item style={{ width: '100%' }}>
            <Label>Ajout d’infraction (hors pol.pêche)</Label>
          </Stack.Item>
          <MissionTargetForm
            name={name}
            editTarget={false}
            value={{ target }}
            onClose={handleClose}
            targetType={targetType}
            fieldFormik={fieldFormik}
            vehicleType={vehicleType}
            editControl={editInfraction}
            editInfraction={editInfraction}
            availableControlTypes={getAvailableControlTypes(target, availableControlTypes)}
          />
        </Stack.Item>
      )}
      <Stack.Item style={{ width: '100%' }} data-testid={'mission-target-infraction-list'}>
        <TargetInfractionList
          name={name}
          targetType={targetType}
          fieldFormik={fieldFormik}
          availableControlTypes={getAvailableControlTypes(target, availableControlTypes)}
        />
      </Stack.Item>
    </Stack>
  )
}

export default TargetItemDefault
