import { ControlType } from '@common/types/control-types.ts'
import { VehicleTypeEnum } from '@common/types/env-mission-types.ts'
import { THEME } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { Target, TargetType } from '../../../common/types/target-types.ts'
import MissionTargetEnvAction from '../ui/mission-target-env-action.tsx'
import MissionTargetEnvExternalData from '../ui/mission-target-env-external-data.tsx'
import MissionTargetEnvTitle from '../ui/mission-target-env-title.tsx'
import MissionTargetEnvForm from './mission-target-env-form.tsx'
import MissionTargetEnvInfractionList from './mission-target-env-infraction-list.tsx'

interface MissionTargetEnvItemProps {
  name: string
  targetType: TargetType
  vehicleType?: VehicleTypeEnum
  fieldFormik: FieldProps<Target>
  actionNumberOfControls: number
  onDelete?: (id?: string) => void
  availableControlTypes?: ControlType[]
}

const MissionTargetEnvItem: React.FC<MissionTargetEnvItemProps> = ({
  name,
  onDelete,
  vehicleType,
  fieldFormik,
  targetType,
  availableControlTypes
}) => {
  const [target, setTarget] = useState<Target>()
  const [showDetail, setShowDetail] = useState(false)
  const [enableForm, setEnableFrom] = useState(false)
  const [controlType, setControlType] = useState<ControlType>()

  const handleDelete = (id?: string) => {
    if (onDelete && id) onDelete(id)
  }

  useEffect(() => {
    if (!fieldFormik.field.value) return
    setTarget(fieldFormik.field.value)
    const controlWithInfraction = fieldFormik.field.value.controls?.find(c => c.infractions?.some(Boolean))
    const newControlType = controlWithInfraction?.controlType
    if (newControlType !== controlType) setControlType(newControlType)
  }, [fieldFormik, controlType])

  useEffect(() => {
    setEnableFrom(targetType === TargetType.VEHICLE)
  }, [targetType])

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
            <MissionTargetEnvTitle target={target} vehicleType={vehicleType} />
          </Stack.Item>
          <Stack.Item>
            <MissionTargetEnvAction
              source={target?.source}
              showDetail={showDetail}
              enableForm={enableForm}
              setShowDetail={setShowDetail}
              onDelete={() => handleDelete(target?.id)}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      {target?.externalData && (
        <Stack.Item style={{ width: '100%' }}>
          <MissionTargetEnvExternalData showDetail={showDetail} externalData={target.externalData} />
        </Stack.Item>
      )}
      {showDetail && enableForm && (
        <Stack.Item style={{ width: '100%' }}>
          <MissionTargetEnvForm
            name={name}
            targetType={targetType}
            fieldFormik={fieldFormik}
            vehicleType={vehicleType}
            onClose={() => setShowDetail(false)}
            availableControlTypes={availableControlTypes}
          />
        </Stack.Item>
      )}
      <Stack.Item style={{ width: '100%' }}>
        <MissionTargetEnvInfractionList
          name={name}
          targetType={targetType}
          fieldFormik={fieldFormik}
          availableControlTypes={availableControlTypes}
        />
      </Stack.Item>
    </Stack>
  )
}

export default MissionTargetEnvItem
