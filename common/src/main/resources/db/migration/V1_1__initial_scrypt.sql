create table if not exists public.industries
(
    id            bigserial
        primary key,
    industry_name varchar(100)          not null
        constraint industries_industry_key
            unique,
    created       timestamp(6)          not null,
    changed       timestamp(6)          not null,
    is_deleted    boolean default false not null,
    is_visible    boolean default true  not null
);

alter table public.industries
    owner to dev;

create unique index if not exists industries_industry_uindex
    on public.industries (industry_name);

create table if not exists public.professions
(
    id              bigserial
        primary key,
    profession_name varchar(100)          not null
        constraint professions_profession_key
            unique,
    created         timestamp(6)          not null,
    changed         timestamp(6)          not null,
    is_deleted      boolean default false not null,
    is_visible      boolean default true  not null
);

alter table public.professions
    owner to dev;

create unique index if not exists professions_profession_uindex
    on public.professions (profession_name);

create table if not exists public.specializations
(
    id                  bigserial
        primary key,
    specialization_name varchar(200)          not null
        constraint specializations_specialization_key
            unique,
    created             timestamp(6)          not null,
    changed             timestamp(6)          not null,
    is_deleted          boolean default false not null,
    is_visible          boolean default true  not null
);

alter table public.specializations
    owner to dev;

create unique index if not exists specializations_specialization_uindex
    on public.specializations (specialization_name);

create table if not exists public.positions
(
    id            bigserial
        primary key,
    position_name varchar(200)          not null
        constraint positions_position_key
            unique,
    created       timestamp(6)          not null,
    changed       timestamp(6)          not null,
    is_deleted    boolean default false not null,
    is_visible    boolean default true  not null
);

alter table public.positions
    owner to dev;

create unique index if not exists positions_position_uindex
    on public.positions (position_name);

create table if not exists public.roles
(
    id         bigserial
        primary key,
    role_name  varchar(100)          not null
        constraint roles_role_key
            unique,
    created    timestamp(6)          not null,
    changed    timestamp(6)          not null,
    is_deleted boolean default false not null,
    is_visible boolean default true  not null
);

alter table public.roles
    owner to dev;

create unique index if not exists roles_role_uindex
    on public.roles (role_name);

create table if not exists public.locations
(
    id             bigserial
        primary key,
    global_region  varchar(50) default 'any'::character varying not null,
    country        varchar(50) default 'any'::character varying not null,
    local_region   varchar(50) default 'any'::character varying not null,
    city           varchar(50) default 'any'::character varying not null,
    is_countryside boolean     default false                    not null,
    created        timestamp(6)                                 not null,
    changed        timestamp(6)                                 not null,
    is_deleted     boolean     default false                    not null,
    is_visible     boolean     default true                     not null
);

alter table public.locations
    owner to dev;

create index if not exists locations_city_index
    on public.locations (city);

create index if not exists locations_country_index
    on public.locations (country);

create index if not exists locations_global_region_index
    on public.locations (global_region);

create index if not exists locations_local_region_index
    on public.locations (local_region);

create unique index if not exists locations_global_region_country_local_region_city_uindex
    on public.locations (global_region, country, local_region, city, is_countryside);

create table if not exists public.users
(
    id         bigserial
        primary key,
    email      varchar(360)          not null
        unique,
    password   varchar(50)           not null,
    created    timestamp(6)          not null,
    changed    timestamp(6)          not null,
    is_deleted boolean default false not null,
    is_visible boolean default false not null,
    is_locked  boolean default false not null,
    is_expired boolean default false not null,
    nick_name  varchar(100)          not null
);

alter table public.users
    owner to dev;

create index if not exists users_created_index
    on public.users (created);

create unique index if not exists users_email_password_uindex
    on public.users (email, password);

create unique index if not exists users_email_uindex
    on public.users (email);

create index if not exists users_password_index
    on public.users (password);

create index if not exists users_nick_name_index
    on public.users (nick_name);

create table if not exists public.l_users_roles
(
    id         bigserial
        primary key,
    user_id    bigint                not null
        constraint l_users_roles_users_id_fk
            references public.users
            on update cascade on delete cascade,
    role_id    bigint                not null
        constraint l_users_roles_roles_id_fk
            references public.roles
            on update cascade on delete cascade,
    is_deleted boolean default false not null,
    created    timestamp(6)          not null,
    changed    timestamp(6)          not null
);

alter table public.l_users_roles
    owner to dev;

create index if not exists l_users_roles_role_id_index
    on public.l_users_roles (role_id);

