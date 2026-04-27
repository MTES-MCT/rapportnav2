ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS fish_auction_id INTEGER NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_mission_action_fish_auction'
    ) THEN
        ALTER TABLE mission_action ADD CONSTRAINT fk_mission_action_fish_auction
            FOREIGN KEY (fish_auction_id) REFERENCES fish_auction(id);
    END IF;
END $$;