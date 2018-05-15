package com.hhcf.system.listner;

import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Component;

/**
 * @Title: ConfirmCallBackListener
 * @Description:确认后回调
 * @Author: zhaotf
 * @Since:2018年5月15日 下午3:39:51
 * @see {@linkplain https://blog.csdn.net/qq315737546/article/details/54176560}
 */
@Component("confirmCallBackListener")
public class ConfirmCallBackListener implements ConfirmCallback {

	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		// TODO Auto-generated method stub
		System.out.println("confirm--:correlationData:" + correlationData + ",ack:" + ack + ",cause:" + cause);
	}

}
