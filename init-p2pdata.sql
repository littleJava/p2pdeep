/*---- MySQL 判断表是否存在 SELECT table_name       FROM information_schema.tables       WHERE table_schema = 'your_database'       AND table_name = 'your_table_name';       ----*/
CREATE TABLE `invest` (
`id` INTEGER AUTO_INCREMENT,
`invest_id` VARCHAR(64) ,
`platform` VARCHAR(32) ,
`title` VARCHAR(255) ,
`type` VARCHAR(32) ,--信用标 担保标 抵押标 净值标 流转标 天标 秒标
`rate` VARCHAR(32) ,
`reward` VARCHAR(32) ,
`total` INTEGER ,
`min_invest` VARCHAR(32) ,
`max_invest` VARCHAR(32) ,
`due_time` VARCHAR(32) ,
`active_time` VARCHAR(32),
`payment_type` VARCHAR(64) ,--每月还本息/到期还
`url` VARCHAR(255) ,
`step` smallint DEFAULT 0,
`invest_desc` VARCHAR(1024) ,
PRIMARY KEY (`id`)
);
CREATE UNIQUE INDEX `invest_unique_idx` ON `invest` ( `invest_id`, `platform` );
ALTER TABLE `invest` ENGINE = InnoDB
/*----
  ---- alter table invest
  ---- alter column invest_desc varchar(1024);
 ----*/


CREATE TABLE `investor` (
`id` INTEGER AUTO_INCREMENT,
`nick_name` VARCHAR(64) ,
`per_invest` Integer ,
`invest_time` VARCHAR(32) ,
`url` VARCHAR(255) ,
`invest_id` VARCHAR(64) ,
`platform` VARCHAR(32) ,
PRIMARY KEY (`id`) ) ;

ALTER TABLE `investor` ORDER BY `id`

--DROP INDEX public.investor_unique_idx;
--CREATE UNIQUE INDEX `investor_unique_idx` ON `investor` ( `invest_id`, `platform` );

--ALTER TABLE public.investor DROP COLUMN "invest-per";
--ALTER TABLE public.investor ADD per_invest INTEGER;
