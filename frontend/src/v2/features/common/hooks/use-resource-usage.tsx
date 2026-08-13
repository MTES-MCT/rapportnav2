import { useMemo } from 'react'
import * as Yup from 'yup'
import { getResourceUsageKind } from '../types/control-unit-types.ts'

/**
 * Resource usage (km / engine hours) concern for the ULAM general info.
 *
 * `schema` is a `{ resources }` Yup fragment: on a finished mission an eligible resource must carry its
 * value; otherwise it's optional. When "aucun moyen utilisé" is checked, resources is emptied upstream, so
 * there is nothing to validate. Spread it into a form's shape, or wrap it for a sub-form's inner Formik —
 * both share the one rule so the km / engine-hours inputs get a real error and show the red required border.
 */
export const useResourceUsage = (isMissionFinished: boolean) => {
  const validationSchema = useMemo(
    () => ({
      resources: Yup.array().of(
        Yup.object({
          nbKms: Yup.number()
            .min(0, 'La distance parcourue doit être positive')
            .nullable()
            .test('km-required', 'Km parcourus obligatoire', function (value) {
              if (!isMissionFinished) return true
              if (getResourceUsageKind(this.parent?.type) !== 'KM') return true
              return value !== undefined && value !== null
            }),
          nbEngineHours: Yup.number()
            .min(0, "Le nombre d'heures moteur doit être positif")
            .nullable()
            .test('engine-hours-required', "Nb d'heures moteur obligatoire", function (value) {
              if (!isMissionFinished) return true
              if (getResourceUsageKind(this.parent?.type) !== 'ENGINE_HOURS') return true
              return value !== undefined && value !== null
            })
        })
      )
    }),
    [isMissionFinished]
  )

  return { validationSchema }
}

export default useResourceUsage
