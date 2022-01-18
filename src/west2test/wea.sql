/*
 Navicat Premium Data Transfer

 Source Server         : weather
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : wea

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 18/01/2022 16:18:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for location
-- ----------------------------
DROP TABLE IF EXISTS `location`;
CREATE TABLE `location`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `lat` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `lon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `name`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of location
-- ----------------------------
INSERT INTO `location` VALUES ('101010100', '北京', '39.90498', '116.40528');
INSERT INTO `location` VALUES ('101020100', '上海', '31.23170', '121.47264');
INSERT INTO `location` VALUES ('101230101', '福州', '26.07530', '119.30623');

-- ----------------------------
-- Table structure for weather
-- ----------------------------
DROP TABLE IF EXISTS `weather`;
CREATE TABLE `weather`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `fxDate` date NOT NULL,
  `Max` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Min` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Day` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`, `fxDate`) USING BTREE,
  INDEX `name`(`name` ASC) USING BTREE,
  CONSTRAINT `id` FOREIGN KEY (`id`) REFERENCES `location` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `name` FOREIGN KEY (`name`) REFERENCES `location` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weather
-- ----------------------------
INSERT INTO `weather` VALUES ('101010100', '北京', '2022-01-18', '3', '-7', '晴');
INSERT INTO `weather` VALUES ('101010100', '北京', '2022-01-19', '0', '-8', '多云');
INSERT INTO `weather` VALUES ('101010100', '北京', '2022-01-20', '-1', '-7', '多云');
INSERT INTO `weather` VALUES ('101020100', '上海', '2022-01-18', '11', '5', '晴');
INSERT INTO `weather` VALUES ('101020100', '上海', '2022-01-19', '14', '5', '多云');
INSERT INTO `weather` VALUES ('101020100', '上海', '2022-01-20', '10', '4', '多云');
INSERT INTO `weather` VALUES ('101230101', '福州', '2022-01-18', '13', '10', '阴');
INSERT INTO `weather` VALUES ('101230101', '福州', '2022-01-19', '18', '11', '多云');
INSERT INTO `weather` VALUES ('101230101', '福州', '2022-01-20', '16', '11', '阴');

SET FOREIGN_KEY_CHECKS = 1;
