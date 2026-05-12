import { array, mixed, number, string } from 'yup'
import conditionallyRequired from './conditionally-required-helper'
import getGeoCoordsSchema from './geocoords-schema'
import { LocationType } from '../types/location-type'

const getLocationSchema = (isMissionFinished?: boolean) => ({
  locationType: conditionallyRequired(
    () => mixed<LocationType>().nullable().oneOf(Object.values(LocationType)),
    [],
    true,
    'Location type is required',
    schema => schema.nonNullable()
  )(!!isMissionFinished),

  geoCoords: mixed().when('locationType', {
    is: LocationType.GPS,
    then: () => getGeoCoordsSchema(isMissionFinished).geoCoords,
    otherwise: () => array().of(number()).nullable()
  }),

  portLocode: string().when('locationType', {
    is: (val: LocationType) => val === LocationType.PORT,
    then: () => (isMissionFinished ? string().required('Location description is required') : string().nullable()),
    otherwise: () => string().nullable()
  }),

  zipCode: string().when('locationType', {
    is: (val: LocationType) => val === LocationType.COMMUNE,
    then: () => (isMissionFinished ? string().required('Location description is required') : string().nullable()),
    otherwise: () => string().nullable()
  })
})

export default getLocationSchema
