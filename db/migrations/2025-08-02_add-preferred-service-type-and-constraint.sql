-- Migration: Add preferred_service_type if missing and (re)create check constraint safely
-- This script is idempotent and safe to re-run.

-- 1) Add preferred_service_type only if it does not exist
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'public'
      AND table_name = 'profiles'
      AND column_name = 'preferred_service_type'
  ) THEN
    ALTER TABLE public.profiles
      ADD COLUMN preferred_service_type varchar(255) NULL;
  END IF;
END
$$;

-- 2) Read-only sanity check (optional): verify both columns exist
-- You can run this SELECT manually in Supabase SQL editor if desired.
-- SELECT column_name, data_type
-- FROM information_schema.columns
-- WHERE table_schema = 'public'
--   AND table_name = 'profiles'
--   AND column_name IN ('preferred_service_type','language_code');

-- 3) Create or replace the check constraint (drop if exists, then add)
ALTER TABLE public.profiles
  DROP CONSTRAINT IF EXISTS profiles_preferred_service_type_check;

ALTER TABLE public.profiles
  ADD CONSTRAINT profiles_preferred_service_type_check CHECK (
    preferred_service_type IS NULL
    OR preferred_service_type::text = ANY (ARRAY[
      'MENSTRUATION'::varchar,
      'BREAST_HEALTH'::varchar,
      'MENTAL_HEALTH'::varchar,
      'PCOS'::varchar
    ]::text[])
  );

-- 4) Optional: ensure column type/nullable (only run if needed to align types)
-- Uncomment if you discover type mismatch or NOT NULL you want to drop:
-- ALTER TABLE public.profiles
--   ALTER COLUMN preferred_service_type TYPE varchar(255),
--   ALTER COLUMN preferred_service_type DROP NOT NULL;

-- 5) Optional: inspect existing check constraints on the table (for debugging)
-- SELECT con.conname, pg_get_constraintdef(con.oid) AS definition
-- FROM pg_constraint con
-- JOIN pg_class rel ON rel.oid = con.conrelid
-- JOIN pg_namespace nsp ON nsp.oid = rel.relnamespace
-- WHERE nsp.nspname = 'public'
--   AND rel.relname = 'profiles'
--   AND con.contype = 'c';