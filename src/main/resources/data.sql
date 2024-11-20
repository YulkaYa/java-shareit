insert into users (id, name, email)
values (0, 'testLogin','yulkaTESdddT@ya.ru') ON CONFLICT(id) DO NOTHING;