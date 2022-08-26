INSERT INTO `TEMPLATE` (`id`, `address`, `body`) VALUES (1,
'exampleg@example.com',
'AAA
Invoice: invoice
Cash: payment PLN
Payment date: date');

INSERT INTO `TEMPLATE` (`id`, `address`, `body`) VALUES (2,
'example@example.org',
'BBB
Invoice: invoice
Cash: payment PLN
Payment date: date
Account number: account');
example@example.com
INSERT INTO `KEY_PATTERN` (`id`, `keyword`, `type`,`pattern`) VALUES (1,'example@example.com', 'invoice','[0-9]{1,10}\/[0-9]{1,4}\/[0-9]{4}\/F');
INSERT INTO `KEY_PATTERN` (`id`, `keyword`, `type`,`pattern`) VALUES (2,'example@example.com', 'payment','[0-9]{0,3}\,[0-9]{2}');
INSERT INTO `KEY_PATTERN` (`id`, `keyword`, `type`,`pattern`) VALUES (3,'example@example.com', 'date','[0-9]{2}-[0-9]{2}-[0-9]{4}');
