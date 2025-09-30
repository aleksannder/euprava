import {Region} from './enums/region.enum';

export interface WageStat {
    id: number;
    region: Region;
    year: number;
    averageWage: number;
    growthPercent: number;
}

export interface WageGrowth {
  region: Region;
  growthPercent: number;
  growthRsd: number;
}
