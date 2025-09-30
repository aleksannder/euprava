import {Region} from './enums/region.enum';

export interface CitizenDashboard {
  region: Region;
  population: number | null;
  averageAge: number | null;
  gdpBillion: number | null;
  wage: number | null;
  registeredVehicles: number | null;
  trafficAccidents: number | null;
}
