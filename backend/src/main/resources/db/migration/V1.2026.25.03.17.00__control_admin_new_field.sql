DO
$$
  BEGIN
    ALTER TABLE control_2 ADD COLUMN compliant_safe_manning_permit CHAR VARYING(16) NULL;
  END
$$;
