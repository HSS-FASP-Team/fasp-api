/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  altius
 * Created: 26-Apr-2022
 */

INSERT INTO fasp.ap_static_label(STATIC_LABEL_ID,LABEL_CODE,ACTIVE) VALUES ( NULL,'static.report.versionFinal*','1');
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Version (* indicates Final)');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Version (* indique Finale)');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Versión (* indica Final)');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Versão (* indica Final)');-- pr

INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.common.person','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Person');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Personne');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Persona');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Pessoa');-- pr

INSERT INTO fasp.ap_static_label(STATIC_LABEL_ID,LABEL_CODE,ACTIVE) VALUES ( NULL,'static.importIntoQATSupplyPlan.forecastFinalVersion','1');
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Forecast version (Final Versions Only)');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Version prévisionnelle (versions finales uniquement)');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Versão de previsão (somente versões finais)');-- pr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Versión de previsión (solo versiones finales)');-- sp

INSERT INTO fasp.ap_static_label(STATIC_LABEL_ID,LABEL_CODE,ACTIVE) VALUES ( NULL,'static.importIntoQATSupplyPlan.importIntoQATSupplyPlan','1');
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'QAT Forecast Import');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Importation des prévisions QAT');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Importación de pronóstico QAT');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Dados do plano de fornecimento importados com sucesso');-- pr


update ap_static_label l 
left join ap_static_label_languages ll on l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
set ll.LABEL_TEXT='Compare With Version (* indicates Final)'
where l.LABEL_CODE='static.compareVersion.compareWithVersion' and ll.LANGUAGE_ID=1;

update ap_static_label l 
left join ap_static_label_languages ll on l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
set ll.LABEL_TEXT='Comparer avec la version (* indique la version finale)'
where l.LABEL_CODE='static.compareVersion.compareWithVersion' and ll.LANGUAGE_ID=2;

update ap_static_label l 
left join ap_static_label_languages ll on l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
set ll.LABEL_TEXT='Comparar con la versión (* indica final)'
where l.LABEL_CODE='static.compareVersion.compareWithVersion' and ll.LANGUAGE_ID=3;

update ap_static_label l 
left join ap_static_label_languages ll on l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
set ll.LABEL_TEXT='Comparar com a versão (* indica Final)'
where l.LABEL_CODE='static.compareVersion.compareWithVersion' and ll.LANGUAGE_ID=4;

ALTER TABLE `fasp`.`rm_forecast_tree_node_data` ADD COLUMN `IS_EXTRAPOLATION` TINYINT(1) UNSIGNED NOT NULL AFTER `NODE_DATA_PU_ID`;
UPDATE rm_forecast_tree_node_data ftnd LEFT JOIN rm_forecast_tree_node ftn ON ftnd.NODE_ID=ftn.NODE_ID SET ftnd.IS_EXTRAPOLATION=ftn.IS_EXTRAPOLATION;
ALTER TABLE `fasp`.`rm_forecast_tree_node` DROP COLUMN `IS_EXTRAPOLATION`;

USE `fasp`;
CREATE 
     OR REPLACE ALGORITHM = UNDEFINED 
    DEFINER = `faspUser`@`%` 
    SQL SECURITY DEFINER
VIEW `vw_forecast_tree_node` AS
    SELECT 
        `tn`.`NODE_ID` AS `NODE_ID`,
        `tn`.`TREE_ID` AS `TREE_ID`,
        `tn`.`PARENT_NODE_ID` AS `PARENT_NODE_ID`,
        `tn`.`SORT_ORDER` AS `SORT_ORDER`,
        `tn`.`LEVEL_NO` AS `LEVEL_NO`,
        `tn`.`NODE_TYPE_ID` AS `NODE_TYPE_ID`,
        `tn`.`UNIT_ID` AS `UNIT_ID`,
        `tn`.`LABEL_ID` AS `LABEL_ID`,
        `tn`.`CREATED_BY` AS `CREATED_BY`,
        `tn`.`CREATED_DATE` AS `CREATED_DATE`,
        `tn`.`LAST_MODIFIED_BY` AS `LAST_MODIFIED_BY`,
        `tn`.`LAST_MODIFIED_DATE` AS `LAST_MODIFIED_DATE`,
        `tn`.`ACTIVE` AS `ACTIVE`,
        `l`.`LABEL_EN` AS `LABEL_EN`,
        `l`.`LABEL_FR` AS `LABEL_FR`,
        `l`.`LABEL_SP` AS `LABEL_SP`,
        `l`.`LABEL_PR` AS `LABEL_PR`
    FROM
        (`rm_forecast_tree_node` `tn`
        LEFT JOIN `ap_label` `l` ON ((`tn`.`LABEL_ID` = `l`.`LABEL_ID`)));


