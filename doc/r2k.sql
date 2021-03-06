/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.6.16 : Database - r2k
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`r2k` /*!40100 DEFAULT CHARACTER SET utf8 */;


/*Table structure for table `DEVICETYPE` 设备类型表 */

DROP TABLE IF EXISTS `DEVICETYPE`;

CREATE TABLE `DEVICETYPE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '设备id',
  `NAME` varchar(200) DEFAULT NULL COMMENT '设备名称',
  `DESC` varchar(600) DEFAULT NULL COMMENT '描述',
  `CRT_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `ORG` 机构表 */

DROP TABLE IF EXISTS `ORG`;

CREATE TABLE `ORG` (
  `ORG_ID` varchar(50) DEFAULT NULL COMMENT '机构id',
  `CRT_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `ORG_NAME` varchar(200) DEFAULT NULL COMMENT '机构名称',
  `PAPER` varchar(20) DEFAULT NULL COMMENT '报纸（是否授权:0未授权，1已授权）',
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `EBOOK` varchar(20) DEFAULT NULL COMMENT '书（是否授权:0未授权，1已授权）',
  `MAKER_ID` int(11) DEFAULT NULL COMMENT '模板id',
  `DEVICE_NUM` int(11) DEFAULT NULL COMMENT '屏数量',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COMMENT='机构用户';

/*Table structure for table `ORG_AUTH_DEVICE` 设备授权表 */

DROP TABLE IF EXISTS `ORG_AUTH_DEVICE`;

CREATE TABLE `ORG_AUTH_DEVICE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ORG_ID` varchar(50) DEFAULT NULL COMMENT '机构ID',
  `DEVICE_ID` varchar(50) DEFAULT NULL COMMENT '设备类型id',
  `GROUP_NAME` varchar(50) DEFAULT NULL COMMENT '设备组名',
  `LAST_DATE` datetime DEFAULT NULL COMMENT '最后更新时间',
  `IS_ONLINE` varchar(50) DEFAULT NULL COMMENT '是否在线',
  `MAKER_ID` int(11) DEFAULT NULL COMMENT '模板id',
  `MAC` varchar(200) DEFAULT NULL COMMENT 'mac',
  `DEVICE_NAME` varchar(50) DEFAULT NULL COMMENT '设备名称',
  `DEVICETYPE_ID` int(11) DEFAULT NULL COMMENT '设备类型id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=275 DEFAULT CHARSET=utf8;

/*Table structure for table `ORG_AUTH_PAPER` 报纸授权表 */

DROP TABLE IF EXISTS `ORG_AUTH_PAPER`;

CREATE TABLE `ORG_AUTH_PAPER` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORG_ID` varchar(50) DEFAULT NULL,
  `CRT_DATE` datetime DEFAULT NULL,
  `LAST_DATE` datetime DEFAULT NULL,
  `PAPER_ID` varchar(50) DEFAULT NULL,
  `TYPE` varchar(20) DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8;

/*Table structure for table `ORG_PUB_COLUMN` 发布信息表 */

DROP TABLE IF EXISTS `ORG_PUB_COLUMN`;

CREATE TABLE `ORG_PUB_COLUMN` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '栏目ID',
  `PARENT_ID` int(11) DEFAULT NULL COMMENT '父节点',
  `TITLE` varchar(100) DEFAULT NULL COMMENT '标题',
  `SUMMARY` varchar(400) DEFAULT NULL COMMENT '摘要',
  `IMAGE` varchar(1000) DEFAULT NULL COMMENT '图片地址',
  `SORT` int(11) DEFAULT NULL COMMENT '顺序',
  `LINK` varchar(200) DEFAULT NULL COMMENT '链接地址',
  `CRT_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `STATUS` int(11) DEFAULT NULL COMMENT '状态',
  `ORG_ID` varchar(50) DEFAULT NULL COMMENT '机构ID',
  `THUMBNAIL` varchar(1000) DEFAULT NULL COMMENT '缩略图链接',
  `TYPE` varchar(200) DEFAULT NULL COMMENT '类型',
  `CONTENT` blob COMMENT '正文内容',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


/*1.5添加内容*/
DELETE FROM `r2k_test`.`PRJ_ENUM` WHERE  `ENUM_ID`=6;
DELETE FROM `r2k_test`.`PRJ_ENUM` WHERE  `ENUM_ID`=7;
DELETE FROM `r2k_test`.`PRJ_ENUM` WHERE  `ENUM_ID`=8;
DELETE FROM `r2k_test`.`PRJ_ENUM` WHERE  `ENUM_ID`=9;
DELETE FROM `r2k_test`.`PRJ_ENUM` WHERE  `ENUM_ID`=10;
UPDATE `r2k_test`.`PRJ_ENUM` SET `ENUM_ID`=5 WHERE  `ENUM_ID`=11;

ALTER TABLE `PRJ_ENUM`
	ADD COLUMN `DEV_DEF` VARCHAR(200) NULL COMMENT `默认现在那些设备上` COLLATE `utf8_bin` AFTER `ENUM_DESC`;
	
UPDATE `PRJ_ENUM` SET `DEV_DEF`=`Android-Large#Android-Pad#Android-Portrait#iPad` WHERE  `ENUM_ID`=5;
UPDATE `PRJ_ENUM` SET `DEV_DEF`='Android-Large#Android-Pad#Android-Portrait#iPad#Android-Phone#iPhone' WHERE  `ENUM_ID`=1;
UPDATE `PRJ_ENUM` SET `DEV_DEF`='Android-Large#Android-Pad#Android-Portrait#iPad#Android-Phone#iPhone' WHERE  `ENUM_ID`=2;
UPDATE `PRJ_ENUM` SET `DEV_DEF`='Android-Large#Android-Pad#Android-Portrait#iPad#Android-Phone#iPhone' WHERE  `ENUM_ID`=3;
UPDATE `PRJ_ENUM` SET `DEV_DEF`='Android-Large#Android-Pad#Android-Portrait#iPad' WHERE  `ENUM_ID`=11;
UPDATE `PRJ_ENUM` SET `ENUM_ID`=5 WHERE  `ENUM_ID`=11;
INSERT INTO PRJ_ENUM (ENUM_ID, ENUM_NAME,ENUM_VALUE,ENUM_CODE,CRT_DATE,ENUM_TYPE,ENUM_SORT,INTERFACE_URL,DEV_DEF) VALUES (6, `用户管理`,`user`,6,`2014-10-28 19:45:35`,`AUTH_RES`,6,`http://r2k/menu/6`,`iPhone#Android-Phone#iPad`);
INSERT INTO PRJ_ENUM (ENUM_ID, ENUM_NAME,ENUM_VALUE,ENUM_CODE, CRT_DATE, ENUM_TYPE,ENUM_SORT,INTERFACE_URL,DEV_DEF) VALUES (7, '导航', 'navigation' 7, '2014-10-28 19:49:53', 'AUTH_RES', 7, 'http://r2k/menu/7','Android-Large#Android-Pad#Android-Portrait#iPad');
UPDATE `PRJ_ENUM` SET `DEV_DEF`='Android-Large#Android-Pad#Android-Portrait#iPad#Android-Phone#iPhone' WHERE  `ENUM_ID`=4;
UPDATE `PRJ_ENUM` SET `DEV_DEF`='Android-Large#Android-Pad#Android-Portrait#iPad#Android-Phone#iPhone#admin' WHERE  `ENUM_ID`=1;
UPDATE `PRJ_ENUM` SET `DEV_DEF`='Android-Large#Android-Pad#Android-Portrait#iPad#Android-Phone#iPhone#admin' WHERE  `ENUM_ID`=2;
UPDATE `PRJ_ENUM` SET `DEV_DEF`='Android-Large#Android-Pad#Android-Portrait#iPad#Android-Phone#iPhone#admin' WHERE  `ENUM_ID`=3;
UPDATE `PRJ_ENUM` SET `DEV_DEF`='Android-Large#Android-Pad#Android-Portrait#iPad#Android-Phone#iPhone#admin' WHERE  `ENUM_ID`=4;
UPDATE `PRJ_ENUM` SET `DEV_DEF`='Android-Large#Android-Pad#Android-Portrait#iPad#admin' WHERE  `ENUM_ID`=5;


/*模板表建表语句*/
CREATE TABLE IF NOT EXISTS `INFO_TEMPLATE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'home/column/article/list/picturelist/videolist/acticlelist',
  `DEVICE_TYPE` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `PAGE_LIST_NUM` int(11) DEFAULT NULL,
  `NAME` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `PATH` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `SET_NO` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '按规则自动生成',
  `SET_NAME` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `SCOPE` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '0全部1个性化',
  `ORG_ID` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '只有个性化的模板才会有orgid',
  `CRT_DATE` datetime DEFAULT NULL,
  `LAST_DATE` datetime DEFAULT NULL,
  `DESCRIPTION` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `DEFAULT_TYPE` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


/*发布表建表*/
CREATE TABLE IF NOT EXISTS `RELEASE_RECORD` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORG_ID` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `DEVICE_ID` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `STATUS` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '未发布、已发布、已过期',
  `RELEASE_DATE` datetime DEFAULT NULL,
  `CRT_DATE` datetime DEFAULT NULL,
  `DEVICE_TYPE` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT 'useragent类型',
  `LAST_DATE` datetime DEFAULT NULL,
  `COLUMN_ID` int(11) DEFAULT NULL COMMENT '内容id',
  `TEMPLATE_ID` int(11) DEFAULT NULL COMMENT '模板id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*AUTH_ENTITY表加入模板管理模板*/
INSERT INTO `AUTH_ENTITY` (`ENTITY_NAME`, `CRT_DATE`, `LAST_UPDATE`, `ORDER`) VALUES ('模板管理', '2014-10-21 18:47:07', '2014-10-21 18:47:09', 8);

/*url资源表*/
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '跳转到模板新建页面', NULL, 0, '/temp/savePage.do', '2014-10-21 18:48:38', NULL, 1, 12, '2014-10-21 18:49:01', '1');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '上传整套模板zip文件', NULL, 0, '/temp/uploadZip.do\r\n', '2014-10-21 18:50:19', NULL, 1, 12, '2014-10-21 18:50:23', '1');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '保存新建模板', NULL, 0, '/temp/save.do', '2014-10-21 18:50:44', NULL, 1, 12, '2014-10-21 18:50:50', '1');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '模板列表', NULL, 0, '/temp/index.do', '2014-10-21 18:51:17', NULL, 2, 12, '2014-10-21 18:51:24', '1');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '跳转到模板修改页面', NULL, 0, '/temp/updatePage.do', '2014-10-21 18:51:46', NULL, 1, 12, '2014-10-21 18:51:49', '1');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '模板修改', NULL, 0, '/temp/update.do', '2014-10-21 18:52:12', NULL, 1, 12, '2014-10-21 18:52:16', '1');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '按模板套号查找', NULL, 0, '/temp/findTempListBySetNo.do', '2014-10-21 18:52:49', NULL, 1, 12, '2014-10-21 18:52:54', '1');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '下载整套zip模板', NULL, 0, '/temp/downloadZip.do', '2014-10-21 18:53:18', NULL, 1, 12, '2014-10-21 18:53:23', '1');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '整套模板上传替换', NULL, 0, '/temp/uploadReplaceZip.do', '2014-10-21 18:53:50', NULL, 1, 12, '2014-10-21 18:53:53', '1');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '触摸横屏资讯', NULL, 0, '/pub/showLarge.do', '2014-10-30 13:29:12', NULL, 2, 3, '2014-10-30 13:29:17', '2');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '安卓竖屏资讯', NULL, 0, '/pub/showPortrait.do', '2014-10-30 13:29:55', NULL, 2, 3, '2014-10-30 13:30:01', '3');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '安卓pad资讯', NULL, 0, '/pub/showAndroidPad.do', '2014-10-30 13:30:47', NULL, 2, 3, '2014-10-30 13:30:52', '4');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, '安卓手机资讯', NULL, 0, '/pub/showAndroidPhone.do', '2014-10-30 13:32:39', NULL, 2, 3, '2014-10-30 13:32:44', '5');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, 'iPad资讯', NULL, 0, '/pub/showIPad.do', '2014-10-30 13:33:28', NULL, 2, 3, '2014-10-30 13:33:31', '6');
INSERT INTO `AUTH_RES` (`RES_CODE`, `RES_NAME`, `RES_DESC`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `ROLE_CODE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES (NULL, 'iPhone资讯', NULL, 0, '/pub/showIPhone.do', '2014-10-30 13:34:00', NULL, 2, 3, '2014-10-30 13:34:04', '7');

/*AUTH_RES_ROLE表插入*/
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 139, '2014-10-21 19:18:34', '2014-10-21 19:18:35');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 140, '2014-10-21 19:18:43', '2014-10-21 19:18:44');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 141, '2014-10-21 19:18:52', '2014-10-21 19:18:53');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 142, '2014-10-21 19:19:01', '2014-10-21 19:19:02');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 143, '2014-10-21 19:19:10', '2014-10-21 19:19:11');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 144, '2014-10-21 19:19:18', '2014-10-21 19:19:19');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 145, '2014-10-21 19:19:36', '2014-10-21 19:19:37');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 146, '2014-10-21 19:19:44', '2014-10-21 19:19:45');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 147, '2014-10-21 19:19:53', '2014-10-21 19:19:54');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 148, '2014-10-30 13:38:16', '2014-10-30 13:38:16');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 149, '2014-10-30 13:38:25', '2014-10-30 13:38:26');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 150, '2014-10-30 13:38:43', '2014-10-30 13:38:43');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 151, '2014-10-30 13:38:53', '2014-10-30 13:38:54');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 152, '2014-10-30 13:39:02', '2014-10-30 13:39:03');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (6, 153, '2014-10-30 13:39:11', '2014-10-30 13:39:11');

drop table if exists PRJ_CONFIG;

/*==============================================================*/
/* Table: PRJ_CONFIG                                            */
/*==============================================================*/
create table PRJ_CONFIG
(
   ID                   integer NOT NULL PRIMARY KEY AUTO_INCREMENT,
   ORG_ID               varchar(50),
   DEVICE_TYPE          varchar(50),
   DEVICE_ID            varchar(50),
   CONFIG_KEY                  varchar(200) comment '配置项字段',
   CONFIG_VALUE                varchar(400) comment '配置项值',
   CRT_DATE             datetime,
   LAST_DATE            datetime,
   ENABLE               varchar(50) comment '是否启用0是1否',
   REMARK               varchar(1000) comment '配置项描述',
   CONFIG_NAME          varchar(100) comment '配置项名称'
);

/*修改菜单表*/
ALTER TABLE `ORG_MENU`
	CHANGE COLUMN `SELECTED` `ICON_BACKGROUND` VARCHAR(100) NULL DEFAULT NULL COMMENT '图标背景图片' COLLATE 'utf8_bin' AFTER `NORMAL`,
	CHANGE COLUMN `FROZEN` `LOGO` VARCHAR(100) NULL DEFAULT NULL COMMENT '置灰时图片' COLLATE 'utf8_bin' AFTER `ICON_BACKGROUND`,
	CHANGE COLUMN `TYPE` `DEVICE_TYPE` VARCHAR(50) NULL DEFAULT NULL COMMENT '设备类型' COLLATE 'utf8_bin' AFTER `STATUS`,
	ADD COLUMN `HIDE` INT(11) NULL DEFAULT NULL COMMENT '是否隐藏' AFTER `DEVICE_TYPE`,
	ADD COLUMN `HOME_PAGE` INT(11) NULL DEFAULT NULL AFTER `HIDE`;
ALTER TABLE `ORG_MENU`
	ADD COLUMN `SORT` INT NULL DEFAULT NULL COMMENT '菜单顺序' AFTER `DESCRIPTION`,
	DROP COLUMN `CALL_ID`;
ALTER TABLE `ORG_MENU`
	CHANGE COLUMN `LINK` `LINK` VARCHAR(500) NULL DEFAULT NULL COMMENT '链接地址' COLLATE 'utf8_bin' AFTER `TITLE`;
ALTER TABLE `ORG_MENU` CHANGE COLUMN `MENU_TYPE` `MENU_TYPE` VARCHAR(100) NULL DEFAULT NULL COMMENT '菜单类型' COLLATE 'utf8_bin';	

/*更新menu表和AUTH_ROLE表的DEVICE_TYPE字段*/	
ALTER TABLE AUTH_ROLE CHANGE COLUMN `DEVICE_TYPE` `DEVICE_TYPE` VARCHAR(50) NULL DEFAULT NULL;
update ORG_MENU set DEVICE_TYPE="ORG" where DEVICE_TYPE="0";
update ORG_MENU set DEVICE_TYPE="Android-Large" where DEVICE_TYPE="1";
update ORG_MENU set DEVICE_TYPE="iPad" where DEVICE_TYPE="4";
update AUTH_ROLE set DEVICE_TYPE="ORG" where DEVICE_TYPE="0";
update AUTH_ROLE set DEVICE_TYPE="Android-Large" where DEVICE_TYPE="1";
update AUTH_ROLE set DEVICE_TYPE="iPad" where DEVICE_TYPE="4";
update AUTH_ROLE set DEVICE_TYPE="iPhone" where DEVICE_TYPE="5";
update AUTH_ROLE set DEVICE_TYPE="slave" where DEVICE_TYPE="6";
update AUTH_ROLE set DEVICE_TYPE="weixin" where DEVICE_TYPE="7";

/*授权*/
UPDATE `AUTH_RES` SET `RES_NAME`='触摸横屏',`RES_URL`='/menu/showAndroidLarge.do',`VIEW_ORDER`='1' WHERE  `ID`=89;
UPDATE `AUTH_RES` SET `RES_URL`='/menu/orgIPad.do',`VIEW_ORDER`='5' WHERE  `ID`=128;
INSERT INTO `AUTH_RES` (`RES_NAME`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES ('触摸竖屏', 0, '/menu/showAndroidPortrait.do', '2014-11-06 15:17:21', 2, 10, '2014-11-06 15:17:29', '2');
INSERT INTO `AUTH_RES` (`RES_NAME`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES ('安卓pad', 0, '/menu/showAndroidPad.do', '2014-11-06 15:17:21', 2, 10, '2014-11-06 15:17:29', '3');
INSERT INTO `AUTH_RES` (`RES_NAME`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES ('安卓手机', 0, '/menu/showAndroidPhone.do', '2014-11-06 15:17:21', 2, 10, '2014-11-06 15:17:29', '4');
INSERT INTO `AUTH_RES` (`RES_NAME`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES ('iPhone', 0, '/menu/showIPhone.do', '2014-11-06 15:17:21', 2, 10, '2014-11-06 15:17:29', '6');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (1, 154, '2014-11-06 15:24:09', '2014-11-06 15:24:10');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (1, 155, '2014-11-06 15:24:09', '2014-11-06 15:24:10');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (1, 156, '2014-11-06 15:24:09', '2014-11-06 15:24:10');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (1, 157, '2014-11-06 15:24:09', '2014-11-06 15:24:10');
INSERT INTO `AUTH_ROLE` (`ROLE_CODE`, `ROLE_NAME`, `ORG_ID`, `CRT_DATE`, `LAST_UPDATE`, `ENABLED`, `DEVICE_TYPE`, `TYPE`) VALUES ('ROLE_28', '系统设置', 1, '2014-11-06 15:28:13', '2014-11-06 15:28:14', 0, 'ORG', 0);
INSERT INTO `AUTH_ENTITY` (`ENTITY_NAME`, `CRT_DATE`, `LAST_UPDATE`, `ORDER`) VALUES ('系统设置', '2014-11-06 15:29:22', '2014-11-06 15:29:23', 9);
INSERT INTO `AUTH_RES` (`RES_NAME`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES ('触摸横屏', 0, '/config/showByLarge.do', '2014-11-06 15:30:26', 2, 13, '2014-11-06 15:30:44', '1');
INSERT INTO `AUTH_RES` (`RES_NAME`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES ('触摸竖屏', 0, '/config/showByPortrait.do', '2014-11-06 15:30:26', 2, 13, '2014-11-06 15:30:44', '2');
INSERT INTO `AUTH_RES` (`RES_NAME`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES ('按设备id设置', 0, '/config/showByDevId.do', '2014-11-06 15:30:26', 1, 13, '2014-11-06 15:30:44', '1');
INSERT INTO `AUTH_RES` (`RES_NAME`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES ('按设备类型保存', 0, '/config/showByPortrait.do', '2014-11-06 15:30:26', 1, 13, '2014-11-06 15:30:44', '1');
INSERT INTO `AUTH_RES` (`RES_NAME`, `PARENT_ID`, `RES_URL`, `CRT_DATE`, `TYPE`, `ENTITY_ID`, `LAST_UPDATE`, `VIEW_ORDER`) VALUES ('按设备id保存', 0, '/config/saveByDevId.do', '2014-11-06 15:30:26', 1, 13, '2014-11-06 15:30:44', '1');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (28, 158, '2014-11-06 15:59:55', '2014-11-06 15:59:56');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (28, 159, '2014-11-06 15:59:55', '2014-11-06 15:59:56');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (28, 160, '2014-11-06 15:59:55', '2014-11-06 15:59:56');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (28, 161, '2014-11-06 15:59:55', '2014-11-06 15:59:56');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (28, 162, '2014-11-06 15:59:55', '2014-11-06 15:59:56');

UPDATE `AUTH_ROLE` SET `ROLE_NAME`='机构内容前台(心跳\菜单)' WHERE  `ID`=13;
UPDATE `AUTH_ROLE` SET `ROLE_NAME`='机构内容前台-pad(菜单)' WHERE  `ID`=14;

/*触摸竖屏权限*/
INSERT INTO `AUTH_ROLE` (`ID`, `ROLE_CODE`, `ROLE_NAME`, `ROLE_DESC`, `ROLE_CRT_USER`, `ORG_ID`, `CRT_DATE`, `LAST_UPDATE`, `ENABLED`, `DEVICE_TYPE`, `TYPE`, `MENU_TYPE`) VALUES (29, 'ROLE_29', '报纸前台-触摸竖屏', NULL, NULL, 1, '2014-11-07 15:30:15', '2014-11-07 15:30:16', 0, 'Android-Portrait', 1, '2');
INSERT INTO `AUTH_ROLE` (`ID`, `ROLE_CODE`, `ROLE_NAME`, `ROLE_DESC`, `ROLE_CRT_USER`, `ORG_ID`, `CRT_DATE`, `LAST_UPDATE`, `ENABLED`, `DEVICE_TYPE`, `TYPE`, `MENU_TYPE`) VALUES (30, 'ROLE_30', '内容前台-触摸竖屏', NULL, NULL, 1, '2014-11-07 15:32:05', '2014-11-07 15:32:06', 0, 'Android-Portrait', 1, '4');
INSERT INTO `AUTH_ROLE` (`ID`, `ROLE_CODE`, `ROLE_NAME`, `ROLE_DESC`, `ROLE_CRT_USER`, `ORG_ID`, `CRT_DATE`, `LAST_UPDATE`, `ENABLED`, `DEVICE_TYPE`, `TYPE`, `MENU_TYPE`) VALUES (31, 'ROLE_31', '专题前台-触摸竖屏', NULL, NULL, 1, '2014-11-07 15:51:00', '2014-11-01 15:51:01', 0, 'Android-Portrait', 1, '3');
INSERT INTO `AUTH_ROLE` (`ID`, `ROLE_CODE`, `ROLE_NAME`, `ROLE_DESC`, `ROLE_CRT_USER`, `ORG_ID`, `CRT_DATE`, `LAST_UPDATE`, `ENABLED`, `DEVICE_TYPE`, `TYPE`, `MENU_TYPE`) VALUES (32, 'ROLE_32', '电子书前台-触摸竖屏', NULL, NULL, 1, '2014-11-07 15:51:35', '2014-11-07 15:52:52', 0, 'Android-Portrait', 1, '1');
INSERT INTO `AUTH_ROLE` (`ID`, `ROLE_CODE`, `ROLE_NAME`, `ROLE_DESC`, `ROLE_CRT_USER`, `ORG_ID`, `CRT_DATE`, `LAST_UPDATE`, `ENABLED`, `DEVICE_TYPE`, `TYPE`, `MENU_TYPE`) VALUES (33, 'ROLE_33', '机构内容前台(心跳\\菜单)-触摸竖屏', NULL, NULL, 1, '2014-11-07 15:53:42', '2014-11-07 15:53:43', 0, 'Android-Portrait', 1, NULL);
INSERT INTO `AUTH_ROLE` (`ID`, `ROLE_CODE`, `ROLE_NAME`, `ROLE_DESC`, `ROLE_CRT_USER`, `ORG_ID`, `CRT_DATE`, `LAST_UPDATE`, `ENABLED`, `DEVICE_TYPE`, `TYPE`, `MENU_TYPE`) VALUES (34, 'ROLE_34', '图片前台-触摸竖屏', NULL, NULL, 1, '2014-11-07 15:55:23', '2014-11-07 15:55:24', 0, 'Android-Portrait', 1, '5');

insert into AUTH_RES_ROLE(ROLE_ID,RES_ID,CRT_DATE,LAST_UPDATE) (select 29,r.RES_ID,r.CRT_DATE,r.LAST_UPDATE from AUTH_RES_ROLE r where r.ROLE_ID=3);
insert into AUTH_RES_ROLE(ROLE_ID,RES_ID,CRT_DATE,LAST_UPDATE) (select 30,r.RES_ID,r.CRT_DATE,r.LAST_UPDATE from AUTH_RES_ROLE r where r.ROLE_ID=5);
insert into AUTH_RES_ROLE(ROLE_ID,RES_ID,CRT_DATE,LAST_UPDATE) (select 31,r.RES_ID,r.CRT_DATE,r.LAST_UPDATE from AUTH_RES_ROLE r where r.ROLE_ID=7);
insert into AUTH_RES_ROLE(ROLE_ID,RES_ID,CRT_DATE,LAST_UPDATE) (select 32,r.RES_ID,r.CRT_DATE,r.LAST_UPDATE from AUTH_RES_ROLE r where r.ROLE_ID=12);
insert into AUTH_RES_ROLE(ROLE_ID,RES_ID,CRT_DATE,LAST_UPDATE) (select 33,r.RES_ID,r.CRT_DATE,r.LAST_UPDATE from AUTH_RES_ROLE r where r.ROLE_ID=13);
insert into AUTH_RES_ROLE(ROLE_ID,RES_ID,CRT_DATE,LAST_UPDATE) (select 34,r.RES_ID,r.CRT_DATE,r.LAST_UPDATE from AUTH_RES_ROLE r where r.ROLE_ID=19);

/*内容表设备类型*/
ALTER TABLE `ORG_PUB_COLUMN` ADD COLUMN `DEVICE_TYPE` VARCHAR(200) NULL DEFAULT NULL COMMENT '设备类型' ;

/*菜单设备类型*/
ALTER TABLE `ORG_MENU` CHANGE COLUMN `MENU_TYPE` `MENU_TYPE` VARCHAR(50) NULL DEFAULT NULL COMMENT '设备类型';
/*apabi模板管理权限*/
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (2, 139, '2014-10-21 19:17:12', '2014-10-21 19:17:13');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (2, 140, '2014-10-21 19:17:24', '2014-10-21 19:17:25');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (2, 141, '2014-10-21 19:17:31', '2014-10-21 19:17:32');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (2, 142, '2014-10-21 19:17:42', '2014-10-21 19:17:43');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (2, 143, '2014-10-21 19:17:51', '2014-10-21 19:17:51');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (2, 144, '2014-10-21 19:17:58', '2014-10-21 19:17:59');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (2, 145, '2014-10-21 19:18:07', '2014-10-21 19:18:08');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (2, 146, '2014-10-21 19:18:14', '2014-10-21 19:18:15');
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (2, 147, '2014-10-21 19:18:24', '2014-10-21 19:18:25');

/*设备表修改*/
ALTER TABLE `ORG_AUTH_DEVICE`
	ADD COLUMN `IPV4` VARCHAR(50) NULL DEFAULT NULL AFTER `TO_VERSION`,
	ADD COLUMN `LAST_HEART_TIME` DATETIME NULL DEFAULT NULL COMMENT '最后心跳时间' AFTER `IPV4`;

/*模板表修改*/
ALTER TABLE `INFO_TEMPLATE`
DROP COLUMN `DEVICE_TYPE`,
DROP COLUMN `PAGE_LIST_NUM`,
DROP COLUMN `SET_NAME`,
DROP COLUMN `SCOPE`,
DROP COLUMN `DEFAULT_TYPE`;

CREATE TABLE `INFO_TEMPLATESET` (
   `ID` int(11) NOT NULL AUTO_INCREMENT,
   `SET_NO` varchar(50) COLLATE utf8_bin DEFAULT NULL,
   `SET_NAME` varchar(50) COLLATE utf8_bin DEFAULT NULL,
   `DEVICE_TYPE` varchar(100) COLLATE utf8_bin DEFAULT NULL,
   `DEFAULT_TYPE` varchar(50) COLLATE utf8_bin DEFAULT NULL,
   `SCOPE` int(11) DEFAULT NULL,
   `ORG_ID` varchar(50) COLLATE utf8_bin DEFAULT NULL,
   `CRT_DATE` datetime DEFAULT NULL,
   `LAST_DATE` datetime DEFAULT NULL,
   `DESCRIPTION` varchar(200) COLLATE utf8_bin DEFAULT NULL,
   PRIMARY KEY (`ID`)
 ) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
 
 /*修改消息表*/
 ALTER TABLE `SYNC_MESSAGE`
	ADD COLUMN `TYPE` VARCHAR(50) NULL DEFAULT NULL COMMENT '类型' AFTER `TIMEOUTS`,
	ADD COLUMN `MSG_BODY` MEDIUMTEXT NULL DEFAULT NULL COMMENT '消息体' AFTER `TYPE`,
	DROP COLUMN `MSG_ID`;
ALTER TABLE `SYNC_MESSAGE`
	ADD COLUMN `DEVICE_ID` VARCHAR(50) NULL DEFAULT NULL COMMENT '设备id';
	
/*iphone菜单权限*/
INSERT INTO `AUTH_ROLE` (`ID`, `ROLE_CODE`, `ROLE_NAME`, `ROLE_DESC`, `ROLE_CRT_USER`, `ORG_ID`, `CRT_DATE`, `LAST_UPDATE`, `ENABLED`, `DEVICE_TYPE`, `TYPE`, `MENU_TYPE`) VALUES (35, 'ROLE_35', '机构内容前台-iphone(菜单)', NULL, NULL, 1, '2014-11-19 14:08:33', '2014-11-19 14:08:34', 0, 'iPhone', 1, NULL);
INSERT INTO `AUTH_RES_ROLE` (`ROLE_ID`, `RES_ID`, `CRT_DATE`, `LAST_UPDATE`) VALUES (35, 129, '2014-11-19 14:10:25', '2014-11-19 14:10:26');

