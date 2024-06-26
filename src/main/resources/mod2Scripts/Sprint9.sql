/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  altius
 * Created: 11-Apr-2022
 */

INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.extrapolation.errorOccured','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'An error occurred while trying to calculate extrapolation');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Une erreur s`est produite lors de la tentative de calcul de l`extrapolation');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Se produjo un error al intentar calcular la extrapolación');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Ocorreu um erro ao tentar calcular a extrapolação');-- pr

INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.consumptionDataEntryAndAdjustment.nothingToInterpolate','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'There is nothing to interpolate');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Il n`y a rien à interpoler');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'No hay nada que interpolar');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Não há nada para interpolar');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.consumptionDataEntryAndAdjustment.interpolatedDataFor','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Interpolated data for: ');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Données interpolées pour: ');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Datos interpolados para: ');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Dados interpolados para: ');-- pr

INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.extrpolation.graphTitlePart1','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Forecast for ');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Prévisions pour ');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Pronóstico para ');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Previsão para ');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.extrpolation.graphTitlePart2','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,' in ');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,' dans ');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,' en ');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,' dentro ');-- pr

INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.extrapolations.showFits','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Show Fits');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Afficher les coupes');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Mostrar ajustes');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Mostrar ajustes');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.extrapolation.missingDataNotePart1','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'You have months with no actual consumption data, which will cause unexpected results in the extrapolation. Return to the ');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Vous avez des mois sans données de consommation réelle, ce qui entraînera des résultats inattendus dans l`extrapolation. Retour à la ');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Tiene meses sin datos de consumo real, lo que provocará resultados inesperados en la extrapolación. Volver a la ');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Você tem meses sem dados reais de consumo, o que causará resultados inesperados na extrapolação. Retorne para ');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.extrapolation.missingDataNotePart2','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'screen to fill in or interpolate the missing data.');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'écran pour compléter ou interpoler les données manquantes.');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'pantalla para completar o interpolar los datos faltantes.');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'tela para preencher ou interpolar os dados ausentes.');-- pr

UPDATE ap_static_label l 
LEFT JOIN ap_static_label_languages ll ON l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
SET ll.LABEL_TEXT='Transfer'
WHERE l.LABEL_CODE='static.tree.transferToNode' AND ll.LANGUAGE_ID=1;

UPDATE ap_static_label l 
LEFT JOIN ap_static_label_languages ll ON l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
SET ll.LABEL_TEXT='Transférer'
WHERE l.LABEL_CODE='static.tree.transferToNode' AND ll.LANGUAGE_ID=2;

UPDATE ap_static_label l 
LEFT JOIN ap_static_label_languages ll ON l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
SET ll.LABEL_TEXT='Transferir'
WHERE l.LABEL_CODE='static.tree.transferToNode' AND ll.LANGUAGE_ID=3;

UPDATE ap_static_label l 
LEFT JOIN ap_static_label_languages ll ON l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
SET ll.LABEL_TEXT='Transferir'
WHERE l.LABEL_CODE='static.tree.transferToNode' AND ll.LANGUAGE_ID=4;

INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.validation.includeOnlySelectedForecast','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Include only Selected Forecasts');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Inclure uniquement les prévisions sélectionnées');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Incluir solo pronósticos seleccionados');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Incluir apenas previsões selecionadas');-- pr

ALTER TABLE `fasp`.`rm_forecast_actual_consumption` ADD COLUMN `PU_AMOUNT` DECIMAL(16,4) UNSIGNED NULL AFTER `ADJUSTED_AMOUNT`, CHANGE COLUMN `EXCLUDE` `ADJUSTED_AMOUNT` DECIMAL(16,4) UNSIGNED NULL;

INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.dataentry.maxRange','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Please select date range within 36 months');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Veuillez sélectionner une plage de dates dans les 36 mois');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Seleccione el rango de fechas dentro de los 36 meses');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Selecione o intervalo de datas dentro de 36 meses');-- pr

