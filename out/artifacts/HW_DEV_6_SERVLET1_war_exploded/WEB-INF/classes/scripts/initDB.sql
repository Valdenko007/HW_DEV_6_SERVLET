drop database if exists hw;
create database if not exists hw;

use hw;


create table `companies` (
                             `id` int not null auto_increment,
                             `name` varchar(255) not null,
                             `city` varchar(255) not null,
                             primary key (`id`)
);


create table `customers` (
                             `id` int not null auto_increment,
                             `name` varchar(255) not null,
                             `city` varchar(255) not null,
                             `industry` varchar(255) not null,

                             primary key (`id`)
);


create table `developers`(
                             `id` int not null auto_increment,
                             `name` varchar(45) not null,
                             `age` int not null,
                             `gender` varchar(255),
                             `salary` long not null,
                             primary key (`id`)
);


create table `projects`(
                           `id` int not null auto_increment,
                           `name`  varchar(255) not null,
                           `field`  varchar(255) not null,
                           `cost` int not null ,
                           `create_date` date not null,
                           primary key (`id`)
);


create table `skills` (
                          `id` int not null auto_increment,
                          `language` varchar(45) not null,
                          `level` enum('Junior', 'Middle', 'Senior') not null,
                          primary key (`id`)
);



