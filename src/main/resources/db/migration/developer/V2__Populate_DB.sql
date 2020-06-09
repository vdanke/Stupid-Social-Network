insert into users (password, username, enabled, id, old) values ('first', 'firstgooduser@mail.ru', true, '856dc312-abd6-4e9d-af31-fdb90eca1a51', 22);
insert into users (password, username, enabled, id, old) values ('second', 'secondgooduser@mail.ru', true, 'adb18c35-7b6b-4f2a-992e-255532c88fa4', 25);
insert into authorities (user_id, authorities) values ('856dc312-abd6-4e9d-af31-fdb90eca1a51', 'ROLE_USER');
insert into authorities (user_id, authorities) values ('adb18c35-7b6b-4f2a-992e-255532c88fa4', 'ROLE_ADMIN')