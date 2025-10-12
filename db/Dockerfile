FROM mysql/mysql-server:latest
WORKDIR /tmp
COPY world.sql /docker-entrypoint-initdb.d
ENV MYSQL_ROOT_PASSWORD example
#Mac Fix
ENV MYSQL_ROOT_HOST=%
