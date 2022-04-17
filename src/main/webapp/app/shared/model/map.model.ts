import dayjs from 'dayjs';
import { IStats } from 'app/shared/model/stats.model';

export interface IMap {
  id?: number;
  distance?: number | null;
  timeStart?: string | null;
  timeStop?: string | null;
  stats?: IStats | null;
}

export const defaultValue: Readonly<IMap> = {};
