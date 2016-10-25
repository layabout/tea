package me.ruby.tea.dao.impl;

import me.ruby.common.persistence.hibernate.dao.impl.BaseDaoImpl;
import me.ruby.tea.dao.UserDao;
import me.ruby.tea.entity.User;
import org.springframework.stereotype.Repository;

/**
 * Created by ruby on 2016/10/25.
 * Email:liyufeng_23@163.com
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl<User, Integer> implements UserDao {
}
