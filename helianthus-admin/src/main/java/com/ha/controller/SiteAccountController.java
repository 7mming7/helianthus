package com.ha.controller;

import com.ha.domain.SiteAccount;
import com.ha.service.ISiteAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账户管理controller
 * User: shuiqing
 * DateTime: 16/8/23 下午10:42
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
public class SiteAccountController {

    @Autowired
    private ISiteAccountService iSiteAccountService;

    @RequestMapping("/accountManagement")
    public ModelAndView accountManagement() {
        return new ModelAndView("account_management");
    }

    @RequestMapping(value = "/queryPageAccounts", method = RequestMethod.GET)
    @ResponseBody
    public Object querySiteAccounts(int pageIndex, int pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();
        Page<SiteAccount> data = iSiteAccountService.querySiteAccounts(pageIndex, pageSize);
        result.put("rows", data.getContent());
        result.put("total", data.getTotalElements());
        return result;
    }

    @RequestMapping(value = "/addOrUpdateAccount", method = RequestMethod.POST)
    @ResponseBody
    public Object addAccount(SiteAccount siteAccount) {
        if (StringUtils.isBlank(siteAccount.getDomain())) {
            return false;
        }
        return iSiteAccountService.addOrUpdateAccount(siteAccount);
    }

    @RequestMapping(value = "/deleteAccounts", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteAccounts(@RequestBody List<String> deleteIds) {
        Assert.notNull(deleteIds);
        return iSiteAccountService.deleteAccountByIds(deleteIds);
    }
}
