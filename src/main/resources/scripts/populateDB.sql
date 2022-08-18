use hw;
INSERT INTO `skills` (`id`,`language`,`level`) VALUES (1,'Java','Middle');
INSERT INTO `skills` (`id`,`language`,`level`) VALUES (2,'Java','Junior');
INSERT INTO `skills` (`id`,`language`,`level`) VALUES (3,'Java','Senior');
INSERT INTO `skills` (`id`,`language`,`level`) VALUES (4,'C++','Junior');
INSERT INTO `skills` (`id`,`language`,`level`) VALUES (5,'C++','Middle');
INSERT INTO `skills` (`id`,`language`,`level`) VALUES (6,'C++','Senior');
INSERT INTO `skills` (`id`,`language`,`level`) VALUES (7,'C#','Junior');
INSERT INTO `skills` (`id`,`language`,`level`) VALUES (8,'C#','Middle');
INSERT INTO `skills` (`id`,`language`,`level`) VALUES (9,'C#','Senior');
INSERT INTO `companies` (`id`, `name`,`city`) VALUES (1, 'PayPal','Zalupinsk');
INSERT INTO `companies` (`id`, `name`,`city`) VALUES (2, 'Amazon','Kryva Hata');
INSERT INTO `companies` (`id`, `name`,`city`) VALUES (3, 'Corning', 'Rivna Noha');
INSERT INTO `companies` (`id`, `name`,`city`) VALUES (4, 'EPAM', 'Dirka');
insert into `customers` (`id`, `name`,`city`,`industry`) values (1, 'Volodymyr Zelenskyi','Kiyv','95kvartal') , (2, 'Boris Jonsonyk','London','BBC') , (3, 'Joseph Baiden','V.DC','CNN'), (4,'Andrzej Duda','Krakow','POLSKA');
insert into `developers` (`id`,`name`,`age`,`gender`,`salary`) values (1, 'Anton', 16,'male',5550), (2, 'Mykola', 77,'male',6000),(3,'Kirill', 35,'male',5000) , (4, 'Oleg', 33,'male',4000) , (5, 'Bohdan', 30,'male',3000);
insert into `projects` (`id` , `name`,`field`, `cost`,`create_date`) values (1, 'Virus', 'Gaming',10000,'04.02.2004') , (2, 'Puzzle history', 'Machine Learning',2,'18.05.2005') , (3, 'Dogsearcher', 'Gaming',14000,'09.11.2011') , (4, 'World money', 'Bank', 2000,'15.02.2010') , (5, 'Catkiller', 'Gaming',3000,'12.02.2012');


