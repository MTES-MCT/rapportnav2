DO $$
BEGIN
    CREATE TYPE "RoleType" AS ENUM ('ADMIN', 'USER_PAM', 'USER_ULAM');
    ALTER TABLE "user"
    ADD COLUMN roles "RoleType"[] DEFAULT ARRAY['USER_PAM']::"RoleType"[];
END $$;
