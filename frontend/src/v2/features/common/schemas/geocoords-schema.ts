import { array, number } from 'yup'

const getGeoCoordsSchema = (isMissionFinished?: boolean) => {
  return isMissionFinished
    ? {
        geoCoords: array()
          .of(number().required('Latitude/Longitude must be a number'))
          .length(2, 'geoCoords must have exactly two elements: [lat, lon]')
          .required('geoCoords is required')
      }
    : {
        geoCoords: array().of(number()).nullable()
      }
}

export default getGeoCoordsSchema
