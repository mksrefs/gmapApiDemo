# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table buy_property (
  property_id               bigint(20) auto_increment not null,
  property_cd               varchar(15) not null,
  property_name             varchar(100),
  property_world_latitude   double,
  property_world_longitude  double,
  constraint uq_buy_property_property_cd unique (property_cd),
  constraint pk_buy_property primary key (property_id))
;

create table buy_property2 (
  property_id               bigint(20) auto_increment not null,
  property_cd               varchar(15) not null,
  property_name             varchar(100),
  property_world_latitude   double,
  property_world_longitude  double,
  constraint uq_buy_property2_property_cd unique (property_cd),
  constraint pk_buy_property2 primary key (property_id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table buy_property;

drop table buy_property2;

SET FOREIGN_KEY_CHECKS=1;

