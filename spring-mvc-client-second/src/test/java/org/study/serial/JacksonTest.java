package org.study.serial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhcf.backend.model.UserModel;

/**
 * @Title: JacksonTest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月22日 下午3:27:45
 * @see {@linkplain http://blog.csdn.net/u013256816/article/details/50721421}
 */
public class JacksonTest {
	private UserModel user = null;
	private JsonGenerator jsonGenerator = null;
	private ObjectMapper objectMapper = null;

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

		objectMapper = new ObjectMapper();
		try {
			// jsonGenerator =
			// objectMapper.getJsonFactory().createJsonGenerator(System.out,
			// JsonEncoding.UTF8);
			jsonGenerator = objectMapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void destory() {
		try {
			if (jsonGenerator != null) {
				jsonGenerator.flush();
			}
			if (!jsonGenerator.isClosed()) {
				jsonGenerator.close();
			}
			jsonGenerator = null;
			objectMapper = null;
			user = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void writeJson() {
		try {
			jsonGenerator.writeObject(user);
			System.out.println();
			System.out.println(objectMapper.writeValueAsBytes(user).length);
			System.out.println(new String(objectMapper.writeValueAsBytes(user)));
			// System.out.println(objectMapper.writeValueAsString(user).length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void readJson() {
		String serString = "{\"name\":\"zzh\",\"age\":18,\"friends\":[{\"name\":\"jj\",\"age\":17,\"friends\":null},{\"name\":\"qq\",\"age\":19,\"friends\":null}]}";
		UserModel uservo = null;
		try {
			uservo = objectMapper.readValue(serString, UserModel.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(uservo.getName());
	}
}
