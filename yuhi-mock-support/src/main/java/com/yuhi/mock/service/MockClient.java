package com.yuhi.mock.service;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.alibaba.dubbo.container.spring.SpringContainer;
import com.alibaba.fastjson.JSON;
import com.yuhi.base.web.spring.SpringUtils;
import com.yuhi.mock.Constants;
import com.yuhi.mock.entity.MockMethod;
import com.yuhi.mock.entity.MockTestConfig;
import com.yuhi.mock.exception.MockAccessException;
import com.yuhi.mock.service.impl.LocalMockMethodApdateImpl;
import com.yuhi.util.PropertiesUtil;
import com.yuhi.util.TypeConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * ClassName MockClient.
 * @coment: 测试系统调用插件
 * @user: 李森林
 * @category @author www.justintoForest@Gamil.com
 * @since: 2017-05-12 下午 05:11
 */
public class MockClient {

    private static final Logger logger = LoggerFactory.getLogger(MockClient.class);

    private static MockTestConfig mockTestConfig=null;
    /**
     * @category 发起调用请求.
     * @author www.justintoForest@Gamil.com
     * @since 2017-05-12 下午 05:11
     */
    public static Object send(Map paramMap, String getInterfaceInfo) {
        if(mockTestConfig==null)mockTestConfig= MockTestConfig.getinstance();
        if(mockTestConfig==null)return null;
//        MockMethodApdate mockbeanMethod = SpringUtils.getBean(MockMethodApdate.class);
        logger.debug("API-Name:"+paramMap.get(Constants.API.API_NAME));
        logger.debug("API-Method:"+paramMap.get(Constants.API.METHOD_NAME));
        logger.debug("API-Method-Params:"+paramMap.get(Constants.API.METHOD_PARAMS));
//        logger.debug("接口方法返回值类型:"+paramMap.get(Constants.API.RESULT_TYPE));
        logger.debug("API-Method-Input:"+paramMap.get(Constants.API.CALL_PARAMS_JSON));
        try {
            MockMethodApdate mockbeanMethod = mockTestConfig.getHolder();
            mockbeanMethod.init(paramMap);
            if(!mockbeanMethod.validApi()){
                logger.error("The [API-METHOD] configuration failed validation！");
                return null;
            }
            MockMethod mockMethod = mockbeanMethod.loadMethod();
            Class<?> aClass = Class.forName(mockMethod.getResultType());
            if(aClass!=null) {
                return JSON.parseObject(mockMethod.getResult(), aClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.error("Output Result Parameter structure ERROR！！");
        return null;
    }
}
