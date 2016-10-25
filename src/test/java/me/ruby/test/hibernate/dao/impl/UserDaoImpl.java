package me.ruby.test.hibernate.dao.impl;

import me.ruby.common.persistence.hibernate.dao.impl.BaseDaoImpl;
import me.ruby.tea.entity.User;
import me.ruby.test.hibernate.dao.UserDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ruby on 2016/10/25.
 * Email:liyufeng_23@163.com
 */
@Repository("userTestDao")
public class UserDaoImpl extends BaseDaoImpl<User, Integer> implements UserDao {
}