create index if not exists l_users_roles_user_id_index
    on public.l_users_roles (user_id);

create unique index if not exists l_users_roles_user_id_role_id_uindex
    on public.l_users_roles (user_id, role_id);

create table if not exists public.employees
(
    id          bigserial
        primary key,
    user_id     bigint                                                not null
        unique
        constraint employees_users_id_fk
            references public.users
            on update cascade on delete cascade,
    location_id bigint                                                not null
        constraint employees_locations_id_fk
            references public.locations
            on update cascade on delete cascade,
    full_name   varchar(200)                                          not null,
    birthday    timestamp(6)                                          not null,
    education   varchar(50) default 'NOT_SELECTED'::character varying not null,
    health      varchar(50) default 'NOT_SELECTED'::character varying not null,
    gender      varchar(50) default 'NOT_SELECTED'::character varying not null,
    created     timestamp(6)                                          not null,
    changed     timestamp(6)                                          not null,
    is_deleted  boolean     default false                             not null
);

alter table public.employees
    owner to dev;

create index if not exists employees_birthday_index
    on public.employees (birthday desc);

create index if not exists employees_location_id_index
    on public.employees (location_id);

create unique index if not exists employees_user_id_uindex
    on public.employees (user_id);

create index if not exists employees_full_name_index
    on public.employees (full_name);

create table if not exists public.companies
(
    id          bigserial
        primary key,
    user_id     bigint                not null
        unique
        constraint companies_users_id_fk
            references public.users
            on update cascade on delete cascade,
    location_id bigint                not null
        constraint companies_locations_id_fk
            references public.locations
            on update cascade on delete cascade,
    full_title  varchar(400)          not null,
    short_title varchar(100)          not null,
    reg_number  varchar(200)          not null,
    created     timestamp(6)          not null,
    changed     timestamp(6)          not null,
    is_deleted  boolean default false not null,
    org_type    varchar(40)           not null
);

alter table public.companies
    owner to dev;

create index if not exists companies_location_id_index
    on public.companies (location_id);

create unique index if not exists companies_user_id_uindex
    on public.companies (user_id);

create index if not exists companies_full_name_index
    on public.companies (full_title);

create index if not exists companies_short_name_index
    on public.companies (short_title);

create index if not exists companies_short_name_location_id_index
    on public.companies (short_title, location_id);

create index if not exists companies_reg_number_index
    on public.companies (reg_number);

create table if not exists public.ranks
(
    id         bigserial
        primary key,
    rank_name  varchar(100)          not null
        constraint ranks_rank_key
            unique,
    created    timestamp(6)          not null,
    changed    timestamp(6)          not null,
    is_visible boolean default true  not null,
    is_deleted boolean default false not null
);

alter table public.ranks
    owner to dev;

create unique index if not exists ranks_rank_uindex
    on public.ranks (rank_name);

create table if not exists public.skills
(
    id                bigserial
        primary key,
    employee_id       bigint                                                 not null
        constraint skills_employees_id_fk
            references public.employees
            on update cascade on delete cascade,
    industry_id       bigint                                                 not null
        constraint skills_industries_id_fk
            references public.industries
            on update cascade,
    profession_id     bigint                                                 not null
        constraint skills_professions_id_fk
            references public.professions
            on update cascade,
    specialization_id bigint                                                 not null
        constraint skills_specializations_id_fk
            references public.specializations
            on update cascade,
    rank_id           bigint                                                 not null
        constraint skills_ranks_id_fk
            references public.ranks
            on update cascade,
    position_id       bigint                                                 not null
        constraint skills_positions_id_fk
            references public.positions
            on update cascade,
    experience        integer      default 0                                 not null,
    is_active         boolean      default true                              not null,
    recommendations   integer      default 0                                 not null,
    equipments        varchar(500) default 'NOT_SELECTED'::character varying not null,
    salary_min        integer                                                not null,
    salary_max        integer                                                not null,
    term_min          integer                                                not null,
    term_max          integer                                                not null,
    created           timestamp(6)                                           not null,
    changed           timestamp(6)                                           not null,
    is_deleted        boolean      default false                             not null
);

alter table public.skills
    owner to dev;

create index if not exists skills_created_index
    on public.skills (created desc);

create index if not exists skills_employee_id_index
    on public.skills (employee_id);

create index if not exists skills_industry_id_index
    on public.skills (industry_id);

create index if not exists skills_industry_id_profession_id_specialization_id_rank_id_prof
    on public.skills (industry_id, profession_id, specialization_id, rank_id, profession_id);

