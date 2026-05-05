import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum, VehicleTypeEnum } from '@common/types/env-mission-types'
import { Field, FieldArrayRenderProps, FieldProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { Target } from '../../../common/types/target-types'
import { useTarget } from '../../hooks/use-target'
import TargetItemOthers from './target-item-others'

export interface TargetListProps {
  name: string
  vehicleType?: VehicleTypeEnum
  actionNumberOfControls: number
  fieldArray: FieldArrayRenderProps
  controlsToComplete: ControlType[]
  actionTargetType?: ActionTargetTypeEnum
  availableControlTypes?: ControlType[]
}

const TargetList: FC<TargetListProps> = ({
  name,
  fieldArray,
  vehicleType,
  actionTargetType,
  availableControlTypes,
  actionNumberOfControls
}) => {
  const { isDefaultTarget, getTargetType } = useTarget()
  const handleRemove = (index: number) => fieldArray.remove(index)

  return (
    <Stack direction="column" alignItems="flex-start" style={{ width: '100%' }}>
      {(fieldArray.form.values.targets || []).map((target: Target, targetIndex: number) => (
        <Stack.Item style={{ width: '100%' }} key={`${name}-default-${targetIndex}`}>
          {!isDefaultTarget(target) && (
            <Field name={`targets[${targetIndex}]`}>
              {(fieldFormik: FieldProps<Target>) => (
                <TargetItemOthers
                  index={targetIndex}
                  fieldFormik={fieldFormik}
                  vehicleType={vehicleType}
                  name={`targets[${targetIndex}]`}
                  targetType={getTargetType(actionTargetType)}
                  onDelete={() => handleRemove(targetIndex)}
                  availableControlTypes={availableControlTypes}
                  actionNumberOfControls={actionNumberOfControls}
                />
              )}
            </Field>
          )}
        </Stack.Item>
      ))}
    </Stack>
  )
}

export default TargetList
