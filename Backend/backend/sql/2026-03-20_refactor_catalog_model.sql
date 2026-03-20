-- Refactor del catalogo:
-- 1. categories deja de modelar genero + categoria
-- 2. se crea genders
-- 3. products pasa a referenciar category_id + gender_id
--
-- Ejecutar primero en un backup o entorno de staging.

START TRANSACTION;

CREATE TABLE IF NOT EXISTS genders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL,
    active BIT(1) NOT NULL DEFAULT b'1',
    CONSTRAINT pk_genders PRIMARY KEY (id),
    CONSTRAINT uk_genders_name UNIQUE (name)
);

INSERT INTO genders (name, active)
SELECT source.name, source.active
FROM (
    SELECT DISTINCT TRIM(c.name) AS name, c.active
    FROM categories c
    WHERE c.parent_id IS NULL
) source
LEFT JOIN genders g
    ON LOWER(g.name) = LOWER(source.name)
WHERE g.id IS NULL;

ALTER TABLE products
    ADD COLUMN gender_id BIGINT NULL;

DROP TEMPORARY TABLE IF EXISTS tmp_categories_new;
CREATE TEMPORARY TABLE tmp_categories_new (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL,
    active BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id),
    UNIQUE KEY uk_tmp_categories_new_name (name)
);

INSERT INTO tmp_categories_new (name, active)
SELECT source.name, MAX(source.active) AS active
FROM (
    -- categorias hijas del modelo viejo: Hombre -> Jeans, Mujer -> Jeans
    SELECT TRIM(child.name) AS name, child.active
    FROM categories child
    WHERE child.parent_id IS NOT NULL

    UNION ALL

    -- categorias ya planas o productos que quedaron referenciando una root sin hijos
    SELECT TRIM(c.name) AS name, c.active
    FROM categories c
    WHERE c.parent_id IS NULL
      AND NOT EXISTS (
          SELECT 1
          FROM categories child
          WHERE child.parent_id = c.id
      )
      AND LOWER(TRIM(c.name)) NOT IN ('hombre', 'mujer')
) source
GROUP BY source.name;

SET @products_category_fk := (
    SELECT kcu.CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE kcu
    WHERE kcu.TABLE_SCHEMA = DATABASE()
      AND kcu.TABLE_NAME = 'products'
      AND kcu.COLUMN_NAME = 'category_id'
      AND kcu.REFERENCED_TABLE_NAME = 'categories'
    LIMIT 1
);

SET @drop_products_category_fk_sql := IF(
    @products_category_fk IS NOT NULL,
    CONCAT('ALTER TABLE products DROP FOREIGN KEY ', @products_category_fk),
    'SELECT 1'
);
PREPARE stmt_drop_products_category_fk FROM @drop_products_category_fk_sql;
EXECUTE stmt_drop_products_category_fk;
DEALLOCATE PREPARE stmt_drop_products_category_fk;

UPDATE products p
JOIN categories old_category
    ON old_category.id = p.category_id
LEFT JOIN categories old_gender
    ON old_gender.id = old_category.parent_id
JOIN tmp_categories_new new_category
    ON LOWER(new_category.name) = LOWER(TRIM(
        CASE
            WHEN old_category.parent_id IS NULL THEN old_category.name
            ELSE old_category.name
        END
    ))
LEFT JOIN genders mapped_gender
    ON LOWER(mapped_gender.name) = LOWER(TRIM(old_gender.name))
SET p.category_id = new_category.id,
    p.gender_id = mapped_gender.id;

ALTER TABLE products
    MODIFY COLUMN gender_id BIGINT NOT NULL;

DROP TABLE categories;

CREATE TABLE categories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL,
    active BIT(1) NOT NULL DEFAULT b'1',
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uk_categories_name UNIQUE (name)
);

INSERT INTO categories (id, name, active)
SELECT id, name, active
FROM tmp_categories_new
ORDER BY id;

ALTER TABLE products
    ADD CONSTRAINT fk_products_category
        FOREIGN KEY (category_id) REFERENCES categories (id),
    ADD CONSTRAINT fk_products_gender
        FOREIGN KEY (gender_id) REFERENCES genders (id);

CREATE INDEX idx_products_category_id ON products (category_id);
CREATE INDEX idx_products_gender_id ON products (gender_id);
CREATE INDEX idx_categories_active_name ON categories (active, name);
CREATE INDEX idx_genders_active_name ON genders (active, name);

COMMIT;

-- Consultas de verificacion sugeridas:
-- SELECT * FROM genders ORDER BY id;
-- SELECT * FROM categories ORDER BY id;
-- SELECT p.id, p.name, c.name AS category_name, g.name AS gender_name
-- FROM products p
-- JOIN categories c ON c.id = p.category_id
-- JOIN genders g ON g.id = p.gender_id
-- ORDER BY p.id;
