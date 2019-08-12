/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost
 Source Database       : douyin

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : utf-8

 Date: 08/12/2019 11:01:16 AM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `bgm`
-- ----------------------------
DROP TABLE IF EXISTS `bgm`;
CREATE TABLE `bgm` (
  `id` varchar(64) NOT NULL,
  `author` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL COMMENT '播放地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Records of `bgm`
-- ----------------------------
BEGIN;
INSERT INTO `bgm` VALUES ('2051d809e9e06a8da1d23ea9fde84605', '佚名', '慢慢喜欢你', '/bgm/慢慢喜欢你.m4a'), ('efa43a303db21d6727fdd61fda539f13', '李雨涵D', '李雨涵D创作的原声', '/bgm/李雨涵D创作的原声.m4a');
COMMIT;

-- ----------------------------
--  Table structure for `comments`
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `id` varchar(64) NOT NULL,
  `father_comment_id` varchar(64) DEFAULT NULL,
  `to_user_id` varchar(64) DEFAULT NULL,
  `video_id` varchar(64) NOT NULL COMMENT '视频id',
  `from_user_id` varchar(64) NOT NULL COMMENT '留言者，评论的用户id',
  `comment` text NOT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程评论表';

-- ----------------------------
--  Records of `comments`
-- ----------------------------
BEGIN;
INSERT INTO `comments` VALUES ('79f36db6611511660bbc949c2b0b0011', 'bf7f4654dcda5449628535b4d78ea4fd', 'f7d02cddff7d58a4ca19b20382b7f06d', '300a4b0fa74e10f7468c0ae07550e255', 'f7d02cddff7d58a4ca19b20382b7f06d', '你好', '2019-05-28 23:43:54'), ('bc449d5eb557993a6db90d370c5b7137', '79f36db6611511660bbc949c2b0b0011', 'f7d02cddff7d58a4ca19b20382b7f06d', '300a4b0fa74e10f7468c0ae07550e255', 'd824b26d397f904de0096f738c5e8319', '你好呀', '2019-05-30 02:58:13'), ('bf7f4654dcda5449628535b4d78ea4fd', 'undefined', 'undefined', '300a4b0fa74e10f7468c0ae07550e255', 'f7d02cddff7d58a4ca19b20382b7f06d', '哈哈哈', '2019-05-28 23:43:45'), ('e241252e3088cefb338b57d5ca46e03d', 'undefined', 'undefined', '060bfa91cc8cbd89d2fe6bb9084ba296', 'd824b26d397f904de0096f738c5e8319', 'nihao', '2019-06-04 15:58:13');
COMMIT;

-- ----------------------------
--  Table structure for `search_records`
-- ----------------------------
DROP TABLE IF EXISTS `search_records`;
CREATE TABLE `search_records` (
  `id` varchar(64) NOT NULL,
  `content` varchar(255) NOT NULL COMMENT '搜索的内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频搜索的记录表';

-- ----------------------------
--  Records of `search_records`
-- ----------------------------
BEGIN;
INSERT INTO `search_records` VALUES ('03d0cbd0a73ab9ecadf1ef9a9b1b2046', '刚'), ('18116ab4e134f9ca34f55254fb3c74e9', '深情'), ('48d78acf65806f11efccfcd0e384ac62', '刚'), ('5a101c51c909c120b3e542258fcda75b', 'f'), ('8cc9a612edb30a4dffe350359f691231', '小孩'), ('921732f8a9d13d32338fa822ed3151c8', '逯刚'), ('973ec59dbda34dfc6e0ee086e81c41b5', '逯刚'), ('eb2aecf7499569432c7f0681b83f4c35', '刚'), ('ebf809d8cee351ac3f42b12276f465a0', '刚'), ('f890d64733db74d5726d05a1ee3fd32c', 'f');
COMMIT;

-- ----------------------------
--  Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` varchar(64) NOT NULL,
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `face_image` varchar(255) DEFAULT NULL COMMENT '我的头像，如果没有默认给一张',
  `nickname` varchar(20) NOT NULL COMMENT '昵称',
  `fans_counts` int(11) DEFAULT '0' COMMENT '我的粉丝数量',
  `follow_counts` int(11) DEFAULT '0' COMMENT '我关注的人总数',
  `receive_like_counts` int(11) DEFAULT '0' COMMENT '我接受到的赞美/收藏 的数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Records of `users`
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES ('d824b26d397f904de0096f738c5e8319', 'lisi', '4QrcOUm6Wau+VuBX8g+IPg==', '/d824b26d397f904de0096f738c5e8319/face/wx61a4f8a8177a9c16.o6zAJs-E2a28M94WCqM7i2Vv1wzo.IASfOvVf4q719e4e20cee19b776f54b41f91776a736e.JPG', 'lisi', '1', '0', '2'), ('f7d02cddff7d58a4ca19b20382b7f06d', 'zhangsan', '4QrcOUm6Wau+VuBX8g+IPg==', '/f7d02cddff7d58a4ca19b20382b7f06d/face/tmp_6ac79095b5e05892657482e3109c8bb2.jpg', 'zhangsan', '0', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `users_fans`
-- ----------------------------
DROP TABLE IF EXISTS `users_fans`;
CREATE TABLE `users_fans` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户',
  `fan_id` varchar(64) NOT NULL COMMENT '粉丝',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`fan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户粉丝关联关系表';

-- ----------------------------
--  Records of `users_fans`
-- ----------------------------
BEGIN;
INSERT INTO `users_fans` VALUES ('e7d32ea17fba34a0c64603b7a7c6c2b6', 'd824b26d397f904de0096f738c5e8319', 'f7d02cddff7d58a4ca19b20382b7f06d');
COMMIT;

-- ----------------------------
--  Table structure for `users_like_videos`
-- ----------------------------
DROP TABLE IF EXISTS `users_like_videos`;
CREATE TABLE `users_like_videos` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '用户',
  `video_id` varchar(64) NOT NULL COMMENT '视频',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_video_rel` (`user_id`,`video_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户喜欢的/赞过的视频';

-- ----------------------------
--  Records of `users_like_videos`
-- ----------------------------
BEGIN;
INSERT INTO `users_like_videos` VALUES ('52ec38eb804c222c969cea6afe892165', 'd824b26d397f904de0096f738c5e8319', '23e142b005f01a2b63a934733d020194'), ('e030e1174595ac713aa829e622837b83', 'f7d02cddff7d58a4ca19b20382b7f06d', '06255f40df6bb0daa48b0541627b86fb');
COMMIT;

-- ----------------------------
--  Table structure for `users_report`
-- ----------------------------
DROP TABLE IF EXISTS `users_report`;
CREATE TABLE `users_report` (
  `id` varchar(64) NOT NULL,
  `deal_user_id` varchar(64) NOT NULL COMMENT '被举报用户id',
  `deal_video_id` varchar(64) NOT NULL,
  `title` varchar(128) NOT NULL COMMENT '类型标题，让用户选择，详情见 枚举',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `userid` varchar(64) NOT NULL COMMENT '举报人的id',
  `create_date` datetime NOT NULL COMMENT '举报时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报用户表';

-- ----------------------------
--  Records of `users_report`
-- ----------------------------
BEGIN;
INSERT INTO `users_report` VALUES ('2b29b7f408402ff87bd6ab4348c7171e', 'f7d02cddff7d58a4ca19b20382b7f06d', '2f284afc5a8b1ac306d536152846469c', '其它原因', '不好看', 'f7d02cddff7d58a4ca19b20382b7f06d', '2019-05-27 17:30:31');
COMMIT;

-- ----------------------------
--  Table structure for `videos`
-- ----------------------------
DROP TABLE IF EXISTS `videos`;
CREATE TABLE `videos` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL COMMENT '发布者id',
  `audio_id` varchar(64) DEFAULT NULL COMMENT '用户使用音频的信息',
  `video_desc` varchar(128) DEFAULT NULL COMMENT '视频描述',
  `video_path` varchar(255) NOT NULL COMMENT '视频存放的路径',
  `video_seconds` float(6,2) DEFAULT NULL COMMENT '视频秒数',
  `video_width` int(6) DEFAULT NULL COMMENT '视频宽度',
  `video_height` int(6) DEFAULT NULL COMMENT '视频高度',
  `cover_path` varchar(255) DEFAULT NULL COMMENT '视频封面图',
  `like_counts` bigint(20) NOT NULL DEFAULT '0' COMMENT '喜欢/赞美的数量',
  `status` int(1) NOT NULL COMMENT '视频状态：\r\n1、发布成功\r\n2、禁止播放，管理员操作',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频信息表';

-- ----------------------------
--  Records of `videos`
-- ----------------------------
BEGIN;
INSERT INTO `videos` VALUES ('060bfa91cc8cbd89d2fe6bb9084ba296', 'd824b26d397f904de0096f738c5e8319', '2051d809e9e06a8da1d23ea9fde84605', 'cesh', '/d824b26d397f904de0096f738c5e8319/video/3b54f26d-eb6a-4855-aef3-4910eaeed470.mp4', '13.61', '1280', '720', '/d824b26d397f904de0096f738c5e8319/video/wx61a4f8a8177a9c16o6zAJs-E2a28M94WCqM7i2Vv1wzogTAf2RCasFAY11861adde12f89392718a50e0ff514f8.gif', '0', '1', '2019-06-04 15:57:33'), ('06255f40df6bb0daa48b0541627b86fb', 'd824b26d397f904de0096f738c5e8319', '', '吃烧烤啦', '/d824b26d397f904de0096f738c5e8319/video/tmp_c433390df866818383e2bd3d7d2e2a1a.mp4', '3.00', '540', '960', '/d824b26d397f904de0096f738c5e8319/video/tmp_c433390df866818383e2bd3d7d2e2a1a.gif', '1', '1', '2019-05-27 03:04:39'), ('228022cfb0088feb0f57586fa934adf1', 'f7d02cddff7d58a4ca19b20382b7f06d', '', '深情的逯刚', '/f7d02cddff7d58a4ca19b20382b7f06d/video/tmp_3a1e148b029c41302fca3da74c65b8ef.mp4', '11.00', '540', '960', '/f7d02cddff7d58a4ca19b20382b7f06d/video/tmp_3a1e148b029c41302fca3da74c65b8ef.gif', '1', '1', '2019-05-27 03:03:31'), ('23e142b005f01a2b63a934733d020194', 'd824b26d397f904de0096f738c5e8319', '18052674D26HH32P', '', '/d824b26d397f904de0096f738c5e8319/video/2885c8da-46b9-417a-b70e-46c2f7552124.mp4', '13.61', '1280', '720', '/d824b26d397f904de0096f738c5e8319/video/wx61a4f8a8177a9c16o6zAJs-E2a28M94WCqM7i2Vv1wzozkqGrtBe6UCN11861adde12f89392718a50e0ff514f8.gif', '1', '1', '2019-06-02 15:59:09'), ('2f284afc5a8b1ac306d536152846469c', 'f7d02cddff7d58a4ca19b20382b7f06d', '', '', '/f7d02cddff7d58a4ca19b20382b7f06d/video/tmp_a2590958c15de43a1ec0e9aecda3e098.mp4', '5.00', '544', '960', '/f7d02cddff7d58a4ca19b20382b7f06d/video/tmp_a2590958c15de43a1ec0e9aecda3e098.gif', '0', '1', '2019-05-27 03:03:51'), ('300a4b0fa74e10f7468c0ae07550e255', 'f7d02cddff7d58a4ca19b20382b7f06d', '18052674D26HH32P', '', '/f7d02cddff7d58a4ca19b20382b7f06d/video/6ef21b12-cdf0-4360-8b8c-e1514e541d01.mp4', '2.83', '540', '960', '/f7d02cddff7d58a4ca19b20382b7f06d/video/wx61a4f8a8177a9c16HK4yaLx4Aegm91aec096484c7601ee4f83ad470bd12e.gif', '0', '1', '2019-05-28 17:27:25'), ('8589df4a05cbf28b5f0425003b681873', 'f7d02cddff7d58a4ca19b20382b7f06d', '18052674D26HH32P', '嘟嘟嘟嘟', '/f7d02cddff7d58a4ca19b20382b7f06d/video/ce861c45-18b9-41dd-9198-4e307fcdf6e0.mp4', '5.00', '540', '960', '/f7d02cddff7d58a4ca19b20382b7f06d/video/tmp_070434f014f6cf625e9476ed15d83a8e.gif', '0', '1', '2019-05-27 03:04:09'), ('ccfe93a7b70232fd7408312b678a1132', 'f7d02cddff7d58a4ca19b20382b7f06d', '18052674D26HH32P', '小孩真调皮', '/f7d02cddff7d58a4ca19b20382b7f06d/video/0e645fdf-57aa-4ac8-8f04-e30149be2ee6.mp4', '7.00', '544', '960', '/f7d02cddff7d58a4ca19b20382b7f06d/video/tmp_1706d96c34b8c466b06599ba8ad8744b.gif', '0', '2', '2019-05-27 17:14:55');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
