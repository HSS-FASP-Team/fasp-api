ALTER TABLE `fasp`.`rm_forecast_actual_consumption` ADD COLUMN `PU_AMOUNT` DECIMAL(16,4) UNSIGNED NULL AFTER `ADJUSTED_AMOUNT`, CHANGE COLUMN `EXCLUDE` `ADJUSTED_AMOUNT` DECIMAL(16,4) UNSIGNED NULL;

