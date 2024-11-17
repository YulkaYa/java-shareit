insert into users (id, name, email)
values (1, 'testLogin','yulkaTEST@ya.ru') ON CONFLICT(id) DO NOTHING;