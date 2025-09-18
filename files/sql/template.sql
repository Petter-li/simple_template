/*
 Navicat Premium Dump SQL

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : template

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 18/09/2025 11:08:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tp_permission
-- ----------------------------
DROP TABLE IF EXISTS `tp_permission`;
CREATE TABLE `tp_permission`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `permission_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限',
  `module_id` int UNSIGNED NOT NULL COMMENT '模块ID',
  `action_id` int UNSIGNED NOT NULL COMMENT '行为ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unq_permission`(`permission_name` ASC) USING BTREE,
  UNIQUE INDEX `unq_complex`(`module_id` ASC, `action_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tp_permission
-- ----------------------------
INSERT INTO `tp_permission` VALUES (1, 'USER:INSERT', 1, 1);
INSERT INTO `tp_permission` VALUES (2, 'USER:DELETE', 1, 2);
INSERT INTO `tp_permission` VALUES (3, 'USER:UPDATE', 1, 3);
INSERT INTO `tp_permission` VALUES (4, 'USER:SELECT', 1, 4);
INSERT INTO `tp_permission` VALUES (5, 'EMPLOYEE:INSERT', 2, 1);
INSERT INTO `tp_permission` VALUES (6, 'EMPLOYEE:DELETE', 2, 2);
INSERT INTO `tp_permission` VALUES (7, 'EMPLOYEE:UPDATE', 2, 3);
INSERT INTO `tp_permission` VALUES (8, 'EMPLOYEE:SELECT', 2, 4);
INSERT INTO `tp_permission` VALUES (9, 'DEPT:INSERT', 3, 1);
INSERT INTO `tp_permission` VALUES (10, 'DEPT:DELETE', 3, 2);
INSERT INTO `tp_permission` VALUES (11, 'DEPT:UPDATE', 3, 3);
INSERT INTO `tp_permission` VALUES (12, 'DEPT:SELECT', 3, 4);
INSERT INTO `tp_permission` VALUES (13, 'MEETING:INSERT', 4, 1);
INSERT INTO `tp_permission` VALUES (14, 'MEETING:DELETE', 4, 2);
INSERT INTO `tp_permission` VALUES (15, 'MEETING:UPDATE', 4, 3);
INSERT INTO `tp_permission` VALUES (16, 'MEETING:SELECT', 4, 4);
INSERT INTO `tp_permission` VALUES (17, 'WORKFLOW:APPROVAL', 5, 5);
INSERT INTO `tp_permission` VALUES (19, 'ROOT', 0, 0);

-- ----------------------------
-- Table structure for tp_role
-- ----------------------------
DROP TABLE IF EXISTS `tp_role`;
CREATE TABLE `tp_role`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色名称',
  `permissions` json NOT NULL COMMENT '权限集合',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `role_type` enum('ADMIN','MANAGEMENT','STAFF') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'STAFF' COMMENT '角色类型（超管、管理层、普通员工）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unq_role_name`(`role_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tp_role
-- ----------------------------
INSERT INTO `tp_role` VALUES (1, '总经理', '[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]', NULL, 'STAFF', '2025-01-26 09:51:24', '2025-01-26 09:51:24', NULL);
INSERT INTO `tp_role` VALUES (2, '部门经理', '[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]', NULL, 'STAFF', '2025-01-26 09:51:24', '2025-01-26 09:51:24', NULL);
INSERT INTO `tp_role` VALUES (3, '普通员工', '[1, 2, 3, 4, 5, 6, 7, 8]', NULL, 'STAFF', '2025-01-26 09:51:24', '2025-01-26 09:51:24', NULL);
INSERT INTO `tp_role` VALUES (6, '超级管理员', '[0]', NULL, 'STAFF', '2025-01-26 09:51:24', '2025-08-13 14:09:26', NULL);

-- ----------------------------
-- Table structure for tp_user
-- ----------------------------
DROP TABLE IF EXISTS `tp_user`;
CREATE TABLE `tp_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `open_id` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '长期授权字符串',
  `photo` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '头像网址',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `sex` enum('男','女') CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '性别',
  `tel` char(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `email` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `role` json NOT NULL COMMENT '角色',
  `root` tinyint(1) NOT NULL COMMENT '是否是超级管理员',
  `dept_id` int UNSIGNED NULL DEFAULT NULL COMMENT '部门编号',
  `status` tinyint NOT NULL COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `password` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户密码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id` ASC) USING BTREE,
  UNIQUE INDEX `unq_open_id`(`open_id` ASC) USING BTREE,
  INDEX `unq_email`(`email` ASC) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tp_user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
