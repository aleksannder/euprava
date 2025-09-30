import {Region} from './enums/region.enum';

export interface GdpGrowth {
  region: Region,
  year: number;
  growthPercent: number;
  growthBillion: number;
}
