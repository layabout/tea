package me.ruby.tea.service.impl;

import me.ruby.tea.dao.UserDao;
import me.ruby.tea.entity.User;
import me.ruby.tea.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ruby on 2016/10/25.
 * Email:liyufeng_23@163.com
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void save(User user) {
        userDao.save(user);
    }
}
