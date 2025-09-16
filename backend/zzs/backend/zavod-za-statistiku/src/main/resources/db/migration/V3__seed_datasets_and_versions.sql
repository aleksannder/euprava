-- Po jedan dataset po indikatoru + po jedna PUBLISHED verzija

-- population dataset
insert into dataset(name, description, status, is_public, created_by, indicator_id)
select 'pop_srbijа_rzs','Stanovništvo – RZS', 'PUBLISHED', true, 'seed', i.id
from indicator i where i.code='population_by_age_gender'
    on conflict (name) do nothing;

insert into dataset_version(dataset_id, version, schema_json, checksum, row_count, created_at, published_at, published_by)
select d.id, '1.0.0', null, null, 0, now(), now(), 'seed'
from dataset d where d.name='pop_srbijа_rzs'
    on conflict do nothing;

-- salary dataset
insert into dataset(name, description, status, is_public, created_by, indicator_id)
select 'salaries_rzs','Prosečna neto zarada – RZS', 'PUBLISHED', true, 'seed', i.id
from indicator i where i.code='avg_net_salary'
    on conflict (name) do nothing;

insert into dataset_version(dataset_id, version, schema_json, checksum, row_count, created_at, published_at, published_by)
select d.id, '1.0.0', null, null, 0, now(), now(), 'seed'
from dataset d where d.name='salaries_rzs'
    on conflict do nothing;

-- hospital beds dataset
insert into dataset(name, description, status, is_public, created_by, indicator_id)
select 'beds_moh','Bolnički kreveti – MOH', 'PUBLISHED', true, 'seed', i.id
from indicator i where i.code='hospital_beds'
    on conflict (name) do nothing;

insert into dataset_version(dataset_id, version, schema_json, checksum, row_count, created_at, published_at, published_by)
select d.id, '1.0.0', null, null, 0, now(), now(), 'seed'
from dataset d where d.name='beds_moh'
    on conflict do nothing;

-- traffic dataset (ostavimo DRAFT)
insert into dataset(name, description, status, is_public, created_by, indicator_id)
select 'traffic_roads','Saobraćaj – putevi', 'DRAFT', false, 'seed', i.id
from indicator i where i.code='daily_traffic_volume'
    on conflict (name) do nothing;

insert into dataset_version(dataset_id, version, schema_json, checksum, row_count, created_at, published_at, published_by)
select d.id, '0.9.0', null, null, 0, now(), null, null
from dataset d where d.name='traffic_roads'
    on conflict do nothing;