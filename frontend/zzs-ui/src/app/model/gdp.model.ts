import {Region} from './enums/region.enum';

export interface GdpStat {
    id: number;
    region: Region;
    year: number;
    gdpBillion: number;
    growthPercent: number;
    cpiPercent: number;
}
