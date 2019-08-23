/*
Navicat MySQL Data Transfer

Source Server         : localmysql
Source Server Version : 80015
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2019-08-23 18:03:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `perm`
-- ----------------------------
DROP TABLE IF EXISTS `perm`;
CREATE TABLE `perm` (
  `perm_id` bigint(20) NOT NULL,
  `perm_name` varchar(30) NOT NULL,
  `perm_desc` varchar(100) DEFAULT NULL,
  `available` char(1) DEFAULT '1',
  PRIMARY KEY (`perm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of perm
-- ----------------------------
INSERT INTO `perm` VALUES ('11', 'user:create', 'user:create', '1');
INSERT INTO `perm` VALUES ('12', 'user:update', 'user:update', '1');
INSERT INTO `perm` VALUES ('13', 'user:delete', 'user:delete', '1');
INSERT INTO `perm` VALUES ('14', 'user:view', 'user:view', '1');

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role_id` bigint(20) NOT NULL,
  `role_name` varchar(30) NOT NULL,
  `role_desc` varchar(100) DEFAULT NULL,
  `available` char(1) DEFAULT '1',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'role1', 'desc1', '1');
INSERT INTO `role` VALUES ('2', 'role2', 'desc2', '1');

-- ----------------------------
-- Table structure for `role_perm`
-- ----------------------------
DROP TABLE IF EXISTS `role_perm`;
CREATE TABLE `role_perm` (
  `role_id` bigint(20) NOT NULL,
  `perm_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`perm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_perm
-- ----------------------------
INSERT INTO `role_perm` VALUES ('1', '11');
INSERT INTO `role_perm` VALUES ('1', '12');
INSERT INTO `role_perm` VALUES ('1', '13');
INSERT INTO `role_perm` VALUES ('1', '14');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL,
  `user_name` varchar(30) NOT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `salt` varchar(100) NOT NULL DEFAULT '123456',
  `locked` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '1:閿佸畾',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `unique_name` (`user_name`),
  UNIQUE KEY `unique_email` (`email`),
  UNIQUE KEY `unique_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1164460514286374912', 'zhong1', '18260631671', null, '1f7c7faa0be82af57f3ba308843072ca6e57f66105a0b06017528044ff3a7ce9', '4694e56b4d8db9896eb50b9f8b42d4a1', '0', '2019-08-22 16:53:17', '2019-08-22 16:53:17');
INSERT INTO `sys_user` VALUES ('1164792856871833600', 'zhong2', '18260631672', null, '4a493c78c97d5d202fe0ec05f2730814bb682fcf68324c05f050531d37bc0c73', '6febca7278865cd186730ee1fda3a6e3', '0', '2019-08-23 14:53:54', '2019-08-23 14:53:54');

-- ----------------------------
-- Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1164460514286374912', '1');
INSERT INTO `user_role` VALUES ('1164460514286374912', '2');
INSERT INTO `user_role` VALUES ('1164792856871833600', '2');
