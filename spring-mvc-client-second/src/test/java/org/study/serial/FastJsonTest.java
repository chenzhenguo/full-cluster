package org.study.serial;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.hhcf.backend.model.UserModel;

/**
 * @Title: FastJsonTest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月22日 下午3:43:41
 * @see {@linkplain http://blog.csdn.net/u013256816/article/details/50721421}
 */
public class FastJsonTest {
	private UserModel user = null;

	@Before
	public void init() {
		user = new UserModel();
		user.setName("zzh");
		user.setAge(18);

		UserModel f1 = new UserModel();
		f1.setName("jj");
		f1.setAge(17);
		UserModel f2 = new UserModel();
		f2.setName("qq");
		f2.setAge(19);

		List<UserModel> friends = new ArrayList<UserModel>();
		friends.add(f1);
		friends.add(f2);
		user.setFriends(friends);
	}

	@Test
	public void writeJson() {
		String str = JSON.toJSONString(user);
		System.out.println(str);
		System.out.println(str.length());
	}

	@Test
	public void readJson() {
		String serString = "{\"name\":\"zzh\",\"age\":18,\"friends\":[{\"name\":\"jj\",\"age\":17},{\"name\":\"qq\",\"age\":19}]}";
		UserModel userVo = JSON.parseObject(serString, UserModel.class);
		System.out.println(userVo.getName());
	}

}
