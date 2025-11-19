-- Create separate databases for each service
CREATE DATABASE authdb;
CREATE DATABASE tmdbdb;
CREATE DATABASE userdb;

-- Create users for each service with passwords
CREATE USER authdbuser WITH PASSWORD 'authdbpass';
CREATE USER tmdbdbuser WITH PASSWORD 'tmdbdbpass';
CREATE USER userdbuser WITH PASSWORD 'userdbpass';

-- Grant privileges on databases to respective users
GRANT ALL PRIVILEGES ON DATABASE authdb TO authdbuser;
GRANT ALL PRIVILEGES ON DATABASE tmdbdb TO tmdbdbuser;
GRANT ALL PRIVILEGES ON DATABASE userdb TO userdbuser;

-- Grant schema privileges for authdb
\c authdb
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO authdbuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO authdbuser;
GRANT ALL ON SCHEMA public TO authdbuser;

-- Grant schema privileges for tmdbdb
\c tmdbdb
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO tmdbdbuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO tmdbdbuser;
GRANT ALL ON SCHEMA public TO tmdbdbuser;

-- Grant schema privileges for userdb
\c userdb
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO userdbuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO userdbuser;
GRANT ALL ON SCHEMA public TO userdbuser;