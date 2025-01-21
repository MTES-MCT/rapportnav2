import * as Yup from 'yup';

export const simpleDateRangeValidationSchema = Yup.object().shape({
  startDateTimeUtc: Yup.date()
    .required('La date de début est requise')
    .typeError('La date de début est mal formatée'),

  endDateTimeUtc: Yup.date()
    .test(
      'both-defined',
      'Les dates de début et de fin doivent être renseignées',
      function (value) {
        const {startDateTimeUtc} = this.parent;
        return !!value || !!startDateTimeUtc
      }
    )
    .test(
      'is-defined',
      'La date de fin est requise',
      function (value) {
        return !!value
      }
    )
    .typeError('La date de fin est mal formatée')
    .test(
      'is-after-start',
      'La date de fin doit être antérieure à la date de début',
      function (value) {
        const {startDateTimeUtc} = this.parent;
        if (!startDateTimeUtc) return true
        return value && startDateTimeUtc && new Date(value) > new Date(startDateTimeUtc);
      }
    ),
});
