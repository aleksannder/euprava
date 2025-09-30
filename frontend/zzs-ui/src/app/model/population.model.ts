import {Region} from './enums/region.enum';

export interface PopulationStat {
    id: number;
    region: Region;
    year: number;
    population: number;
    averageAge: number;
    birthRate: number;
    mortalityRate: number;
}

export interface AvgAgeData {
  region: Region;
  regionLabel: string;
  averageAge: number;
}

export interface RegionValueDto {
  region: Region;
  value: number
}

export interface ExtremesData {
  youngestRegion: Region;
  youngestAge: number;
  oldestRegion: Region;
  oldestAge: number;
}

export interface ProjectionData {
  year: number;
  projectedPopulation: number;
}
