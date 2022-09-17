INSERT INTO `TEMPLATE` (`id`, `address`, `body`) VALUES (10,
'example@mailgo.com',
'Invoice invoice payment date');

INSERT INTO `TEMPLATE` (`id`, `address`, `body`) VALUES (20,
'example@mailgo.org',
'Payment invoice payment date');

INSERT INTO `KEY_PATTERN` (`id`, `keyword`, `type`,`pattern`) VALUES (10,'example@example.com', 'invoice','invoice');
INSERT INTO `KEY_PATTERN` (`id`, `keyword`, `type`,`pattern`) VALUES (20,'example@example.com', 'payment','payment');
INSERT INTO `KEY_PATTERN` (`id`, `keyword`, `type`,`pattern`) VALUES (30,'example@example.com', 'date','date');

INSERT INTO `MESSAGE` (`id`, `body`, `email_uuid`, `send_date`, `status`)
VALUES (10, 'Message', 'e976f0d4-22d1-48be-a724-ef6c3879f428', '2021-10-16','NOT_SENT');
INSERT INTO `MESSAGE` (`id`, `body`, `email_uuid`, `send_date`, `status`)
VALUES (20, 'Message', 'e976f0d4-22d1-48be-a724-ef6c3879f429', '2021-10-16','NOT_SENT');
INSERT INTO `MESSAGE` (`id`, `body`, `email_uuid`, `send_date`, `status`)
VALUES (30, 'Message', 'e976f0d4-22d1-48be-a724-ef6c3879f430', '2021-10-16','SENT');
INSERT INTO `MESSAGE` (`id`, `body`, `email_uuid`, `send_date`, `status`)
VALUES (40, 'Message', 'e976f0d4-22d1-48be-a724-ef6c3879f431', '2021-10-16','SENT');
