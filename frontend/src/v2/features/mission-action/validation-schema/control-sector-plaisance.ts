import { object, ObjectShape, string } from 'yup'

export const getControlSectorPlaisanceSchema = (isMissionFinished: boolean): ObjectShape => ({
  sectorEstablishmentType: isMissionFinished ? string().required() : string().nullable(),
  establishment: isMissionFinished
    ? object()
        .shape({
          name: string().required()
        })
        .required()
    : object().nullable()
})
