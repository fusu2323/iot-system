-- 更新用户密码为 admin123 (BCrypt 加密)
UPDATE `user` SET `password` = '$2b$10$zx9GL3uyMlOazwcqD0UfneEVkcYKCFOm15O5baLQw6boP2WYZM9iu' WHERE `username` = 'admin';
UPDATE `user` SET `password` = '$2b$10$zx9GL3uyMlOazwcqD0UfneEVkcYKCFOm15O5baLQw6boP2WYZM9iu' WHERE `username` = 'user';
