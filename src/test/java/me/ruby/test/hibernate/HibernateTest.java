package me.ruby.test.hibernate;

import me.ruby.tea.entity.User;
import me.ruby.test.BaseSpringTestRunner;
import me.ruby.test.hibernate.dao.UserDao;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ruby on 2016/10/25.
 * Email:liyufeng_23@163.com
 */
public class HibernateTest extends  BaseSpringTestRunner {

    private static final Logger logger = LoggerFactory.getLogger(HibernateTest.class);

    @Autowired
    private UserDao userTestDao;

    @Test
    @Transactional
    @Rollback(false)
    public void testHibernate() throws Exception {
        User user = new User();
        user.setName("jack");

        userTestDao.save(user);
    }


}
