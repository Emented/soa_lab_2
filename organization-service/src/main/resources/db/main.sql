create table if not exists organization
(
	id bigserial primary key,
	name varchar(255) not null,
	x_coordinate bigint not null,
	y_coordinate double precision not null,
	annual_turnover int,
	type varchar(64),
	official_address_zip varchar(6),
	created_at timestamptz not null
);

create table if not exists employee
(
	id bigserial primary key,
	name varchar(255) not null,
	organization_id bigint references organization (id)
);
