package me.ruby.test;

import me.ruby.tea.Constants;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;


/**
 * Created by ruby on 2016/10/14.
 * Email:liyufeng_23@163.com
 */
public class ReadProperyTest extends BaseSpringTestRunner {

    private static final Logger logger = LoggerFactory.getLogger("Read Properties File Test Log");

    @Test
    public void testReadProperties() {
        logger.info("find properties in wpms");
        assertEquals("wx366d2eb74816c1bb", Constants.WX_APPID);
    }

}
