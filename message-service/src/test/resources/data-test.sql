INSERT INTO `TEMPLATE` (`id`, `address`, `body`) VALUES (10,
'example@mailgo.com',
'Invoice invoice payment date');

INSERT INTO `TEMPLATE` (`id`, `address`, `body`) VALUES (20,
'example@mailgo.org',
'Payment invoice payment date');

INSERT INTO `KEY_PATTERN` (`id`, `keyword`, `type`,`pattern`) VALUES (10,'example@example.com', 'invoice','[0-9]{1,10}\/[0-9]{1,4}\/[0-9]{4}\/F');
INSERT INTO `KEY_PATTERN` (`id`, `keyword`, `type`,`pattern`) VALUES (20,'example@example.com', 'payment','[0-9]{0,3}\,[0-9]{2}');
INSERT INTO `KEY_PATTERN` (`id`, `keyword`, `type`,`pattern`) VALUES (30,'example@example.com', 'date','[0-9]{2}-[0-9]{2}-[0-9]{4}');
