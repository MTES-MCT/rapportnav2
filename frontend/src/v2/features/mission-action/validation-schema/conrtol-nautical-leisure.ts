import { number } from 'yup'

export const CONTROL_NAUTICAL_LEISURE_SCHEMA = {
  nbrOfControl: number().required(`Nombre de contrôles est requis.`),
  nbrOfControl300m: number()
    .required(`Nombre de contrôles 300m est requis.`)
    .test('control-sup-300m', `Doit être inférieur au Nombre total de contrôles.`, function (value) {
      const { nbrOfControl } = this.parent
      return nbrOfControl > value
    }),

  nbrOfControlAmp: number()
    .required(`Nombre de contrôles Amp est requis.`)
    .test('control-sup-amp', `Doit être inférieur au Nombre total de contrôles.`, function (value) {
      const { nbrOfControl } = this.parent
      return nbrOfControl > value
    })
}
