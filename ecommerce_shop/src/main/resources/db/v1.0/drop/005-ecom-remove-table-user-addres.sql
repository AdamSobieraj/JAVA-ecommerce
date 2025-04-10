
DELETE FROM addresses WHERE user_id IN (
    SELECT id FROM users WHERE username IN ('johndoe', 'janedoe', 'alice', 'bobsmith', 'charlie')
);


DELETE FROM users WHERE username IN ('johndoe', 'janedoe', 'alice', 'bobsmith', 'charlie');
