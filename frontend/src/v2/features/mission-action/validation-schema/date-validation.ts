import * as Yup from 'yup';
import {Mission} from "@common/types/mission-types.ts";

export const simpleDateRangeValidationSchema = Yup.object().shape({
  startDateTimeUtc: Yup.date()
    .required('La date de début est requise')
    .typeError('La date de début est mal formatée'),
  endDateTimeUtc: Yup.date()
    .required('La date de fin est requise')
    .typeError('La date de fin est mal formatée')
    .test('is-after-start', 'La date de fin doit être antérieure à la date de début', function (value) {
      debugger
      const {startDateTimeUtc} = this.parent; // Access other field values
      return value && startDateTimeUtc && new Date(value) > new Date(startDateTimeUtc);
    }),
});

export const dateRangeValidationWithMissionDatesSchema = (mission: Mission) =>
  Yup.object().shape({
    startDateTimeUtc: Yup.date()
      .required('La date de début est requise')
      .typeError('La date de début est mal formatée')
      .test(
        'is-after-mission-start',
        'La date de début doit être comprise entre les dates de début et de fin de mission',
        function (value) {
          return mission.startDateTimeUtc && value >= mission.startDateTimeUtc;
        }
      )
      .test(
        'is-before-mission-end',
        'La date de début doit être comprise entre les dates de début et de fin de mission',
        function (value) {
          return mission.endDateTimeUtc && value <= mission.endDateTimeUtc;
        }
      ),
    endDateTimeUtc: Yup.date()
      .required('La date de fin est requise')
      .typeError('La date de fin est mal formatée')
      .test(
        'is-after-action-start',
        'La date de fin doit être antérieure à la date de début',
        function (value) {
          const {startDateTimeUtc} = this.parent;
          return value && startDateTimeUtc && value > startDateTimeUtc;
        }
      )
      .test(
        'is-before-mission-end',
        'La date de fin doit être comprise entre les dates de début et de fin de mission',
        function (value) {
          return mission.endDateTimeUtc && value <= mission.endDateTimeUtc;
        }
      )
      .test(
        'is-after-mission-start',
        'La date de fin doit être comprise entre les dates de début et de fin de mission',
        function (value) {
          return mission.startDateTimeUtc && value >= mission.startDateTimeUtc;
        }
      ),
  });

