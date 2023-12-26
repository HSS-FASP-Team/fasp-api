INSERT INTO `fasp`.`ap_static_label`(`STATIC_LABEL_ID`,`LABEL_CODE`,`ACTIVE`) VALUES ( NULL,'static.tooltip.conversionfactorARU','1'); 
SELECT MAX(l.STATIC_LABEL_ID) INTO @MAX FROM ap_static_label l ;

INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,1,'How many Alternate Reporting Units are in 1 Planning Unit? e.g. Alternate Reporting Unit = 1 tablet, Planning Unit = bottle of 30 tablets. There are 30 tablets in 1 bottle. There are 30 alternate reporting units in 1 planning unit. Conversion Factor = 30.');-- en
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,2,'Combien d’unités de reporting alternatives y a-t-il dans 1 unité de planification ? par exemple. Unité de reporting alternative = 1 comprimé, Unité de planification = flacon de 30 comprimés. Il y a 30 comprimés dans 1 flacon. Il existe 30 unités de reporting alternatives dans 1 unité de planification. Facteur de conversion = 30.');-- fr
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,3,'¿Cuántas unidades de informes alternativas hay en 1 unidad de planificación? p.ej. Unidad de informe alternativa = 1 tableta, Unidad de planificación = frasco de 30 tabletas. Hay 30 comprimidos en 1 frasco. Hay 30 unidades de informes alternativas en 1 unidad de planificación. Factor de conversión = 30.');-- sp
INSERT INTO ap_static_label_languages VALUES(NULL,@MAX,4,'Quantas unidades de relatórios alternativos existem em 1 unidade de planejamento? por exemplo. Unidade de Relatório Alternativo = 1 comprimido, Unidade de Planejamento = frasco de 30 comprimidos. Existem 30 comprimidos em 1 frasco. Existem 30 unidades de relatórios alternativos em 1 unidade de planejamento. Fator de conversão = 30.');-- pr