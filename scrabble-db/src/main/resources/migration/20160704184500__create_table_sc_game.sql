DO LANGUAGE plpgsql $$
BEGIN
  BEGIN
    CREATE TABLE IF NOT EXISTS sc_game (
      id serial primary key,
      user_name varchar(50) not null,
      field jsonb,
      score_human int,
      score_machine int
    );
  END;

  BEGIN
    CREATE INDEX ix_sc_game ON sc_game("user_name");
    EXCEPTION
    WHEN duplicate_table
      THEN RAISE NOTICE 'index already exists';
  END;
END;
$$