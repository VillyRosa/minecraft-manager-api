CREATE TABLE locations (
    id UUID PRIMARY KEY,
    world_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    dimension VARCHAR(100) NOT NULL,
    x DOUBLE PRECISION NOT NULL,
    y DOUBLE PRECISION NOT NULL,
    z DOUBLE PRECISION NOT NULL,
    yaw FLOAT,
    pitch FLOAT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_locations_world
        FOREIGN KEY (world_id)
        REFERENCES worlds (id)
        ON DELETE CASCADE
);
