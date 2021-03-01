-- 1.查看所有数据库容量大小
SELECT
    table_schema AS '数据库',
    sum(table_rows) AS '记录数',
    sum(truncate(data_length / 1024 / 1024, 2)) AS '数据容量(MB)',
    sum(truncate(index_length / 1024 / 1024, 2)) AS '索引容量(MB)'
FROM
    information_schema.tables
GROUP BY
    table_schema
ORDER BY
    sum(data_length)
    DESC,
    sum(index_length)
    DESC;

-- 2.查看所有数据库各表容量大小
SELECT
    table_schema AS '数据库',
    table_name AS '表名',
    table_rows AS '记录数',
    truncate(data_length / 1024 / 1024, 2) AS '数据容量(MB)',
    truncate(index_length / 1024 / 1024, 2) AS '索引容量(MB)'
FROM
    information_schema.tables
ORDER BY
    data_length DESC,
    index_length DESC;

-- 3.查看指定数据库容量大小
SELECT
    table_schema AS '数据库',
    sum(table_rows) AS '记录数',
    sum(truncate(data_length / 1024 / 1024, 2)) AS '数据容量(MB)',
    sum(truncate(index_length / 1024 / 1024, 2)) AS '索引容量(MB)'
FROM
    information_schema.tables
WHERE
        table_schema = 'mysql';

-- 4.查看指定数据库各表容量大小
SELECT
    table_schema AS '数据库',
    table_name AS '表名',
    table_rows AS '记录数',
    truncate(data_length / 1024 / 1024, 2) AS '数据容量(MB)',
    truncate(index_length / 1024 / 1024, 2) AS '索引容量(MB)'
FROM
    information_schema.tables
WHERE
        table_schema = 'ipharmacare_knowledge'
ORDER BY
    data_length DESC,
    index_length DESC;

SELECT
    concat(round(sum(data_length / 1024 / 1024), 2), 'MB') AS data_length_MB,
    concat(round(sum(index_length / 1024 / 1024), 2), 'MB') AS index_length_MB
FROM
    information_schema.tables
WHERE
    table_schema = 'ipharmacare_knowledge'
    AND table_name = 'tb_product_property';