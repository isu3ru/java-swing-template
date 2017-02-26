/*
Navicat MySQL Data Transfer

Source Server         : Local MySQL Server
Source Server Version : 50505
Source Host           : 127.0.0.1:3306
Source Database       : application_database

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2017-02-02 17:10:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for login_history
-- ----------------------------
DROP TABLE IF EXISTS `login_history`;
CREATE TABLE `login_history` (
  `user_id` int(11) DEFAULT NULL,
  `logged_in_time` datetime DEFAULT NULL,
  `login_hash` varchar(255) DEFAULT NULL,
  KEY `user_id` (`user_id`),
  CONSTRAINT `login_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of login_history
-- ----------------------------

-- ----------------------------
-- Table structure for system_privileges
-- ----------------------------
DROP TABLE IF EXISTS `system_privileges`;
CREATE TABLE `system_privileges` (
  `prv_id` int(11) NOT NULL AUTO_INCREMENT,
  `prv_code` varchar(45) DEFAULT NULL,
  `prv_name` varchar(45) DEFAULT NULL,
  `prv_display_name` varchar(255) DEFAULT NULL,
  `prv_parent` int(11) DEFAULT '0',
  PRIMARY KEY (`prv_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_privileges
-- ----------------------------
INSERT INTO `system_privileges` VALUES ('1', '101', 'REPORTS', 'Reports View', '0');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) DEFAULT NULL,
  `user_display_name` varchar(255) DEFAULT NULL,
  `user_password` varchar(80) DEFAULT NULL,
  `user_level` int(11) DEFAULT NULL,
  `user_status` int(11) DEFAULT NULL,
  `user_type` int(11) DEFAULT NULL,
  `user_close_time` datetime DEFAULT NULL,
  `user_logged_last` datetime DEFAULT NULL,
  `last_logged_hash` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `user_level` (`user_level`) USING BTREE,
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`user_level`) REFERENCES `user_levels` (`level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('3', 'admin', 'administrator', '*4ACFE3202A5FF5CF467898FC58AAB1D615029441', '1', '1', '1', null, '2017-02-02 01:42:20', '2c60d692-b876-457f-8604-8bd45ad8b47e');

-- ----------------------------
-- Table structure for user_levels
-- ----------------------------
DROP TABLE IF EXISTS `user_levels`;
CREATE TABLE `user_levels` (
  `level_id` int(11) NOT NULL AUTO_INCREMENT,
  `level_code` varchar(45) DEFAULT NULL,
  `level_name` varchar(45) DEFAULT NULL,
  `level_display_name` varchar(45) DEFAULT NULL,
  `level_active` tinyint(1) DEFAULT '1' COMMENT 'i is for ''active'' status and 0 is for ''inactive'' status',
  PRIMARY KEY (`level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_levels
-- ----------------------------
INSERT INTO `user_levels` VALUES ('1', '001', 'Level1', 'Administrator', '1');

-- ----------------------------
-- Table structure for user_level_privileges
-- ----------------------------
DROP TABLE IF EXISTS `user_level_privileges`;
CREATE TABLE `user_level_privileges` (
  `level_id` int(11) NOT NULL,
  `prv_id` int(11) NOT NULL,
  PRIMARY KEY (`level_id`,`prv_id`),
  KEY `prv_id` (`prv_id`) USING BTREE,
  KEY `level_id` (`level_id`) USING BTREE,
  CONSTRAINT `user_level_privileges_ibfk_1` FOREIGN KEY (`prv_id`) REFERENCES `system_privileges` (`prv_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_level_privileges_ibfk_2` FOREIGN KEY (`level_id`) REFERENCES `user_levels` (`level_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_level_privileges
-- ----------------------------
INSERT INTO `user_level_privileges` VALUES ('1', '1');

-- ----------------------------
-- Table structure for user_privileges
-- ----------------------------
DROP TABLE IF EXISTS `user_privileges`;
CREATE TABLE `user_privileges` (
  `user_id` int(11) DEFAULT NULL,
  `prv_id` int(11) DEFAULT NULL,
  UNIQUE KEY `user_priv_combo` (`user_id`,`prv_id`) USING BTREE,
  KEY `wb_user_privileges_ibfk_1` (`prv_id`) USING BTREE,
  CONSTRAINT `user_privileges_ibfk_1` FOREIGN KEY (`prv_id`) REFERENCES `system_privileges` (`prv_id`) ON UPDATE CASCADE,
  CONSTRAINT `user_privileges_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_privileges
-- ----------------------------
INSERT INTO `user_privileges` VALUES ('3', '1');

-- ----------------------------
-- Procedure structure for COMBINED
-- ----------------------------
DROP PROCEDURE IF EXISTS `COMBINED`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` PROCEDURE `COMBINED`(IN `year_num` int,IN `cust_id` varchar(45),OUT `prv_units` int,OUT `prv_bal` double,OUT `prv_arr` double,OUT `prv_warr` double,OUT `new_units` int,OUT `new_bal` double,OUT `new_arr` double,OUT `new_warr` double,OUT `chg_units` int,OUT `chg_bal` double,OUT `chg_arr` double,OUT `chg_warr` double,OUT `prevw` double,OUT `addw` double,OUT `aftw` double)
    SQL SECURITY INVOKER
BEGIN
SELECT
SUM(bal_update_log.priv_units),
SUM(bal_update_log.priv_bal),
SUM(bal_update_log.priv_arrears),
SUM(bal_update_log.priv_warrant),
SUM(bal_update_log.new_units),
SUM(bal_update_log.new_balance),
SUM(bal_update_log.new_arrears),
SUM(bal_update_log.new_warrant),
SUM(bal_update_log.change_units),
SUM(bal_update_log.change_balAmt),
SUM(bal_update_log.change_arrears),
SUM(bal_update_log.change_warrant), 
(SELECT
	SUM(bill_balance_log.prev_warrant)
	FROM
	bill_balance_log
	WHERE
	YEAR(bill_balance_log.time) = 2016 AND
	bill_balance_log.customer_id = 10002) AS SUM_PRE_WARNT,
(SELECT
	SUM(bill_balance_log.added_warrant)
	FROM
	bill_balance_log
	WHERE
	YEAR(bill_balance_log.time) = 2016 AND
	bill_balance_log.customer_id = 10002) AS SUM_ADDED_WARNT,
(SELECT
	SUM(bill_balance_log.after_warrant)
	FROM
	bill_balance_log
	WHERE
	YEAR(bill_balance_log.time) = 2016 AND
	bill_balance_log.customer_id = 10002) AS SUM_AFTER_WARNT
INTO `prv_units`, `prv_bal`, `prv_arr`, `prv_warr`, `new_units`, `new_bal`, `new_arr`, `new_warr`, 
`chg_units`, `chg_bal`, `chg_arr`, `chg_warr`, `prevw`, `addw`, `aftw`  
FROM bal_update_log
WHERE
bal_update_log.customer_id = 10002 AND
bal_update_log.entry_type <> 2 AND
YEAR(bal_update_log.update_date_time) = 2016;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for CP_BALANCE_ADJUSTMENTS
-- ----------------------------
DROP PROCEDURE IF EXISTS `CP_BALANCE_ADJUSTMENTS`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` PROCEDURE `CP_BALANCE_ADJUSTMENTS`(IN `year_num` int,IN `cust_id` varchar(45),OUT `prv_units` int,OUT `prv_bal` double,OUT `prv_arr` double,OUT `prv_warr` double,OUT `new_units` int,OUT `new_bal` double,OUT `new_arr` double,OUT `new_warr` double,OUT `chg_units` int,OUT `chg_bal` double,OUT `chg_arr` double,OUT `chg_warr` double)
    SQL SECURITY INVOKER
BEGIN
	SELECT
SUM(bal_update_log.priv_units),
SUM(bal_update_log.priv_bal),
SUM(bal_update_log.priv_arrears),
SUM(bal_update_log.priv_warrant),
SUM(bal_update_log.new_units),
SUM(bal_update_log.new_balance),
SUM(bal_update_log.new_arrears),
SUM(bal_update_log.new_warrant),
SUM(bal_update_log.change_units),
SUM(bal_update_log.change_balAmt),
SUM(bal_update_log.change_arrears),
SUM(bal_update_log.change_warrant) 
INTO `prv_units`, `prv_bal`, `prv_arr`, `prv_warr`, `new_units`, `new_bal`, `new_arr`, `new_warr`, 
`chg_units`, `chg_bal`, `chg_arr`, `chg_warr` 
FROM bal_update_log
WHERE
bal_update_log.customer_id = cust_id AND
bal_update_log.entry_type <> 2 AND
YEAR(bal_update_log.update_date_time) = year_num;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for CP_BILL_ISSUINGS
-- ----------------------------
DROP PROCEDURE IF EXISTS `CP_BILL_ISSUINGS`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` PROCEDURE `CP_BILL_ISSUINGS`(IN `year_num` int,IN `cust_number` varchar(45),OUT `prevw` double,OUT `addw` double,OUT `aftw` double)
    SQL SECURITY INVOKER
BEGIN
SELECT
	SUM(B1.prev_warrant) AS prev_warrant,
	SUM(B1.added_warrant) AS added_warrant,
	SUM(B1.after_warrant) AS after_warrant 
INTO prevw, addw, aftw 
FROM
	(
		SELECT
			*
		FROM
			bill_balance_log
		WHERE
			YEAR (bill_balance_log.time) = year_num
		AND bill_balance_log.customer_id = cust_number
		GROUP BY
			bill_balance_log.bill_id
	) B1;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for CP_BILL_UPDATES
-- ----------------------------
DROP PROCEDURE IF EXISTS `CP_BILL_UPDATES`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` PROCEDURE `CP_BILL_UPDATES`(IN `yearnum` int, IN `custid` VARCHAR(45), OUT `WCTOTAL` double,OUT `FCTOTAL` double,OUT `ECTOTAL` double,OUT `TCTOTAL` double,OUT `SUBTOTAL` double,OUT `GRAND_TOTAL` double)
    SQL SECURITY INVOKER
BEGIN
	SELECT
	SUM(bill_charges.water_charge),
	SUM(bill_charges.fixed_charge),
	SUM(bill_charges.electricity_charges),
	SUM(bill_charges.tax_charge),
	SUM(bill_charges.water_charge + bill_charges.fixed_charge + bill_charges.electricity_charges),
	SUM(bill_charges.water_charge + bill_charges.fixed_charge + bill_charges.electricity_charges + bill_charges.tax_charge) 
	INTO WCTOTAL, FCTOTAL, ECTOTAL, TCTOTAL, SUBTOTAL, GRAND_TOTAL
FROM
	(
		SELECT
			*
		FROM
			bill_balance_log
		WHERE
			bill_balance_log.customer_id = custid
		AND YEAR (bill_balance_log.time) = yearnum
		GROUP BY
			bill_balance_log.bill_id ASC
	) BB1
INNER JOIN bill_charges ON BB1.bill_id = bill_charges.bill_id;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for CP_CUSTOMER_SUMMARY_BY_ROAD
-- ----------------------------
DROP PROCEDURE IF EXISTS `CP_CUSTOMER_SUMMARY_BY_ROAD`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` PROCEDURE `CP_CUSTOMER_SUMMARY_BY_ROAD`(IN `year_no` int(11), IN `rdnum` int(11))
    SQL SECURITY INVOKER
BEGIN
	# customer fields
	DECLARE `custid` VARCHAR(45) DEFAULT '';
	DECLARE `custname` VARCHAR(255) DEFAULT '';
	DECLARE `cust_status` TINYINT(1) DEFAULT 0;

	# balance update fields
	DECLARE `BU_WC` DOUBLE DEFAULT 0.0;
	DECLARE `BU_FC` DOUBLE DEFAULT 0.0;
	DECLARE `BU_EC` DOUBLE DEFAULT 0.0;
	DECLARE `BU_TC` DOUBLE DEFAULT 0.0;
	DECLARE `BU_SUB` DOUBLE DEFAULT 0.0;
	DECLARE `BU_GRAND` DOUBLE DEFAULT 0.0;

	# bill issuings fields
	DECLARE `prevw` DOUBLE DEFAULT 0.0;
	DECLARE `addw` DOUBLE DEFAULT 0.0;
	DECLARE `aftw` DOUBLE DEFAULT 0.0;

	# payments fields
	DECLARE `artot` DOUBLE DEFAULT 0.0;
	DECLARE `wctot` DOUBLE DEFAULT 0.0;
	DECLARE `wartot` DOUBLE DEFAULT 0.0;
	DECLARE `totwovat` DOUBLE DEFAULT 0.0;
	DECLARE `vattot` DOUBLE DEFAULT 0.0;
	DECLARE `totwvat` DOUBLE DEFAULT 0.0;

	# balance adjustments
	DECLARE `prv_units` INT DEFAULT 0.0;
  DECLARE `prv_bal` DOUBLE DEFAULT 0.0;
  DECLARE `prv_arr` DOUBLE DEFAULT 0.0;
  DECLARE `prv_warr` DOUBLE DEFAULT 0.0; 
  DECLARE `new_units` INT DEFAULT 0.0; 
  DECLARE `new_bal` DOUBLE DEFAULT 0.0;
  DECLARE `new_arr` DOUBLE DEFAULT 0.0;
  DECLARE `new_warr` DOUBLE DEFAULT 0.0; 
  DECLARE `chg_units` INT DEFAULT 0.0;
  DECLARE `chg_bal` DOUBLE DEFAULT 0.0;
  DECLARE `chg_arr` DOUBLE DEFAULT 0.0;
  DECLARE `chg_warr` DOUBLE DEFAULT 0.0;

	DECLARE l_last_row INT(11) DEFAULT 0;

	# send select
	DECLARE custs_cur CURSOR FOR 
	SELECT customers.customer_id, customers.customer_name, customers.active_status  
	FROM customers
	WHERE customers.road_name = rdnum AND customers.active_status = 1;

	# continue handler
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET l_last_row=1;
	
	# clear the table before data insertion
	TRUNCATE TABLE `detailed_summary_table`;

	# open and loop the cursor
	OPEN custs_cur;
    cust_curloop: LOOP
				-- GET EACH CUSTOMER'S ID AND NAME
        FETCH custs_cur INTO `custid`, `custname`, `cust_status`;

				IF l_last_row = 1 THEN
					LEAVE cust_curloop;
				END IF;

				-- GET YEAR STARTING BALANCE FROM ANNUAL PROCESS
				set @startingbal = CP_YEAR_START_BALANCE(custid, year_no);
				
				-- GET TOTAL BILL ISSUINGS VALUES
				CALL CP_BILL_ISSUINGS(year_no, `custid`, `prevw`, `addw`, `aftw`); 
				
				-- GET TOTAL BILL UPDATES VALUES
				CALL CP_BILL_UPDATES(year_no, `custid`, BU_WC, BU_FC, BU_EC, BU_TC, BU_SUB, BU_GRAND);

				-- GET TOTAL PAYMENT VALUES
				CALL CP_PAYMENTS(year_no, `custid`, `artot`, `wctot`, `wartot`, `totwovat`, `vattot`, `totwvat`);
				
				-- GET TOTAL BALANCE ADJUSTMENTS
				CALL CP_BALANCE_ADJUSTMENTS(year_no, `custid`, `prv_units`, `prv_bal`, `prv_arr`, `prv_warr`, `new_units`, `new_bal`, `new_arr`, 
				`new_warr`, `chg_units`, `chg_bal`, `chg_arr`, `chg_warr`);

				# send entry to detailed summary table
				INSERT INTO `detailed_summary_table` (`year_num`, `road_no`, `custid`, `custname`, `cust_status`, `starting_balance`, `bu_wc`, `bu_fc`, `bu_ec`, 
				`bu_tc`, `bu_sub`, `bu_grand`, `bi_prevw`, `bi_addw`, `bi_aftw`, `pay_artot`, `pay_wctot`, `pay_wartot`, `pay_totwovat`, 
				`pay_vattot`, `pay_totwvat`, `ba_prv_units`, `ba_prv_bal`, `ba_prv_arr`, `ba_prv_warr`, `ba_new_units`, `ba_new_bal`, 
				`ba_new_arr`, `ba_new_warr`, `ba_chg_units`, `ba_chg_bal`, `ba_chg_arr`, `ba_chg_warr`) 
				VALUES (`year_no`, `rdnum`, `custid`, `custname`, `cust_status`, @startingbal, `BU_WC` , `BU_FC` , BU_EC, 
				`BU_TC`, `BU_SUB` , `BU_GRAND`, prevw, addw, aftw, artot, wctot, wartot, totwovat, 
				vattot, totwvat, prv_units, prv_bal, prv_arr, prv_warr, new_units, new_bal, 
				new_arr, new_warr, chg_units, chg_bal, chg_arr, chg_warr);

    END LOOP cust_curloop;
 CLOSE custs_cur;#end and close of the loop
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for CP_CUSTOMER_SUMMARY_BY_ROADS
-- ----------------------------
DROP PROCEDURE IF EXISTS `CP_CUSTOMER_SUMMARY_BY_ROADS`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` PROCEDURE `CP_CUSTOMER_SUMMARY_BY_ROADS`(IN `year_no` int(11), IN `rdnum` int(11))
    SQL SECURITY INVOKER
BEGIN
	# customer fields
	DECLARE `custid` VARCHAR(45) DEFAULT '';
	DECLARE `custname` VARCHAR(255) DEFAULT '';
	DECLARE `cust_status` TINYINT(1) DEFAULT 0;

	# balance update fields
	DECLARE `BU_WC` DOUBLE DEFAULT 0.0;
	DECLARE `BU_FC` DOUBLE DEFAULT 0.0;
	DECLARE `BU_EC` DOUBLE DEFAULT 0.0;
	DECLARE `BU_TC` DOUBLE DEFAULT 0.0;
	DECLARE `BU_SUB` DOUBLE DEFAULT 0.0;
	DECLARE `BU_GRAND` DOUBLE DEFAULT 0.0;

	# bill issuings fields
	DECLARE `prevw` DOUBLE DEFAULT 0.0;
	DECLARE `addw` DOUBLE DEFAULT 0.0;
	DECLARE `aftw` DOUBLE DEFAULT 0.0;

	# payments fields
	DECLARE `artot` DOUBLE DEFAULT 0.0;
	DECLARE `wctot` DOUBLE DEFAULT 0.0;
	DECLARE `wartot` DOUBLE DEFAULT 0.0;
	DECLARE `totwovat` DOUBLE DEFAULT 0.0;
	DECLARE `vattot` DOUBLE DEFAULT 0.0;
	DECLARE `totwvat` DOUBLE DEFAULT 0.0;

	# balance adjustments
	DECLARE `prv_units` INT DEFAULT 0.0;
  DECLARE `prv_bal` DOUBLE DEFAULT 0.0;
  DECLARE `prv_arr` DOUBLE DEFAULT 0.0;
  DECLARE `prv_warr` DOUBLE DEFAULT 0.0; 
  DECLARE `new_units` INT DEFAULT 0.0; 
  DECLARE `new_bal` DOUBLE DEFAULT 0.0;
  DECLARE `new_arr` DOUBLE DEFAULT 0.0;
  DECLARE `new_warr` DOUBLE DEFAULT 0.0; 
  DECLARE `chg_units` INT DEFAULT 0.0;
  DECLARE `chg_bal` DOUBLE DEFAULT 0.0;
  DECLARE `chg_arr` DOUBLE DEFAULT 0.0;
  DECLARE `chg_warr` DOUBLE DEFAULT 0.0;

	DECLARE l_last_row INT(11) DEFAULT 0;

	# send select
	DECLARE custs_cur CURSOR FOR 
	SELECT customers.customer_id, customers.customer_name, customers.active_status  
	FROM customers
	WHERE customers.road_name = rdnum AND customers.active_status = 1;

	# continue handler
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET l_last_row=1;

	SET autocommit=0; # start transactions

	# open and loop the cursor
	OPEN custs_cur;
    cust_curloop: LOOP
				-- GET EACH CUSTOMER'S ID AND NAME
        FETCH custs_cur INTO `custid`, `custname`, `cust_status`;

				IF l_last_row = 1 THEN
					LEAVE cust_curloop;
				END IF;

				-- GET YEAR STARTING BALANCE FROM ANNUAL PROCESS
				set @startingbal = CP_YEAR_START_BALANCE(custid, year_no);

				-- GET TOTAL BILL UPDATES VALUES
				CALL CP_BILL_UPDATES(year_no, `custid`, BU_WC, BU_FC, BU_EC, BU_TC, BU_SUB, BU_GRAND);

				-- GET TOTAL BILL ISSUINGS VALUES
				CALL CP_BILL_ISSUINGS(year_no, `custid`, `prevw`, `addw`, `aftw`);

				-- GET TOTAL PAYMENT VALUES
				CALL CP_PAYMENTS(year_no, `custid`, `artot`, `wctot`, `wartot`, `totwovat`, `vattot`, `totwvat`);

				-- GET TOTAL BALANCE ADJUSTMENTS
				CALL CP_BALANCE_ADJUSTMENTS(year_no, `custid`, `prv_units`, `prv_bal`, `prv_arr`, `prv_warr`, `new_units`, `new_bal`, `new_arr`, 
				`new_warr`, `chg_units`, `chg_bal`, `chg_arr`, `chg_warr`);

				# send entry to detailed summary table
				INSERT INTO `detailed_summary_table` (`year_num`, `road_no`, `custid`, `custname`, `cust_status`, `bu_wc`, `bu_fc`, `bu_ec`, 
				`bu_tc`, `bu_sub`, `bu_grand`, `bi_prevw`, `bi_addw`, `bi_aftw`, `pay_artot`, `pay_wctot`, `pay_wartot`, `pay_totwovat`, 
				`pay_vattot`, `pay_totwvat`, `ba_prv_units`, `ba_prv_bal`, `ba_prv_arr`, `ba_prv_warr`, `ba_new_units`, `ba_new_bal`, 
				`ba_new_arr`, `ba_new_warr`, `ba_chg_units`, `ba_chg_bal`, `ba_chg_arr`, `ba_chg_warr`) 
				VALUES (`year_no`, `rdnum`, `custid`, `custname`, `cust_status`, `BU_WC` , `BU_FC` , BU_EC, 
				`BU_TC`, `BU_SUB` , `BU_GRAND`, prevw, addw, aftw, artot, wctot, wartot, totwovat, 
				vattot, totwvat, prv_units, prv_bal, prv_arr, prv_warr, new_units, new_bal, 
				new_arr, new_warr, chg_units, chg_bal, chg_arr, chg_warr);

    END LOOP cust_curloop;
  CLOSE custs_cur; #end and close of the loop
	COMMIT;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for CP_CUSTOMER_SUMMARY_BY_ROAD_SINGLE
-- ----------------------------
DROP PROCEDURE IF EXISTS `CP_CUSTOMER_SUMMARY_BY_ROAD_SINGLE`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` PROCEDURE `CP_CUSTOMER_SUMMARY_BY_ROAD_SINGLE`(IN `year_no` int(11), IN `rdnum` int(11), IN `in_cust` VARCHAR(45))
    SQL SECURITY INVOKER
BEGIN
	# customer fields
	DECLARE `custid` VARCHAR(45) DEFAULT '';
	DECLARE `custname` VARCHAR(255) DEFAULT '';
	DECLARE `cust_status` TINYINT(1) DEFAULT 0;

	# balance update fields
	DECLARE `BU_WC` DOUBLE DEFAULT 0.0;
	DECLARE `BU_FC` DOUBLE DEFAULT 0.0;
	DECLARE `BU_EC` DOUBLE DEFAULT 0.0;
	DECLARE `BU_TC` DOUBLE DEFAULT 0.0;
	DECLARE `BU_SUB` DOUBLE DEFAULT 0.0;
	DECLARE `BU_GRAND` DOUBLE DEFAULT 0.0;

	# bill issuings fields
	DECLARE `prevw` DOUBLE DEFAULT 0.0;
	DECLARE `addw` DOUBLE DEFAULT 0.0;
	DECLARE `aftw` DOUBLE DEFAULT 0.0;

	# payments fields
	DECLARE `artot` DOUBLE DEFAULT 0.0;
	DECLARE `wctot` DOUBLE DEFAULT 0.0;
	DECLARE `wartot` DOUBLE DEFAULT 0.0;
	DECLARE `totwovat` DOUBLE DEFAULT 0.0;
	DECLARE `vattot` DOUBLE DEFAULT 0.0;
	DECLARE `totwvat` DOUBLE DEFAULT 0.0;

	# balance adjustments
	DECLARE `prv_units` INT DEFAULT 0.0;
  DECLARE `prv_bal` DOUBLE DEFAULT 0.0;
  DECLARE `prv_arr` DOUBLE DEFAULT 0.0;
  DECLARE `prv_warr` DOUBLE DEFAULT 0.0; 
  DECLARE `new_units` INT DEFAULT 0.0; 
  DECLARE `new_bal` DOUBLE DEFAULT 0.0;
  DECLARE `new_arr` DOUBLE DEFAULT 0.0;
  DECLARE `new_warr` DOUBLE DEFAULT 0.0; 
  DECLARE `chg_units` INT DEFAULT 0.0;
  DECLARE `chg_bal` DOUBLE DEFAULT 0.0;
  DECLARE `chg_arr` DOUBLE DEFAULT 0.0;
  DECLARE `chg_warr` DOUBLE DEFAULT 0.0;

	DECLARE l_last_row INT(11) DEFAULT 0;

	# send select
	DECLARE custs_cur CURSOR FOR 
	SELECT customers.customer_id, customers.customer_name, customers.active_status  
	FROM customers
	WHERE customers.road_name = rdnum AND customers.active_status = 1 AND customers.customer_id = in_cust;

	# continue handler
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET l_last_row=1;
	
	# clear the table before data insertion
	TRUNCATE TABLE `detailed_summary_table`;

	SET autocommit=0; # start transactions

	# open and loop the cursor
	OPEN custs_cur;
    cust_curloop: LOOP
				-- GET EACH CUSTOMER'S ID AND NAME
        FETCH custs_cur INTO `custid`, `custname`, `cust_status`;

				IF l_last_row = 1 THEN
					LEAVE cust_curloop;
				END IF;

				-- GET YEAR STARTING BALANCE FROM ANNUAL PROCESS
				set @startingbal = CP_YEAR_START_BALANCE(custid, year_no);
				
				-- GET TOTAL BILL ISSUINGS VALUES
				CALL CP_BILL_ISSUINGS(year_no, `custid`, `prevw`, `addw`, `aftw`); 
				
				-- GET TOTAL BILL UPDATES VALUES
				CALL CP_BILL_UPDATES(year_no, `custid`, BU_WC, BU_FC, BU_EC, BU_TC, BU_SUB, BU_GRAND);

				-- GET TOTAL PAYMENT VALUES
				CALL CP_PAYMENTS(year_no, `custid`, `artot`, `wctot`, `wartot`, `totwovat`, `vattot`, `totwvat`);
				
				-- GET TOTAL BALANCE ADJUSTMENTS
				CALL CP_BALANCE_ADJUSTMENTS(year_no, `custid`, `prv_units`, `prv_bal`, `prv_arr`, `prv_warr`, `new_units`, `new_bal`, `new_arr`, 
				`new_warr`, `chg_units`, `chg_bal`, `chg_arr`, `chg_warr`);

				
				# send entry to detailed summary table
				INSERT INTO `detailed_summary_table` (`year_num`, `road_no`, `custid`, `custname`, `cust_status`, `bu_wc`, `bu_fc`, `bu_ec`, 
				`bu_tc`, `bu_sub`, `bu_grand`, `bi_prevw`, `bi_addw`, `bi_aftw`, `pay_artot`, `pay_wctot`, `pay_wartot`, `pay_totwovat`, 
				`pay_vattot`, `pay_totwvat`, `ba_prv_units`, `ba_prv_bal`, `ba_prv_arr`, `ba_prv_warr`, `ba_new_units`, `ba_new_bal`, 
				`ba_new_arr`, `ba_new_warr`, `ba_chg_units`, `ba_chg_bal`, `ba_chg_arr`, `ba_chg_warr`) 
				VALUES (`year_no`, `rdnum`, `custid`, `custname`, `cust_status`, `BU_WC` , `BU_FC` , BU_EC, 
				`BU_TC`, `BU_SUB` , `BU_GRAND`, prevw, addw, aftw, artot, wctot, wartot, totwovat, 
				vattot, totwvat, prv_units, prv_bal, prv_arr, prv_warr, new_units, new_bal, 
				new_arr, new_warr, chg_units, chg_bal, chg_arr, chg_warr);

    END LOOP cust_curloop;
 CLOSE custs_cur;#end and close of the loop
COMMIT;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for CP_PAYMENTS
-- ----------------------------
DROP PROCEDURE IF EXISTS `CP_PAYMENTS`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` PROCEDURE `CP_PAYMENTS`(IN `year_num` int,IN `cust_num` varchar(45),OUT `artot` double,OUT `wctot` double,OUT `wartot` double,OUT `totwovat` double,OUT `vattot` double,OUT `totwvat` double)
    SQL SECURITY INVOKER
BEGIN
	SELECT
	SUM(
		nemc_water_billing.wb_payment.arrears_paid_amt
	),
	SUM(
		nemc_water_billing.wb_payment.tax_paid_amt
	),
	SUM(
		nemc_water_billing.wb_payment.warrant_paid_amt
	),
	SUM(
		nemc_water_billing.wb_payment.tax_paid_amt + nemc_water_billing.wb_payment.warrant_paid_amt
	),
	SUM(
		nemc_water_billing.wb_payment.vat_paid_amt
	),
	SUM(
		nemc_water_billing.wb_payment.total_paid_amnt
	)
INTO artot, wctot, wartot, totwovat, vattot, totwvat 
FROM
	nemc_water_billing.wb_payment
RIGHT JOIN cashcollect.cobill ON nemc_water_billing.wb_payment.pay_id = cashcollect.cobill.trnID
WHERE
	nemc_water_billing.wb_payment.cus_id = cust_num
AND nemc_water_billing.wb_payment.pay_status = 1
AND YEAR (
	nemc_water_billing.wb_payment.payment_date
) = year_num
AND cashcollect.cobill.proID = 5;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for TEST
-- ----------------------------
DROP PROCEDURE IF EXISTS `TEST`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` PROCEDURE `TEST`(`cu_id` varchar(45))
    SQL SECURITY INVOKER
BEGIN
	DECLARE l_ret_val TINYINT(1) DEFAULT 0;
	DECLARE l_last_row_fetched INT DEFAULT 0;
	DECLARE l_rb_count INT DEFAULT 0;

	DECLARE t_bill_id VARCHAR(10);
	DECLARE t_bill_status TINYINT(1);

	DECLARE c_redbill CURSOR FOR 
	SELECT issued_bills.bill_id,issued_bills.bill_status 
	FROM issued_bills 
	WHERE issued_bills.customer_id = cu_id 
	ORDER BY issued_bills.bill_date DESC LIMIT 0, 3;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET l_last_row_fetched=1;

	OPEN c_redbill;
	l_rbcurl: LOOP
			FETCH c_redbill INTO t_bill_id, t_bill_status;

			IF t_bill_status != 3 THEN 
				SET l_rb_count = l_rb_count + 1;
			END IF;

			IF l_last_row_fetched=1 THEN
					 LEAVE l_rbcurl;
			END IF;
	END LOOP l_rbcurl;
	CLOSE c_redbill;

	IF l_rb_count = 3 THEN
		SET l_ret_val = 1;
	END IF;

 SELECT l_rb_count,l_ret_val;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for TESTPROC
-- ----------------------------
DROP PROCEDURE IF EXISTS `TESTPROC`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `TESTPROC`()
BEGIN
	SELECT
	roads.road_id AS ROAD_ID,
	roads.road_name AS ROAD_NAME,
	customers.customer_id AS CUSID,
	( SELECT DATE(bill_balance_log.time) FROM bill_balance_log 
	INNER JOIN issued_bills ON issued_bills.bill_id = bill_balance_log.bill_id 
	WHERE bill_balance_log.customer_id = CUSID AND issued_bills.bill_year = 2016 
	ORDER BY bill_balance_log.time ASC LIMIT 1 ) AS FIRSTBILLDATE,
	CONCAT(
		customers.customer_id,
		' - ',
		customers.customer_name
	) AS CUSTOMER,
	(
		SELECT
			issued_bills.current_balance
		FROM
			issued_bills
		INNER JOIN customers ON customers.customer_id = issued_bills.customer_id
		WHERE
			issued_bills.customer_id = CUSID
		AND issued_bills.bill_year = 2016
		ORDER BY
			`bill_date`
		LIMIT 1
	) AS STARTINGBAL,
	Sum(bill_charges.water_charge) AS WATER,
	Sum(bill_charges.fixed_charge) AS FIXED,
	Sum(bill_charges.tax_charge) AS VAT,
	Sum( bill_charges.warrant_charge ) AS WARRANT,
	Sum( bill_charges.electricity_charges ) AS ELECTRICITY,
	(
	SELECT
	SUM(wb_payment.total_paid_amnt)
	FROM
	wb_payment
	WHERE
	wb_payment.cus_id = CUSID AND
	wb_payment.payment_date > FIRSTBILLDATE
	) AS TOTPAID,
(
	SELECT
	SUM(wb_payment.tax_paid_amt)
	FROM
	wb_payment
	WHERE
	wb_payment.cus_id = CUSID AND
	wb_payment.payment_date > FIRSTBILLDATE
	) AS TOTTAXPAID, 
	(
	SELECT
	SUM(wb_payment.vat_paid_amt)
	FROM
	wb_payment
	WHERE
	wb_payment.cus_id = CUSID AND
	wb_payment.payment_date > FIRSTBILLDATE
	) AS TOTVATPAID,
	(
	SELECT
	SUM(wb_payment.arrears_paid_amt)
	FROM
	wb_payment
	WHERE
	wb_payment.cus_id = CUSID AND
	wb_payment.payment_date > FIRSTBILLDATE
	) AS TOTARREARSPAID,
	(
	SELECT
	SUM(wb_payment.warrant_paid_amt)
	FROM
	wb_payment
	WHERE
	wb_payment.cus_id = CUSID AND
	wb_payment.payment_date > FIRSTBILLDATE
	) AS TOTWARRANTPAID,
	(
	SELECT IFNULL(SUM(bal_update_log.new_balance - bal_update_log.priv_bal), 0.0) AS baladj
	FROM bal_update_log
	WHERE
	bal_update_log.customer_id = issued_bills.customer_id AND
	bal_update_log.entry_type <> 2 AND
	bal_update_log.update_date_time >= FIRSTBILLDATE
	) AS baladjustments
FROM
	issued_bills
INNER JOIN customers ON customers.customer_id = issued_bills.customer_id
INNER JOIN roads ON roads.road_id = customers.road_name
INNER JOIN bill_charges ON bill_charges.bill_id = issued_bills.bill_id
WHERE
	issued_bills.bill_year = 2016
AND customers.road_name = 6
GROUP BY
	issued_bills.customer_id
ORDER BY
	customers.road_name ASC,
	CUSID ASC;

END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for BILL_WARRANT
-- ----------------------------
DROP FUNCTION IF EXISTS `BILL_WARRANT`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` FUNCTION `BILL_WARRANT`(`billid` varchar(45)) RETURNS double
    SQL SECURITY INVOKER
BEGIN
	DECLARE warr DOUBLE(15,4) DEFAULT 0.0;

	SELECT issued_bills.balance_warrant INTO warr FROM issued_bills WHERE issued_bills.bill_id = billid; 

	RETURN warr;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for CP_YEAR_START_BALANCE
-- ----------------------------
DROP FUNCTION IF EXISTS `CP_YEAR_START_BALANCE`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` FUNCTION `CP_YEAR_START_BALANCE`(`custid` varchar(45),`yearnum` int(11)) RETURNS double
    SQL SECURITY INVOKER
BEGIN
	DECLARE balam DOUBLE(10,2) DEFAULT 0.0;
	SELECT annual_process_values.balance_amount INTO balam 
FROM annual_process_values 
WHERE annual_process_values.year_number = yearnum AND 
annual_process_values.customer_id = custid;
	RETURN balam;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for CUSTOMER_BALANCE
-- ----------------------------
DROP FUNCTION IF EXISTS `CUSTOMER_BALANCE`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` FUNCTION `CUSTOMER_BALANCE`(`cust_id` varchar(45)) RETURNS double(15,4)
    SQL SECURITY INVOKER
BEGIN
	DECLARE ret_balance_val DOUBLE(15,4);
	DECLARE bal_amount DOUBLE(15,4);
	DECLARE bal_warrant DOUBLE(15,4);
	DECLARE bal_arrears DOUBLE(15,4);
	DECLARE bal_watercharge DOUBLE(15,4);

	SELECT wb_balance.balance_amount, wb_balance.balance_warrant, 
	wb_balance.balance_arrears, wb_balance.balance_watercharge INTO bal_amount, bal_warrant, bal_arrears, bal_watercharge  
	FROM wb_balance WHERE wb_balance.customer_id = cust_id;

	SET ret_balance_val = bal_amount + bal_warrant;

	RETURN ret_balance_val;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for FIRSTBILLDATE
-- ----------------------------
DROP FUNCTION IF EXISTS `FIRSTBILLDATE`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `FIRSTBILLDATE`(`cus_id` varchar(45),`byear` int(11)) RETURNS date
BEGIN

	DECLARE fbpd DATE;

	SELECT DATE(bill_balance_log.time) INTO fbpd FROM bill_balance_log 
	INNER JOIN issued_bills ON issued_bills.bill_id = bill_balance_log.bill_id 
	WHERE bill_balance_log.customer_id = cus_id AND issued_bills.bill_year = byear ORDER BY bill_balance_log.time ASC LIMIT 1;

	RETURN fbpd;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for getFirstBillDate
-- ----------------------------
DROP FUNCTION IF EXISTS `getFirstBillDate`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `getFirstBillDate`(`cus_id` varchar(45),`byear` int(11)) RETURNS date
BEGIN

	DECLARE fbpd DATE;

	SELECT DATE(bill_balance_log.time) INTO fbpd FROM bill_balance_log 
	INNER JOIN issued_bills ON issued_bills.bill_id = bill_balance_log.bill_id 
	WHERE bill_balance_log.customer_id = cus_id AND issued_bills.bill_year = byear ORDER BY bill_balance_log.time ASC LIMIT 1;

	RETURN fbpd;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for HasBillForMonth
-- ----------------------------
DROP FUNCTION IF EXISTS `HasBillForMonth`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` FUNCTION `HasBillForMonth`(`cus_id` int(11),`bill_year` int(11),`bill_month` int(11)) RETURNS tinyint(1)
    SQL SECURITY INVOKER
BEGIN
	DECLARE billcount INT(11);

	SELECT Count(issued_bills.bill_id) INTO billcount 
	FROM issued_bills 
	WHERE issued_bills.customer_id = cus_id AND issued_bills.bill_year = bill_year AND 
issued_bills.bill_month = bill_month;

RETURN billcount > 0;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for IS_REDBILL_CUSTOMER
-- ----------------------------
DROP FUNCTION IF EXISTS `IS_REDBILL_CUSTOMER`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` FUNCTION `IS_REDBILL_CUSTOMER`(`cu_id` varchar(45)) RETURNS tinyint(1)
    SQL SECURITY INVOKER
BEGIN
	DECLARE l_ret_val TINYINT(1) DEFAULT 0;
	DECLARE l_last_row_fetched INT DEFAULT 0;
	DECLARE l_rb_count INT DEFAULT 0;

	DECLARE t_bill_id VARCHAR(10);
	DECLARE t_bill_status TINYINT(1);

	DECLARE c_redbill CURSOR FOR 
	SELECT issued_bills.bill_id,issued_bills.bill_status 
	FROM issued_bills 
	WHERE issued_bills.customer_id = cu_id 
	ORDER BY issued_bills.bill_date DESC LIMIT 0, 3;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET l_last_row_fetched=1;

	OPEN c_redbill;
	l_rbcurl: LOOP
			FETCH c_redbill INTO t_bill_id, t_bill_status;

			IF t_bill_status != 3 THEN 
				SET l_rb_count = l_rb_count + 1;
			END IF;

			IF l_last_row_fetched=1 THEN
					 LEAVE l_rbcurl;
			END IF;
	END LOOP l_rbcurl;
	CLOSE c_redbill;

	IF l_rb_count > 2 THEN
		SET l_ret_val = 1;
	END IF;

	RETURN 
# l_ret_val;
l_rb_count;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for PROPERTY_VALUE
-- ----------------------------
DROP FUNCTION IF EXISTS `PROPERTY_VALUE`;
DELIMITER ;;
CREATE DEFINER=`mdccuser`@`%` FUNCTION `PROPERTY_VALUE`(`in_prop_type` int, `in_prop_id` int) RETURNS varchar(45) CHARSET utf8
    SQL SECURITY INVOKER
BEGIN
	DECLARE l_prop_val VARCHAR(45) DEFAULT '';

	SELECT properties.prop_value INTO l_prop_val 
	FROM properties 
	WHERE properties.prop_type=in_prop_type AND properties.prop_id=in_prop_id;

	RETURN l_prop_val;
END
;;
DELIMITER ;
