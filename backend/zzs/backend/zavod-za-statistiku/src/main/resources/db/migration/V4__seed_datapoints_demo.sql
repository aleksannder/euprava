-- population demo data
insert into data_point(indicator_id, dataset_version_id, dims, measures)
select i.id, dv.id,
       jsonb_build_object('year', 2024, 'municipalityCode','11000','ageBucket','25-34','gender','F'),
       jsonb_build_object('count', 123)
from indicator i
         join dataset d on d.indicator_id=i.id and d.name='pop_srbijа_rzs'
         join dataset_version dv on dv.dataset_id=d.id and dv.published_at is not null
where i.code='population_by_age_gender';

insert into data_point(indicator_id, dataset_version_id, dims, measures)
select i.id, dv.id,
       jsonb_build_object('year', 2024, 'municipalityCode','11000','ageBucket','25-34','gender','M'),
       jsonb_build_object('count', 119)
from indicator i
         join dataset d on d.indicator_id=i.id and d.name='pop_srbijа_rzs'
         join dataset_version dv on dv.dataset_id=d.id and dv.published_at is not null
where i.code='population_by_age_gender';

-- salary demo data
insert into data_point(indicator_id, dataset_version_id, dims, measures)
select i.id, dv.id,
       jsonb_build_object('month','2025-07','municipalityCode','11000'),
       jsonb_build_object('netSalary', 86543.0)
from indicator i
         join dataset d on d.indicator_id=i.id and d.name='salaries_rzs'
         join dataset_version dv on dv.dataset_id=d.id and dv.published_at is not null
where i.code='avg_net_salary';

insert into data_point(indicator_id, dataset_version_id, dims, measures)
select i.id, dv.id,
       jsonb_build_object('month','2025-08','municipalityCode','11000'),
       jsonb_build_object('netSalary', 87210.0)
from indicator i
         join dataset d on d.indicator_id=i.id and d.name='salaries_rzs'
         join dataset_version dv on dv.dataset_id=d.id and dv.published_at is not null
where i.code='avg_net_salary';

-- hospital beds demo data
insert into data_point(indicator_id, dataset_version_id, dims, measures)
select i.id, dv.id,
       jsonb_build_object('year', 2024, 'region','BG','ownership','PUBLIC'),
       jsonb_build_object('beds', 5400)
from indicator i
         join dataset d on d.indicator_id=i.id and d.name='beds_moh'
         join dataset_version dv on dv.dataset_id=d.id and dv.published_at is not null
where i.code='hospital_beds';