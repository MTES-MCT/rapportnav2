import { store } from '..'

export const setFormValidation = (isValid: boolean) => {
  store.setState(state => {
    return {
      ...state,
      formValidation: {
        isValid
      }
    }
  })
}

export const resetFormValidation = () => {
  store.setState(state => {
    return {
      ...state,
      formValidation: {}
    }
  })
}
