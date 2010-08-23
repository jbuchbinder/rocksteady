
create database rocksteady;
grant all privileges on rocksteady.* to 'rocksteady'@'localhost' identified by 'rocktheworld';
grant all privileges on rocksteady.* to 'rocksteady'@'%' identified by 'rocktheworld';
flush privileges;