update ap_static_label l 
left join ap_static_label_languages ll on l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
set ll.LABEL_TEXT='NOTE:  The minimum values needed to get correct graphs and reports for the various features are below:\n* TES, Holt-Winters:  Needs at least 24 months of actual consumption data\n* ARIMA:  Needs at least 14 months of actual consumption data\n* Moving Average, Semi-Averages, and Linear Regression:  Needs at least 3 months of actual consumption data'
where l.LABEL_CODE='static.tree.minDataRequiredToExtrapolate' and ll.LANGUAGE_ID=1;

update ap_static_label l 
left join ap_static_label_languages ll on l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
set ll.LABEL_TEXT='REMARQUE : Les valeurs minimales nécessaires pour obtenir des graphiques et des rapports corrects pour les différentes fonctionnalités sont les suivantes :\n* TES, Holt-Winters : nécessite au moins 24 mois de données de consommation réelle\n* ARIMA : nécessite au moins 14 mois de données de consommation réelle\n* Moyenne mobile, semi-moyennes et régression linéaire : nécessite au moins 3 mois de données de consommation réelle'
where l.LABEL_CODE='static.tree.minDataRequiredToExtrapolate' and ll.LANGUAGE_ID=2;

update ap_static_label l 
left join ap_static_label_languages ll on l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
set ll.LABEL_TEXT='NOTA: Los valores mínimos necesarios para obtener gráficos e informes correctos para las diversas funciones se encuentran a continuación:\n* TES, Holt-Winters: necesita al menos 24 meses de datos de consumo real\n* ARIMA: necesita al menos 14 meses de datos de consumo real\n* Promedio móvil, semipromedios y regresión lineal: necesita al menos 3 meses de datos de consumo real'
where l.LABEL_CODE='static.tree.minDataRequiredToExtrapolate' and ll.LANGUAGE_ID=3;

update ap_static_label l 
left join ap_static_label_languages ll on l.STATIC_LABEL_ID=ll.STATIC_LABEL_ID
set ll.LABEL_TEXT='OBSERVAÇÃO: os valores mínimos necessários para obter gráficos e relatórios corretos para os vários recursos estão abaixo:\n* TES, Holt-Winters: precisa de pelo menos 24 meses de dados de consumo reais\n* ARIMA: precisa de pelo menos 14 meses de dados de consumo reais\n* Média Móvel, Semi-Médias e Regressão Linear: Precisa de pelo menos 3 meses de dados de consumo real'
where l.LABEL_CODE='static.tree.minDataRequiredToExtrapolate' and ll.LANGUAGE_ID=4;

UPDATE `fasp`.`ap_static_label_languages` SET `LABEL_TEXT`='Whereas a moving average weighs each data point equally, exponential smoothing uses older data at exponentially decreasing weights over time. Triple exponential smoothing applies smoothing to the level (alpha), trend (beta), and seasonality (gamma) - parameters are set between 0 and 1, with values close to 1 favoring recent values and values close to 0 favoring past values.\r\nConfidence interval:  between 0% and 100% (exclusive) e.g. 90% confidence level indicates 90% of future points are to fall within this radius from prediction.\r\nAlpha (level), beta (trend), gamma (seasonality): between 0 and 1, with values close to 1 favoring recent values and values close to 0 favoring past values. \r\nSee \'Show Guidance\' for more.\r\n' WHERE `STATIC_LABEL_LANGUAGE_ID`='200570';

