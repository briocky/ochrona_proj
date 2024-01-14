INSERT INTO users (id, birth_date, email, email_confirmed, first_name, last_name, password)
VALUES (nextval('users_seq'), '2000-01-01', 'jan@wp.pl', true, 'Jan', 'Kowalski',
        'HZbmORJlA0kwdoEQ3mAi7oNWdhyJjRZZM6ldYdkVFh2BRtPbmlf5YA==');

INSERT INTO users (id, birth_date, email, email_confirmed, first_name, last_name, password)
VALUES (nextval('users_seq'), '2000-01-01', 'marek@wp.pl', true, 'Marek', 'Kowalski',
        'HZbmORJlA0kwdoEQ3mAi7oNWdhyJjRZZM6ldYdkVFh2BRtPbmlf5YA==');
