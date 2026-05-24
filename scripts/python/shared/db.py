import psycopg2
from contextlib import contextmanager
from shared.env_loader import DatabaseConfig

_config = DatabaseConfig()

@contextmanager
def get_connection():
    conn = psycopg2.connect(_config.url)
    try:
        yield conn
        conn.commit()
    except Exception:
        conn.rollback()
        raise
    finally:
        conn.close()

@contextmanager
def get_cursor():
    with get_connection() as conn:
        cursor = conn.cursor()
        try:
            yield cursor
        finally:
            cursor.close()