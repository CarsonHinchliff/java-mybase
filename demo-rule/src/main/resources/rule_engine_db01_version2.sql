/*
 Navicat Premium Data Transfer

 Source Server         : localhost-MySQL8.0
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : rule_engine_db01

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 10/04/2025 17:28:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rule_define
-- ----------------------------
DROP TABLE IF EXISTS `rule_define`;
CREATE TABLE `rule_define`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `rule_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cond` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `prepare` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `action` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `priority` int(0) NULL DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `composite_type` int(0) NULL DEFAULT NULL,
  `status` int(0) NULL DEFAULT NULL,
  `group_id` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rule_define
-- ----------------------------
INSERT INTO `rule_define` VALUES (1, 'testRule01', 'number > 1', NULL, 'System.out.println(\'Hello, great than 1!\');', 1, NULL, 0, 0, 1);
INSERT INTO `rule_define` VALUES (2, 'testRule02', 'number > 3', NULL, 'System.out.println(\'Hello, great than 3!\');', 1, NULL, 0, 0, 1);
INSERT INTO `rule_define` VALUES (3, 'testRule03', 'number > 5 ', NULL, 'studentSvc.say(number);', 1, NULL, 0, 0, 1);

-- ----------------------------
-- Table structure for rule_group
-- ----------------------------
DROP TABLE IF EXISTS `rule_group`;
CREATE TABLE `rule_group`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `result_on_exception` int(0) NULL DEFAULT NULL,
  `status` int(0) NULL DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rule_group
-- ----------------------------
INSERT INTO `rule_group` VALUES (1, 'testGroup01', 0, 0, NULL);

-- ----------------------------
-- Table structure for rule_tree
-- ----------------------------
DROP TABLE IF EXISTS `rule_tree`;
CREATE TABLE `rule_tree`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `rule_id` int(0) NULL DEFAULT NULL,
  `parent_rule_id` int(0) NULL DEFAULT NULL,
  `group_id` int(0) NULL DEFAULT NULL,
  `status` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rule_tree
-- ----------------------------
INSERT INTO `rule_tree` VALUES (1, 1, 0, 1, 0);
INSERT INTO `rule_tree` VALUES (2, 2, 0, 1, 0);
INSERT INTO `rule_tree` VALUES (3, 3, 0, 1, 0);

SET FOREIGN_KEY_CHECKS = 1;
