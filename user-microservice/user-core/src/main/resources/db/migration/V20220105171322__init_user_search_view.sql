CREATE OR REPLACE VIEW user_search AS SELECT u.id, u.lastname, u.firstname FROM public.user u WHERE u.visibility = 'PUBLIC';
