ALTER TABLE mission_action ADD COLUMN IF NOT EXISTS fish_auction_id INTEGER NULL;
ALTER TABLE mission_action ADD CONSTRAINT fk_mission_action_fish_auction
    FOREIGN KEY (fish_auction_id) REFERENCES fish_auction(id);