UPDATE `fasp`.`ap_static_label_languages` SET `LABEL_TEXT`='Alors qu\'une moyenne mobile pèse chaque point de données de manière égale, le lissage exponentiel utilise des données plus anciennes à des poids décroissants de manière exponentielle au fil du temps. Le triple lissage exponentiel applique le lissage au niveau (alpha), à la tendance (bêta) et à la saisonnalité (gamma) - les paramètres sont définis entre 0 et 1, les valeurs proches de 1 favorisant les valeurs récentes et les valeurs proches de 0 favorisant les valeurs passées.\r\nIntervalle de confiance : entre 0% et 100% (exclusif) ex. Un niveau de confiance de 90 % indique que 90 % des points futurs doivent se situer dans ce rayon de prédiction.\r\nAlpha (niveau), beta (tendance), gamma (saisonnalité) : entre 0 et 1, avec des valeurs proches de 1 favorisant les valeurs récentes et des valeurs proches de 0 favorisant les valeurs passées.\r\nVoir \'Afficher le guidage\' pour plus d\'informations.\r\n' WHERE `STATIC_LABEL_LANGUAGE_ID`='200571';

UPDATE `fasp`.`ap_static_label_languages` SET `LABEL_TEXT`='Mientras que un promedio móvil pondera cada punto de datos por igual, el suavizado exponencial utiliza datos más antiguos con ponderaciones que disminuyen exponencialmente a lo largo del tiempo. El suavizado exponencial triple aplica suavizado al nivel (alfa), la tendencia (beta) y la estacionalidad (gamma): los parámetros se establecen entre 0 y 1, con valores cercanos a 1 que favorecen los valores recientes y valores cercanos a 0 que favorecen los valores pasados.\r\nIntervalo de confianza: entre 0% y 100% (exclusivo) ej. El nivel de confianza del 90% indica que el 90% de los puntos futuros caerán dentro de este radio de predicción.\r\nAlfa (nivel), beta (tendencia), gamma (estacionalidad): entre 0 y 1, con valores cercanos a 1 favoreciendo valores recientes y valores cercanos a 0 favoreciendo valores pasados.\r\nConsulte \'Mostrar orientación\' para obtener más información.' WHERE `STATIC_LABEL_LANGUAGE_ID`='200572';

UPDATE `fasp`.`ap_static_label_languages` SET `LABEL_TEXT`='Enquanto uma média móvel pesa cada ponto de dados igualmente, a suavização exponencial usa dados mais antigos com pesos exponencialmente decrescentes ao longo do tempo. A suavização exponencial tripla aplica a suavização ao nível (alfa), tendência (beta) e sazonalidade (gama) - os parâmetros são definidos entre 0 e 1, com valores próximos de 1 favorecendo valores recentes e valores próximos de 0 favorecendo valores passados.\r\nIntervalo de confiança: entre 0% e 100% (exclusivo) ex. O nível de confiança de 90% indica que 90% dos pontos futuros devem cair dentro desse raio de previsão.\r\nAlfa (nível), beta (tendência), gama (sazonalidade): entre 0 e 1, com valores próximos de 1 favorecendo valores recentes e valores próximos de 0 favorecendo valores passados.\r\nConsulte \'Mostrar Orientação\' para mais informações.' WHERE `STATIC_LABEL_LANGUAGE_ID`='200573';


INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.tree.calculatedChangeForMonthTree','1');
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'Calculated change for');
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Variation calculée pour le');
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'Cambio calculado por');
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Alteração calculada para o');

USE `fasp`;
DROP procedure IF EXISTS `getForecastSummary`;

USE `fasp`;
DROP procedure IF EXISTS `fasp`.`getForecastSummary`;
;

