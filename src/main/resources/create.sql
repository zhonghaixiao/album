create table sys_user(
  user_id bigint primary key,
  user_name varchar(30) not null ,
  phone varchar(30),
  email varchar(30),
  password varchar(100) not null ,
  salt varchar(100) not null default '123456',
  locked char(1) not null default '0' comment '1:锁定',
  create_time datetime default now(),
  update_time datetime default now()
)engine=InnoDB default charset='utf8';

create table role(
  role_id bigint primary key ,
  role_name varchar(30) not null ,
  role_desc varchar(100),
  available char(1) default '1'
)engine=InnoDB default charset='utf8';

create table perm(
  perm_id bigint primary key ,
  perm_name varchar(30) not null ,
  perm_desc varchar(100),
  available char(1) default '1'
)engine=InnoDB default charset='utf8';

create table user_role(
  user_id bigint,
  role_id bigint,
  primary key (user_id, role_id)
)engine=InnoDB default charset='utf8';

create table role_perm(
  role_id bigint,
  perm_id bigint,
  primary key (role_id, perm_id)
)engine=InnoDB default charset='utf8';




