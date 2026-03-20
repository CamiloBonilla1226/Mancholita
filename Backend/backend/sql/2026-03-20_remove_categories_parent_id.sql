-- Limpieza explicita de parent_id en categories.
-- Usar este script si la base ya fue migrada parcialmente
-- o si todavia existe la columna parent_id en el esquema real.

START TRANSACTION;

SET @parent_fk_name := (
    SELECT kcu.CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE kcu
    WHERE kcu.TABLE_SCHEMA = DATABASE()
      AND kcu.TABLE_NAME = 'categories'
      AND kcu.COLUMN_NAME = 'parent_id'
      AND kcu.REFERENCED_TABLE_NAME = 'categories'
    LIMIT 1
);

SET @drop_parent_fk_sql := IF(
    @parent_fk_name IS NOT NULL,
    CONCAT('ALTER TABLE categories DROP FOREIGN KEY ', @parent_fk_name),
    'SELECT 1'
);
PREPARE stmt_drop_parent_fk FROM @drop_parent_fk_sql;
EXECUTE stmt_drop_parent_fk;
DEALLOCATE PREPARE stmt_drop_parent_fk;

SET @drop_parent_index_sql := IF(
    EXISTS (
        SELECT 1
        FROM information_schema.STATISTICS s
        WHERE s.TABLE_SCHEMA = DATABASE()
          AND s.TABLE_NAME = 'categories'
          AND s.COLUMN_NAME = 'parent_id'
          AND s.INDEX_NAME <> 'PRIMARY'
    ),
    (
        SELECT CONCAT('ALTER TABLE categories DROP INDEX ', s.INDEX_NAME)
        FROM information_schema.STATISTICS s
        WHERE s.TABLE_SCHEMA = DATABASE()
          AND s.TABLE_NAME = 'categories'
          AND s.COLUMN_NAME = 'parent_id'
          AND s.INDEX_NAME <> 'PRIMARY'
        LIMIT 1
    ),
    'SELECT 1'
);
PREPARE stmt_drop_parent_index FROM @drop_parent_index_sql;
EXECUTE stmt_drop_parent_index;
DEALLOCATE PREPARE stmt_drop_parent_index;

SET @drop_parent_column_sql := IF(
    EXISTS (
        SELECT 1
        FROM information_schema.COLUMNS c
        WHERE c.TABLE_SCHEMA = DATABASE()
          AND c.TABLE_NAME = 'categories'
          AND c.COLUMN_NAME = 'parent_id'
    ),
    'ALTER TABLE categories DROP COLUMN parent_id',
    'SELECT 1'
);
PREPARE stmt_drop_parent_column FROM @drop_parent_column_sql;
EXECUTE stmt_drop_parent_column;
DEALLOCATE PREPARE stmt_drop_parent_column;

COMMIT;

-- Verificacion:
-- SELECT COLUMN_NAME
-- FROM information_schema.COLUMNS
-- WHERE TABLE_SCHEMA = DATABASE()
--   AND TABLE_NAME = 'categories';
