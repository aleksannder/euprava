-- Domene
insert into domain(code,name) values
                                  ('ekonomija','Ekonomija'),
                                  ('zdravlje','Zdravlje'),
                                  ('saobracaj','Saobraćaj'),
                                  ('gradjani','Građani')
    on conflict (code) do nothing;

-- Podoblasti
insert into subdomain(domain_id, code, name)
select id, 'trziste_rada', 'Tržište rada' from domain where code='ekonomija'
union all
select id, 'zdravstvena_infra', 'Zdravstvena infrastruktura' from domain where code='zdravlje'
union all
select id, 'saobracaj_protok', 'Saobraćaj – protok' from domain where code='saobracaj'
union all
select id, 'demografija', 'Demografija' from domain where code='gradjani';

-- Indikatori
insert into indicator(subdomain_id, code, name, description)
select s.id, 'avg_net_salary', 'Prosečna neto zarada', 'Prosek neto zarade po mesecu/opštini'
from subdomain s join domain d on d.id=s.domain_id where d.code='ekonomija' and s.code='trziste_rada'
union all
select s.id, 'hospital_beds', 'Bolnički kreveti', 'Zbir kreveta po regionu/godini'
from subdomain s join domain d on d.id=s.domain_id where d.code='zdravlje' and s.code='zdravstvena_infra'
union all
select s.id, 'daily_traffic_volume', 'Dnevni saobraćaj', 'Broj vozila po danu/opštini/tipu puta'
from subdomain s join domain d on d.id=s.domain_id where d.code='saobracaj' and s.code='saobracaj_protok'
union all
select s.id, 'population_by_age_gender', 'Stanovništvo po starosti i polu', 'Broj stanovnika po opštini/starosnim grupama/polu'
from subdomain s join domain d on d.id=s.domain_id where d.code='gradjani' and s.code='demografija';

-- Dimenzije
insert into dimension(name, type) values
                                      ('year','INT'),
                                      ('month','STRING'),
                                      ('date','DATE'),
                                      ('region','STRING'),
                                      ('municipalityCode','STRING'),
                                      ('ownership','STRING'),
                                      ('roadType','STRING'),
                                      ('ageBucket','STRING'),
                                      ('gender','STRING')
    on conflict (name, type) do nothing;

-- Jedinice
insert into unit(code,name) values
                                ('RSD','Dinars'),
                                ('people','People'),
                                ('count','Count')
    on conflict (code) do nothing;

-- Veze indikator ↔ dimenzije
-- population: year, municipalityCode, ageBucket, gender
insert into indicator_dimension(indicator_id, dimension_id, ord)
select i.id, d.id, x.ord
from indicator i
         join (values ('year',1),('municipalityCode',2),('ageBucket',3),('gender',4)) x(name,ord) on true
         join dimension d on d.name=x.name
where i.code='population_by_age_gender'
    on conflict do nothing;

-- salary: month, municipalityCode
insert into indicator_dimension(indicator_id, dimension_id, ord)
select i.id, d.id, x.ord
from indicator i
         join (values ('month',1),('municipalityCode',2)) x(name,ord) on true
         join dimension d on d.name=x.name
where i.code='avg_net_salary'
    on conflict do nothing;

-- beds: year, region, ownership
insert into indicator_dimension(indicator_id, dimension_id, ord)
select i.id, d.id, x.ord
from indicator i
         join (values ('year',1),('region',2),('ownership',3)) x(name,ord) on true
         join dimension d on d.name=x.name
where i.code='hospital_beds'
    on conflict do nothing;

-- traffic: date, municipalityCode, roadType
insert into indicator_dimension(indicator_id, dimension_id, ord)
select i.id, d.id, x.ord
from indicator i
         join (values ('date',1),('municipalityCode',2),('roadType',3)) x(name,ord) on true
         join dimension d on d.name=x.name
where i.code='daily_traffic_volume'
    on conflict do nothing;

-- Mere
insert into measure(indicator_id, name, agg, unit_id)
select i.id, 'count','SUM', u.id
from indicator i join unit u on u.code='people'
where i.code='population_by_age_gender'
    on conflict do nothing;

insert into measure(indicator_id, name, agg, unit_id)
select i.id, 'netSalary','AVG', u.id
from indicator i join unit u on u.code='RSD'
where i.code='avg_net_salary'
    on conflict do nothing;

insert into measure(indicator_id, name, agg, unit_id)
select i.id, 'beds','SUM', u.id
from indicator i join unit u on u.code='count'
where i.code='hospital_beds'
    on conflict do nothing;

insert into measure(indicator_id, name, agg, unit_id)
select i.id, 'vehicleCount','SUM', u.id
from indicator i join unit u on u.code='count'
where i.code='daily_traffic_volume'
    on conflict do nothing;