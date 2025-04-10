-- pass for users password

INSERT INTO users (id, username, password, email, phone_number, enabled, role) VALUES
  ('550e8400-e29b-41d4-a716-446655440001', 'johndoe', '$2a$12$S4Zg8J82n6X2eLxl4WeU6.5xfEYrTTijqlstEv4fF4kF1LX3Eclai', 'johndoe@example.com', '1234567890', TRUE, 'ROLE_USER'),
  ('550e8400-e29b-41d4-a716-446655440002', 'janedoe', '$2a$12$S4Zg8J82n6X2eLxl4WeU6.5xfEYrTTijqlstEv4fF4kF1LX3Eclai', 'janedoe@example.com', '0987654321', TRUE,  'ROLE_USER'),
  ('550e8400-e29b-41d4-a716-446655440003', 'alice', '$2a$12$S4Zg8J82n6X2eLxl4WeU6.5xfEYrTTijqlstEv4fF4kF1LX3Eclai', 'alice@example.com', '5556667777', TRUE, 'ROLE_USER'),
  ('550e8400-e29b-41d4-a716-446655440004', 'bobsmith', '$2a$12$S4Zg8J82n6X2eLxl4WeU6.5xfEYrTTijqlstEv4fF4kF1LX3Eclai', 'bobsmith@example.com', '4445556666', TRUE, 'ROLE_ADMIN'),
  ('550e8400-e29b-41d4-a716-446655440005', 'charlie', '$2a$12$S4Zg8J82n6X2eLxl4WeU6.5xfEYrTTijqlstEv4fF4kF1LX3Eclai', 'charlie@example.com', '7778889999', TRUE, 'ROLE_USER');

INSERT INTO addresses (id, street, city, state, zip_code, country, user_id) VALUES
    ('660e8400-e29b-41d4-a716-556655440001', '123 Main St', 'New York', 'NY', '10001', 'USA', '550e8400-e29b-41d4-a716-446655440001'),
    ('660e8400-e29b-41d4-a716-556655440002', '456 Oak St', 'Los Angeles', 'CA', '90001', 'USA', '550e8400-e29b-41d4-a716-446655440001'),
    ('660e8400-e29b-41d4-a716-556655440003', '789 Pine St', 'Chicago', 'IL', '60601', 'USA', '550e8400-e29b-41d4-a716-446655440002'),
    ('660e8400-e29b-41d4-a716-556655440004', '101 Maple Ave', 'Houston', 'TX', '77001', 'USA', '550e8400-e29b-41d4-a716-446655440003'),
    ('660e8400-e29b-41d4-a716-556655440005', '202 Birch Rd', 'Phoenix', 'AZ', '85001', 'USA', '550e8400-e29b-41d4-a716-446655440004'),
    ('660e8400-e29b-41d4-a716-556655440006', '303 Cedar Blvd', 'San Francisco', 'CA', '94101', 'USA', '550e8400-e29b-41d4-a716-446655440005'),
    ('660e8400-e29b-41d4-a716-556655440007', '404 Walnut Ln', 'Seattle', 'WA', '98101', 'USA', '550e8400-e29b-41d4-a716-446655440005');
