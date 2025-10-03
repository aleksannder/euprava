export enum Region {
  RS11_BELGRADE = "RS11_BELGRADE",
  RS12_VOJVODINA = "RS12_VOJVODINA",
  RS21_WEST_SUMADIJA = "RS21_WEST_SUMADIJA",
  RS22_SOUTH_EAST = "RS22_SOUTH_EAST",
  RS23_KOSOVO_AND_METOHIJA = "RS23_KOSOVO_AND_METOHIJA"
}

export class RegionUtil {
  static getLabel(region: Region): string {
    switch (region) {
      case Region.RS11_BELGRADE:
        return 'Beogradski region';
      case Region.RS12_VOJVODINA:
        return 'Region Vojvodine';
      case Region.RS21_WEST_SUMADIJA:
        return 'Region Šumadije i Zapadne Srbije';
      case Region.RS22_SOUTH_EAST:
        return 'Region Južne i Istočne Srbije';
      case Region.RS23_KOSOVO_AND_METOHIJA:
        return 'Kosovo i Metohija';
      default:
        return region; // fallback ako se pojavi nešto neočekivano
    }
  }

  static getAllWithLabels(): { value: Region, label: string }[] {
    return Object.values(Region).map(r => ({
      value: r,
      label: this.getLabel(r as Region)
    }));
  }
}
