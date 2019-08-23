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
)engine=InnoDB default charset='utf-8'
