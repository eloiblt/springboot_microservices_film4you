import { Film } from "./film.model";

export interface Preference {
  id?: number;
  userId?: number;
  filmId?: string;
  mark?: number;
  film?: Film;
}