DELIMITER $$
USE `fasp`$$
CREATE DEFINER=`faspUser`@`%` PROCEDURE `getForecastSummary`(VAR_PROGRAM_ID INT(10), VAR_VERSION_ID INT(10))
BEGIN

    -- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    -- Mod 2 Report no 2 - ForecastSummary
    -- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    -- programId must be a single Program
    -- versionId must be the actual version 

    SET @programId = VAR_PROGRAM_ID;
    SET @versionId = VAR_VERSION_ID;
    
    SELECT 
	pu.PLANNING_UNIT_ID, pu.LABEL_ID `PU_LABEL_ID`, pu.LABEL_EN `PU_LABEL_EN`, pu.LABEL_FR `PU_LABEL_FR`, pu.LABEL_SP `PU_LABEL_SP`, pu.LABEL_PR `PU_LABEL_PR`, pu.MULTIPLIER,
        fu.FORECASTING_UNIT_ID, fu.LABEL_ID `FU_LABEL_ID`, fu.LABEL_EN `FU_LABEL_EN`, fu.LABEL_FR `FU_LABEL_FR`, fu.LABEL_SP `FU_LABEL_SP`, fu.LABEL_PR `FU_LABEL_PR`,
        tc.TRACER_CATEGORY_ID, tc.LABEL_ID `TC_LABEL_ID`, tc.LABEL_EN `TC_LABEL_EN`, tc.LABEL_FR `TC_LABEL_FR`, tc.LABEL_SP `TC_LABEL_SP`, tc.LABEL_PR `TC_LABEL_PR`,
	r.REGION_ID, r.LABEL_ID `R_LABEL_ID`, r.LABEL_EN `R_LABEL_EN`, r.LABEL_FR `R_LABEL_FR`, r.LABEL_SP `R_LABEL_SP`, r.LABEL_PR `R_LABEL_PR`,
	COALESCE(t.LABEL_ID, em.LABEL_ID) `SF_LABEL_ID`, COALESCE(CONCAT(t.LABEL_EN,'-',s.LABEL_EN), em.LABEL_EN) `SF_LABEL_EN`, COALESCE(CONCAT(t.LABEL_FR,'-',s.LABEL_FR), em.LABEL_FR) `SF_LABEL_FR`, COALESCE(CONCAT(t.LABEL_SP,'-',s.LABEL_SP), em.LABEL_SP) `SF_LABEL_SP`, COALESCE(CONCAT(t.LABEL_PR,'-',s.LABEL_PR), em.LABEL_PR) `SF_LABEL_PR`,
        dpus.TOTAL_FORECAST, dpus.NOTES, pv.FREIGHT_PERC,
        dpu.STOCK, dpu.EXISTING_SHIPMENTS, dpu.MONTHS_OF_STOCK, dpu.PRICE,
        pa.PROCUREMENT_AGENT_ID, pa.LABEL_ID `PA_LABEL_ID`, pa.LABEL_EN `PA_LABEL_EN`, pa.LABEL_FR `PA_LABEL_FR`, pa.LABEL_SP `PA_LABEL_SP`, pa.LABEL_PR `PA_LABEL_PR`, pa.PROCUREMENT_AGENT_CODE
    FROM rm_dataset_planning_unit dpu 
    LEFT JOIN rm_program_version pv ON dpu.PROGRAM_ID=pv.PROGRAM_ID AND dpu.VERSION_ID=pv.VERSION_ID
    LEFT JOIN rm_program_region pr ON pr.PROGRAM_ID=dpu.PROGRAM_ID
    LEFT JOIN rm_dataset_planning_unit_selected dpus ON dpu.PROGRAM_PLANNING_UNIT_ID=dpus.PROGRAM_PLANNING_UNIT_ID AND pr.REGION_ID=dpus.REGION_ID
    LEFT JOIN vw_planning_unit pu ON dpu.PLANNING_UNIT_ID=pu.PLANNING_UNIT_ID 
    LEFT JOIN vw_forecasting_unit fu ON pu.FORECASTING_UNIT_ID=fu.FORECASTING_UNIT_ID
    LEFT JOIN vw_tracer_category tc ON fu.TRACER_CATEGORY_ID=tc.TRACER_CATEGORY_ID
    LEFT JOIN vw_region r ON pr.REGION_ID=r.REGION_ID
    LEFT JOIN vw_forecast_tree t ON dpus.TREE_ID=t.TREE_ID
    LEFT JOIN vw_scenario s ON dpus.SCENARIO_ID=s.SCENARIO_ID
    LEFT JOIN rm_forecast_consumption_extrapolation fce ON dpus.CONSUMPTION_EXTRAPOLATION_ID=fce.CONSUMPTION_EXTRAPOLATION_ID
    LEFT JOIN vw_extrapolation_method em on fce.EXTRAPOLATION_METHOD_ID=em.EXTRAPOLATION_METHOD_ID
    LEFT JOIN vw_procurement_agent pa ON dpu.PROCUREMENT_AGENT_ID=pa.PROCUREMENT_AGENT_ID
    WHERE dpu.PROGRAM_ID=@programId and dpu.VERSION_ID=@versionId
    GROUP BY pr.REGION_ID, dpu.PLANNING_UNIT_ID;
END$$

DELIMITER ;

