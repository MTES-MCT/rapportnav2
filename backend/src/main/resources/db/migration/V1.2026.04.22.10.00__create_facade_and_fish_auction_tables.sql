-- Create fish_auction table
CREATE TABLE IF NOT EXISTS fish_auction (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(128) NOT NULL,
    facade     VARCHAR(8)   NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    created_by INT,
    updated_by INT,
    deleted_at TIMESTAMP WITH TIME ZONE
);

-- Seed fish auctions
-- Façade MEMN
INSERT INTO fish_auction (name, facade) VALUES
    ('Boulogne-sur-Mer', 'MEMN'),
    ('Dunkerque', 'MEMN'),
    ('Dieppe', 'MEMN'),
    ('Fécamp', 'MEMN'),
    ('Port en Bessin', 'MEMN'),
    ('Grandcamp', 'MEMN'),
    ('Cherbourg', 'MEMN'),
    ('Granville', 'MEMN');

-- Façade NAMO
INSERT INTO fish_auction (name, facade) VALUES
    ('Saint-Malo', 'NAMO'),
    ('Cancale', 'NAMO'),
    ('Erquy', 'NAMO'),
    ('Saint-Quay-Portrieux', 'NAMO'),
    ('Roscoff', 'NAMO'),
    ('Brest', 'NAMO'),
    ('Douarnenez', 'NAMO'),
    ('Audierne', 'NAMO'),
    ('Le Guilvinec', 'NAMO'),
    ('Saint-Guénolé', 'NAMO'),
    ('Loctudy', 'NAMO'),
    ('Concarneau', 'NAMO'),
    ('Lorient', 'NAMO'),
    ('Quiberon', 'NAMO'),
    ('La Turballe', 'NAMO'),
    ('Le Croisic', 'NAMO');

-- Façade SA
INSERT INTO fish_auction (name, facade) VALUES
    ('Noirmoutier', 'SA'),
    ('Île d''Yeu', 'SA'),
    ('Les Sables d''Olonne', 'SA'),
    ('Saint-Gilles-Croix-de-Vie', 'SA'),
    ('La Rochelle', 'SA'),
    ('Oléron - La Cotinière', 'SA'),
    ('Royan', 'SA'),
    ('Arcachon', 'SA'),
    ('Saint-Jean-de-Luz / Ciboure', 'SA');

-- Façade MED
INSERT INTO fish_auction (name, facade) VALUES
    ('Port-la-Nouvelle', 'MED'),
    ('Sète', 'MED'),
    ('Agde', 'MED'),
    ('Le Grau-du-Roi', 'MED');