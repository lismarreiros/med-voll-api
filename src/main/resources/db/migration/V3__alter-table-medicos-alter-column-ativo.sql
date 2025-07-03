ALTER TABLE medicos
ALTER COLUMN ativo TYPE BOOLEAN
USING CASE
         WHEN ativo = 1 THEN TRUE
         WHEN ativo = 0 THEN FALSE
         ELSE NULL  -- ou FALSE, dependendo da lógica
      END;


