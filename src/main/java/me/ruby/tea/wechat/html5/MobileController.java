package me.ruby.tea.wechat.html5;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ruby on 2016/10/19.
 * Email:liyufeng_23@163.com
 */
@Controller
public class MobileController {

    @RequestMapping("/mobile")
    public String home() {
        return "mobile/index";
    }
}
