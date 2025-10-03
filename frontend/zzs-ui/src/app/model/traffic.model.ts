import {Region} from './enums/region.enum';

export interface TrafficStat {
    id: number;
    region: Region;
    year: number;
    registeredVehicles: number;
    trafficAccidents: number;
    fatalities: number;
}

export interface TrafficSummary {
  year: number;
  totalVehicles: number;
  totalAccidents: number;
}

export interface DangerousRegion {
  region: Region;
  avgAccidents: number;
}

export interface FatalitiesTrend {
  year: number;
  region: Region;
  fatalities: number;
}
