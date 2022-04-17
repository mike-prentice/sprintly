import { IUser } from 'app/shared/model/user.model';
import { IRanking } from 'app/shared/model/ranking.model';

export interface IStats {
  id?: number;
  distanceRan?: number | null;
  time?: string | null;
  cadence?: number | null;
  avgpace?: number | null;
  user?: IUser | null;
  ranking?: IRanking | null;
}

export const defaultValue: Readonly<IStats> = {};
