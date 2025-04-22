-- === mission_action_control ===
ALTER TABLE mission_action_control RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_action_control ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_action_control SET mission_id = mission_id_int::VARCHAR;

-- === mission_action_anti_pollution ===
ALTER TABLE mission_action_anti_pollution RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_action_anti_pollution ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_action_anti_pollution SET mission_id = mission_id_int::VARCHAR;

-- === mission_action_baaem_permanence ===
ALTER TABLE mission_action_baaem_permanence RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_action_baaem_permanence ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_action_baaem_permanence SET mission_id = mission_id_int::VARCHAR;

-- === mission_action_free_note ===
ALTER TABLE mission_action_free_note RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_action_free_note ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_action_free_note SET mission_id = mission_id_int::VARCHAR;

-- === mission_action_illegal_immigration ===
ALTER TABLE mission_action_illegal_immigration RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_action_illegal_immigration ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_action_illegal_immigration SET mission_id = mission_id_int::VARCHAR;

-- === mission_action_nautical_event ===
ALTER TABLE mission_action_nautical_event RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_action_nautical_event ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_action_nautical_event SET mission_id = mission_id_int::VARCHAR;

-- === mission_action_public_order ===
ALTER TABLE mission_action_public_order RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_action_public_order ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_action_public_order SET mission_id = mission_id_int::VARCHAR;

-- === mission_action_representation ===
ALTER TABLE mission_action_representation RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_action_representation ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_action_representation SET mission_id = mission_id_int::VARCHAR;

-- === mission_action_status ===
ALTER TABLE mission_action_status RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_action_status ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_action_status SET mission_id = mission_id_int::VARCHAR;

-- === mission_action_vigimer ===
ALTER TABLE mission_action_vigimer RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_action_vigimer ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_action_vigimer SET mission_id = mission_id_int::VARCHAR;

-- === mission_action ===
ALTER TABLE mission_action RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_action ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_action SET mission_id = mission_id_int::VARCHAR;

-- === mission_crew ===
ALTER TABLE mission_crew RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_crew ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_crew SET mission_id = mission_id_int::VARCHAR;

-- === mission_general_info ===
ALTER TABLE mission_general_info RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_general_info ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_general_info SET mission_id = mission_id_int::VARCHAR;

-- === infraction_env_target ===
ALTER TABLE infraction_env_target RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE infraction_env_target ADD COLUMN mission_id VARCHAR(255);
UPDATE infraction_env_target SET mission_id = mission_id_int::VARCHAR;

-- === infraction ===
ALTER TABLE infraction RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE infraction ADD COLUMN mission_id VARCHAR(255);
UPDATE infraction SET mission_id = mission_id_int::VARCHAR;

-- === control_administrative ===
ALTER TABLE control_administrative RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE control_administrative ADD COLUMN mission_id VARCHAR(255);
UPDATE control_administrative SET mission_id = mission_id_int::VARCHAR;

-- === control_security ===
ALTER TABLE control_security RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE control_security ADD COLUMN mission_id VARCHAR(255);
UPDATE control_security SET mission_id = mission_id_int::VARCHAR;

-- === control_navigation ===
ALTER TABLE control_navigation RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE control_navigation ADD COLUMN mission_id VARCHAR(255);
UPDATE control_navigation SET mission_id = mission_id_int::VARCHAR;

-- === control_gens_de_mer ===
ALTER TABLE control_gens_de_mer RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE control_gens_de_mer ADD COLUMN mission_id VARCHAR(255);
UPDATE control_gens_de_mer SET mission_id = mission_id_int::VARCHAR;

-- === mission_action_rescue ===
ALTER TABLE mission_action_rescue RENAME COLUMN mission_id TO mission_id_int;
ALTER TABLE mission_action_rescue ADD COLUMN mission_id VARCHAR(255);
UPDATE mission_action_rescue SET mission_id = mission_id_int::VARCHAR;
