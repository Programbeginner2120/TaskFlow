import os
from dotenv import load_dotenv, find_dotenv

load_dotenv(find_dotenv())

def _require(key: str) -> str:
    val = os.getenv(key)
    if val is None:
        raise EnvironmentError(f"Missing required env var: {key}")
    return val

class BaseConfig:
    def __init__(self):
        self.load()

    def load(self):
        raise NotImplementedError

class DatabaseConfig(BaseConfig):
    def __init__(self):
        super().__init__()

    def load(self):
        self.host     = os.getenv("POSTGRES_HOST", "localhost")
        self.port     = os.getenv("POSTGRES_PORT", "5432")
        self.name     = os.getenv("POSTGRES_DB", "taskflow")
        self.user     = os.getenv("POSTGRES_USER", "taskflow")
        self.password = _require("POSTGRES_PASSWORD")
        self.url      = f"postgresql://{self.user}:{self.password}@{self.host}:{self.port}/{self.name}"

