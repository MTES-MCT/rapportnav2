import { ControlType } from '@common/types/control-types.ts'
import { VehicleTypeEnum } from '@common/types/env-mission-types.ts'
import { THEME } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { MissionSourceEnum } from '../../../common/types/mission-types.ts'
import { Target, TargetType } from '../../../common/types/target-types.ts'
import { useTarget } from '../../hooks/use-target.tsx'
import MissionTargetAction from '../ui/mission-target-action.tsx'
import MissionTargetExternalData from '../ui/mission-target-external-data.tsx'
import MissionTargetTitle from '../ui/mission-target-title.tsx'
import MissionTargetForm from './mission-target-form.tsx'
import MissionTargetInfractionList from './mission-target-infraction-list.tsx'

interface MissionTargetItemProps {
  name: string
  targetType: TargetType
  vehicleType?: VehicleTypeEnum
  fieldFormik: FieldProps<Target>
  actionNumberOfControls: number
  onDelete?: (id?: string) => void
  availableControlTypes?: ControlType[]
}

const MissionTargetItem: React.FC<MissionTargetItemProps> = ({
  name,
  onDelete,
  vehicleType,
  fieldFormik,
  targetType,
  availableControlTypes
}) => {
  const [target, setTarget] = useState<Target>()
  const { computeControlTypeOnTarget } = useTarget()
  const [showDetail, setShowDetail] = useState(false)
  const [editTarget, setEditTarget] = useState<boolean>(false)
  const [editInfraction, setEditInfraction] = useState<boolean>(false)
  const [disabledAddInfraction, setDisableAddInfraction] = useState(false)

  const handleDeleteTarget = (id?: string) => {
    if (onDelete && id) onDelete(id)
  }

  const handleClose = () => {
    setShowDetail(false)
    setEditTarget(false)
    setEditInfraction(false)
  }

  const handleEditTarget = () => setEditTarget(true)
  const handleShowDetail = () => setShowDetail(!showDetail)
  const handleAddInfraction = () => setEditInfraction(true)

  useEffect(() => {
    if (!fieldFormik.field.value) return
    setTarget(fieldFormik.field.value)
  }, [fieldFormik])

  useEffect(() => {
    setDisableAddInfraction(targetType !== TargetType.VEHICLE && target?.source === MissionSourceEnum.MONITORENV)
  }, [targetType, target])

  return (
    <Stack
      direction="column"
      alignItems="flex-start"
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
            <MissionTargetTitle target={target} vehicleType={vehicleType} />
          </Stack.Item>
          <Stack.Item>
            <MissionTargetAction
              source={target?.source}
              showDetail={showDetail}
              onEdit={handleEditTarget}
              onShowDetail={handleShowDetail}
              onAddInfraction={handleAddInfraction}
              disabledAdd={disabledAddInfraction}
              onDelete={() => handleDeleteTarget(target?.id)}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      {target?.externalData && (
        <Stack.Item style={{ width: '100%' }}>
          <MissionTargetExternalData showDetail={showDetail} externalData={target.externalData} />
        </Stack.Item>
      )}
      {(editTarget || editInfraction) && (
        <Stack.Item style={{ width: '100%' }}>
          <MissionTargetForm
            name={name}
            value={{ target }}
            onClose={handleClose}
            editTarget={editTarget}
            targetType={targetType}
            fieldFormik={fieldFormik}
            vehicleType={vehicleType}
            editControl={editInfraction}
            editInfraction={editInfraction}
            availableControlTypes={computeControlTypeOnTarget(availableControlTypes, target ? [target] : [])}
          />
        </Stack.Item>
      )}
      <Stack.Item style={{ width: '100%' }}>
        <MissionTargetInfractionList
          name={name}
          targetType={targetType}
          fieldFormik={fieldFormik}
          availableControlTypes={computeControlTypeOnTarget(availableControlTypes, target ? [target] : [])}
        />
      </Stack.Item>
    </Stack>
  )
}

export default MissionTargetItem