UPDATE ap_static_label l 
LEFT JOIN ap_static_label_languages ll ON l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
SET ll.LABEL_TEXT='Please enter a valid number having max 3 digits before decimal and max 4 digit after decimal.'
WHERE l.LABEL_CODE='static.tree.decimalValidation10&2' AND ll.LANGUAGE_ID=1;

UPDATE ap_static_label l 
LEFT JOIN ap_static_label_languages ll ON l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
SET ll.LABEL_TEXT='Veuillez saisir un nombre valide comportant au maximum 3 chiffres avant la virgule et au maximum 4 chiffres après la virgule.'
WHERE l.LABEL_CODE='static.tree.decimalValidation10&2' AND ll.LANGUAGE_ID=2;

UPDATE ap_static_label l 
LEFT JOIN ap_static_label_languages ll ON l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
SET ll.LABEL_TEXT='Ingrese un número válido que tenga un máximo de 3 dígitos antes del decimal y un máximo de 4 dígitos después del decimal.'
WHERE l.LABEL_CODE='static.tree.decimalValidation10&2' AND ll.LANGUAGE_ID=3;

UPDATE ap_static_label l 
LEFT JOIN ap_static_label_languages ll ON l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
SET ll.LABEL_TEXT='Insira um número válido com no máximo 3 dígitos antes do decimal e no máximo 4 dígitos após o decimal.'
WHERE l.LABEL_CODE='static.tree.decimalValidation10&2' AND ll.LANGUAGE_ID=4;

UPDATE `fasp`.`ap_label` SET `LABEL_EN`='Patient' WHERE `LABEL_ID`='35260'; 
UPDATE `fasp`.`ap_label` SET `LABEL_EN`='Client' WHERE `LABEL_ID`='35261'; 
UPDATE `fasp`.`ap_label` SET `LABEL_EN`='Customer' WHERE `LABEL_ID`='35262'; 

INSERT INTO ap_label VALUES (null, 'ARIMA Low Confidence', null, null, null, 1, now(), 1, now(), 52);
INSERT INTO ap_extrapolation_method VALUES (null, last_insert_id(), 0, 1, now(), 1, now()); 
INSERT INTO ap_label VALUES (null, 'ARIMA High Confidence', null, null, null, 1, now(), 1, now(), 52);
INSERT INTO ap_extrapolation_method VALUES (null, last_insert_id(), 0, 1, now(), 1, now());
INSERT INTO ap_label VALUES (null, 'Linear Regression Low Confidence', null, null, null, 1, now(), 1, now(), 52);
INSERT INTO ap_extrapolation_method VALUES (null, last_insert_id(), 0, 1, now(), 1, now());
INSERT INTO ap_label VALUES (null, 'Linear Regression High Confidence', null, null, null, 1, now(), 1, now(), 52);
INSERT INTO ap_extrapolation_method VALUES (null, last_insert_id(), 0, 1, now(), 1, now());

INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.extrapolation.arimaLower','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Arima (Lower Confidence Bound)');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Arima (limite de confiance inférieure)');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Arima (límite de confianza inferior)');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Arima (Limite de Confiança Inferior)');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.extrapolation.arimaUpper','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Arima (Upper Confidence Bound)');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Arima (limite de confiance supérieure)');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Arima (límite superior de confianza)');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Arima (Limite de Confiança Superior)');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.extrapolation.lrLower','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Linear Regression (Lower Confidence Bound)');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Régression linéaire (limite de confiance inférieure)');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Regresión lineal (límite de confianza inferior)');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Regressão linear (limite de confiança inferior)');-- pr
INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.extrapolation.lrUpper','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Linear Regression (Upper Confidence Bound)');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Régression linéaire (limite de confiance supérieure)');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Regresión lineal (límite de confianza superior)');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Regressão linear (limite de confiança superior)');-- pr
