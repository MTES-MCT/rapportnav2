import { InputPicker as RSuiteInputPicker, InputPickerProps as RSuiteInputPickerProps } from 'rsuite'

type InputPickerProps = RSuiteInputPickerProps

const InputPicker: React.FC<InputPickerProps> = props => {
  return <RSuiteInputPicker {...props} />
}

export default InputPicker