create index if not exists skills_position_id_index
    on public.skills (position_id);

create index if not exists skills_profession_id_index
    on public.skills (profession_id);

create index if not exists skills_rank_id_index
    on public.skills (rank_id);

create index if not exists skills_specialization_id_index
    on public.skills (specialization_id);

create table if not exists public.l_skills_locations
(
    id          bigserial
        primary key,
    skill_id    bigint                not null
        constraint l_skills_locations_skills_id_fk
            references public.skills
            on update cascade on delete cascade,
    location_id bigint                not null
        constraint l_skills_locations_locations_id_fk
            references public.locations
            on update cascade on delete cascade,
    created     timestamp(6)          not null,
    changed     timestamp(6)          not null,
    is_deleted  boolean default false not null
);

alter table public.l_skills_locations
    owner to dev;

create index if not exists l_skills_locations_locations_id_index
    on public.l_skills_locations (location_id);

create index if not exists l_skills_locations_skills_id_index
    on public.l_skills_locations (skill_id);

create unique index if not exists l_skills_locations_skills_id_locations_id_uindex
    on public.l_skills_locations (skill_id, location_id);

create table if not exists public.requirements
(
    id                bigserial
        primary key,
    company_id        bigint                not null
        constraint requirements_companies_id_fk
            references public.companies
            on update cascade on delete cascade,
    industry_id       bigint                not null
        constraint requirements_industries_id_fk
            references public.industries
            on update cascade,
    profession_id     bigint                not null
        constraint requirements_professions_id_fk
            references public.professions
            on update cascade,
    specialization_id bigint                not null
        constraint requirements_specializations_id_fk
            references public.specializations
            on update cascade,
    rank_id           bigint                not null
        constraint requirements_ranks_id_fk
            references public.ranks
            on update cascade,
    location_id       bigint                not null
        constraint requirements_locations_id_fk
            references public.locations
            on update cascade,
    experience        integer               not null,
    must_be_active    boolean default true  not null,
    recommendations   integer               not null,
    equipments        varchar(500)          not null,
    salary            integer               not null,
    term              integer               not null,
    created           timestamp(6)          not null,
    changed           timestamp(6)          not null,
    is_deleted        boolean default false not null,
    position_id       bigint                not null
        constraint requirements_positions_id_fk
            references public.positions
            on update cascade
);

alter table public.requirements
    owner to dev;

create index if not exists requirements_company_id_index
    on public.requirements (company_id);

create index if not exists requirements_created_index
    on public.requirements (created desc);

create index if not exists requirements_industry_id_index
    on public.requirements (industry_id);

create index if not exists requirements_industry_id_profession_id_specialization_id_rank_i
    on public.requirements (industry_id, profession_id, specialization_id, rank_id, position_id, location_id);

create index if not exists requirements_location_id_index
    on public.requirements (location_id);

create index if not exists requirements_position_id_index
    on public.requirements (position_id);

create index if not exists requirements_profession_id_index
    on public.requirements (profession_id);

create index if not exists requirements_rank_id_index
    on public.requirements (rank_id);

create index if not exists requirements_specialization_id_index
    on public.requirements (specialization_id);

create table if not exists public.offers
(
    id                bigint       default nextval('l_skills_requirements_id_seq'::regclass) not null
        constraint l_skills_requirements_pkey
            primary key,
    skill_id          bigint                                                                 not null
        constraint l_skills_requirements_skills_id_fk
            references public.skills
            on update cascade on delete cascade,
    requirement_id    bigint                                                                 not null
        constraint l_skills_requirements_requirements_id_fk
            references public.requirements
            on update cascade on delete cascade,
    created           timestamp(6)                                                           not null,
    changed           timestamp(6)                                                           not null,
    is_deleted        boolean      default false                                             not null,
    is_accepted       boolean      default false                                             not null,
    is_contracted     boolean      default false                                             not null,
    comments_employee varchar(500) default 'NO_COMMENTS'::character varying                  not null,
    comments_company  varchar(500) default 'NO_COMMENTS'::character varying                  not null
);

alter table public.offers
    owner to dev;

create index if not exists l_skills_requirements_changed_index
    on public.offers (changed desc);

create index if not exists l_skills_requirements_created_index
    on public.offers (created desc);

create index if not exists l_skills_requirements_requirements_id_index
    on public.offers (requirement_id);

create index if not exists l_skills_requirements_skills_id_index
    on public.offers (skill_id);

create unique index if not exists l_skills_requirements_skills_id_requirements_id_uindex
    on public.offers (skill_id, requirement_id);

