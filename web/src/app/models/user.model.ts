export interface User {
  id?: number;
  email?: string;
  firstname?: string;
  lastname?: string;
  visibility?: string;
  summary?: {
    actor?: string;
    director?: string;
    filmviewed?: number;
    genre?: string;
    production?: string;
    totalDuration?: number;
  }
}